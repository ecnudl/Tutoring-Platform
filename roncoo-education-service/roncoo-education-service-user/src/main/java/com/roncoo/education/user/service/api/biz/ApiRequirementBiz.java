package com.roncoo.education.user.service.api.biz;

import com.roncoo.education.common.base.BaseBiz;
import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.base.page.PageUtil;
import com.roncoo.education.common.cache.CacheRedis;
import com.roncoo.education.common.core.base.Constants;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.common.core.enums.RequirementStatusEnum;
import com.roncoo.education.common.tools.BeanUtil;
import com.roncoo.education.common.tools.IdWorker;
import com.roncoo.education.common.tools.IpUtil;
import com.roncoo.education.user.dao.TutorRequirementDao;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorRequirement;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorRequirementExample;
import com.roncoo.education.user.service.api.req.RequirementQuickSubmitReq;
import com.roncoo.education.user.service.api.req.RequirementSearchReq;
import com.roncoo.education.user.service.api.resp.RequirementDetailResp;
import com.roncoo.education.user.service.api.resp.RequirementListResp;
import com.roncoo.education.user.service.api.resp.RequirementSearchResp;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * API-需求列表
 *
 * @author fengyw
 */
@Component
@RequiredArgsConstructor
public class ApiRequirementBiz extends BaseBiz {

    @NotNull
    private final TutorRequirementDao tutorRequirementDao;
    @NotNull
    private final CacheRedis cacheRedis;
    @NotNull
    private final HttpServletRequest request;

    /**
     * 需求搜索（分页+多条件）
     * 设计:
     *   SQL 只做 status / 排序 / 一次性拉全部
     *   Java 后过滤所有 filter, 实现 NULL = 通配符 + online 跨城市:
     *     - cityId / district / subject / tutorType / teachingMethod 任一字段 NULL => 视为通配, 该 filter 命中
     *     - teachingMethod=3 (在线) 跳过 cityId/district (位置无关)
     *     - MATCHED 且 matched_at > 24h 前不再展示
     *   再 Java 切片做分页. 量级 <几百行, 性能可接受.
     */
    public Result<Page<RequirementSearchResp>> search(RequirementSearchReq req) {
        TutorRequirementExample example = new TutorRequirementExample();
        TutorRequirementExample.Criteria c = example.createCriteria();
        c.andReqStatusIn(java.util.Arrays.asList(
                RequirementStatusEnum.PUBLISHED.getCode(),
                RequirementStatusEnum.MATCHED.getCode()));

        // SQL 排序: MATCHED 沉底, 同状态 id desc
        example.setOrderByClause(
                "CASE WHEN req_status = " + RequirementStatusEnum.MATCHED.getCode()
                        + " THEN 1 ELSE 0 END ASC, is_urgent DESC, id DESC");
        // 一次性拉全部 (PUBLISHED + MATCHED). 当前规模可接受.
        Page<TutorRequirement> rawPage = tutorRequirementDao.page(1, 5000, example);
        java.util.List<TutorRequirement> all = rawPage.getList() != null ? rawPage.getList() : java.util.Collections.emptyList();

        // Java 后过滤
        LocalDateTime matchedCutoff = LocalDateTime.now().minusHours(24);
        java.util.List<TutorRequirement> filtered = all.stream()
                .filter(r -> notExpiredMatched(r, matchedCutoff))
                .filter(r -> matchTeachingMethod(r, req))
                .filter(r -> matchCity(r, req))
                .filter(r -> matchDistrict(r, req))
                .filter(r -> matchSubject(r, req))
                .filter(r -> matchTutorType(r, req))
                .filter(r -> matchKeyword(r, req))
                .filter(r -> matchGrade(r, req))
                .collect(java.util.stream.Collectors.toList());

        // Java 切片分页
        int pageCurrent = req.getPageCurrent() <= 0 ? 1 : req.getPageCurrent();
        int pageSize = req.getPageSize() <= 0 ? 20 : req.getPageSize();
        int from = Math.min((pageCurrent - 1) * pageSize, filtered.size());
        int to = Math.min(from + pageSize, filtered.size());

        Page<TutorRequirement> outPage = new Page<>();
        outPage.setList(filtered.subList(from, to));
        outPage.setTotalCount(filtered.size());
        outPage.setPageCurrent(pageCurrent);
        outPage.setPageSize(pageSize);
        // totalPage
        outPage.setTotalPage((int) Math.ceil(filtered.size() / (double) pageSize));
        return Result.success(PageUtil.transform(outPage, RequirementSearchResp.class));
    }

    /** MATCHED 状态停留 24h 后从列表消失 */
    private static boolean notExpiredMatched(TutorRequirement r, LocalDateTime cutoff) {
        if (!RequirementStatusEnum.MATCHED.getCode().equals(r.getReqStatus())) return true;
        return r.getMatchedAt() != null && r.getMatchedAt().isAfter(cutoff);
    }

    /** teachingMethod 严格匹配 (用户主动选才过滤) */
    private static boolean matchTeachingMethod(TutorRequirement r, RequirementSearchReq req) {
        if (req.getTeachingMethod() == null) return true;
        return req.getTeachingMethod().equals(r.getTeachingMethod());
    }

    /** city 通配规则: filter NULL → 命中; 订单 city NULL → 命中 (admin 没填); 订单是在线辅导 → 命中跨城市 */
    private static boolean matchCity(TutorRequirement r, RequirementSearchReq req) {
        if (req.getCityId() == null) return true;
        if (Integer.valueOf(3).equals(r.getTeachingMethod())) return true;
        if (r.getCityId() == null) return true;
        return req.getCityId().equals(r.getCityId());
    }

    /** district 通配规则: 同 city, 加上 LIKE 包含语义 */
    private static boolean matchDistrict(TutorRequirement r, RequirementSearchReq req) {
        if (!StringUtils.hasText(req.getDistrict())) return true;
        if (Integer.valueOf(3).equals(r.getTeachingMethod())) return true;
        if (!StringUtils.hasText(r.getDistrictNames())) return true;
        return r.getDistrictNames().contains(req.getDistrict());
    }

    /** subject 通配规则: filter 空命中, 订单 subject_ids 空命中, 否则 contains */
    private static boolean matchSubject(TutorRequirement r, RequirementSearchReq req) {
        if (!StringUtils.hasText(req.getSubject())) return true;
        if (!StringUtils.hasText(r.getSubjectIds())) return true;
        return r.getSubjectIds().contains(req.getSubject());
    }

    /** tutorType 同上 */
    private static boolean matchTutorType(TutorRequirement r, RequirementSearchReq req) {
        if (!StringUtils.hasText(req.getTutorType())) return true;
        if (!StringUtils.hasText(r.getTutorTypePref())) return true;
        return r.getTutorTypePref().contains(req.getTutorType());
    }

    /** keyword 命中 title (空字段视为命中) */
    private static boolean matchKeyword(TutorRequirement r, RequirementSearchReq req) {
        if (!StringUtils.hasText(req.getKeyword())) return true;
        if (!StringUtils.hasText(r.getTitle())) return true;
        return r.getTitle().contains(req.getKeyword());
    }

    /** grade 严格匹配 (空字段视为命中) */
    private static boolean matchGrade(TutorRequirement r, RequirementSearchReq req) {
        if (req.getGradeId() == null) return true;
        if (r.getGradeId() == null) return true;
        return req.getGradeId().equals(r.getGradeId());
    }

    /** 兼容老接口: latest / related 仍走 Example 简版查询, 这两边复用此过滤. */
    private static void applyMatchedCutoff(Page<TutorRequirement> page) {
        if (page == null || page.getList() == null) return;
        LocalDateTime cutoff = LocalDateTime.now().minusHours(24);
        Integer matched = RequirementStatusEnum.MATCHED.getCode();
        page.setList(page.getList().stream()
                .filter(r -> !matched.equals(r.getReqStatus())
                        || (r.getMatchedAt() != null && r.getMatchedAt().isAfter(cutoff)))
                .collect(java.util.stream.Collectors.toList()));
    }

    /**
     * 根据展示编号查看需求详情
     * 公开页面: 不暴露完整电话/微信; 联系人姓名脱敏为 X 女士/先生
     */
    public Result<RequirementDetailResp> viewByDisplayNo(String displayNo) {
        TutorRequirement requirement = tutorRequirementDao.getByDisplayNo(displayNo);
        if (requirement == null) {
            return Result.error("需求不存在");
        }
        Integer st = requirement.getReqStatus();
        if (!RequirementStatusEnum.PUBLISHED.getCode().equals(st)
                && !RequirementStatusEnum.MATCHED.getCode().equals(st)) {
            return Result.error("该需求暂不可查看");
        }
        // 增加浏览次数
        TutorRequirement update = new TutorRequirement();
        update.setId(requirement.getId());
        update.setViewCount((requirement.getViewCount() == null ? 0 : requirement.getViewCount()) + 1);
        tutorRequirementDao.updateById(update);

        RequirementDetailResp resp = BeanUtil.copyProperties(requirement, RequirementDetailResp.class);
        // 隐藏联系方式 (走客服匹配)
        resp.setContactMobile(null);
        // 联系人姓名脱敏: 朱小红 → 朱女士
        resp.setContactName(maskContactName(requirement.getContactName(), requirement.getStudentGender()));
        // 大致位置 (admin 录入的就是脱敏后的 — 直接展示)
        resp.setDisplayLocation(requirement.getAddress());
        return Result.success(resp);
    }

    /**
     * 把联系人姓名脱敏成 "X女士/先生"。无法识别时返回 "热心家长"。
     */
    private static String maskContactName(String contactName, Integer studentGender) {
        if (!StringUtils.hasText(contactName)) return "热心家长";
        // 已经包含称谓的就保留首字 + 称谓
        char first = contactName.charAt(0);
        if (contactName.contains("女士") || contactName.contains("阿姨") || contactName.contains("妈妈") || contactName.contains("妈")) {
            return first + "女士";
        }
        if (contactName.contains("先生") || contactName.contains("叔叔") || contactName.contains("爸爸") || contactName.contains("爸")) {
            return first + "先生";
        }
        // 没称谓 — 显示首字 + 家长
        return first + "家长";
    }

    /**
     * 相关订单 (同区域, 排除自己, 排除已过期已匹配, 最多 5 条)
     */
    public Result<List<RequirementListResp>> related(String displayNo, Integer limit) {
        if (limit == null || limit <= 0) limit = 5;
        TutorRequirement self = tutorRequirementDao.getByDisplayNo(displayNo);
        if (self == null) return Result.success(Collections.emptyList());

        TutorRequirementExample example = new TutorRequirementExample();
        TutorRequirementExample.Criteria c = example.createCriteria();
        c.andReqStatusIn(java.util.Arrays.asList(
                RequirementStatusEnum.PUBLISHED.getCode(),
                RequirementStatusEnum.MATCHED.getCode()));
        c.andIdNotEqualTo(self.getId());
        if (self.getDistrictId() != null) c.andDistrictIdEqualTo(self.getDistrictId());

        example.setOrderByClause(
                "CASE WHEN req_status = " + RequirementStatusEnum.MATCHED.getCode()
                        + " THEN 1 ELSE 0 END ASC, is_urgent DESC, id DESC");
        // 多取一些以备 24h 过滤
        Page<TutorRequirement> page = tutorRequirementDao.page(1, limit * 2, example);
        applyMatchedCutoff(page);
        List<TutorRequirement> list = page.getList();
        if (list.size() > limit) list = list.subList(0, limit);
        return Result.success(BeanUtil.copyProperties(list, RequirementListResp.class));
    }

    /**
     * 首页最新需求列表 (与 search 同样规则: PUBLISHED + 近 24h MATCHED, MATCHED 沉底)
     */
    public Result<List<RequirementListResp>> latest(Long cityId, Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10;
        }
        TutorRequirementExample example = new TutorRequirementExample();
        TutorRequirementExample.Criteria c = example.createCriteria();
        c.andReqStatusIn(java.util.Arrays.asList(
                RequirementStatusEnum.PUBLISHED.getCode(),
                RequirementStatusEnum.MATCHED.getCode()));
        if (cityId != null) c.andCityIdEqualTo(cityId);

        example.setOrderByClause(
                "CASE WHEN req_status = " + RequirementStatusEnum.MATCHED.getCode()
                        + " THEN 1 ELSE 0 END ASC, is_urgent DESC, id DESC");
        Page<TutorRequirement> page = tutorRequirementDao.page(1, limit, example);
        applyMatchedCutoff(page);
        return Result.success(BeanUtil.copyProperties(page.getList(), RequirementListResp.class));
    }

    /**
     * 游客快速提交家教需求(无需登录)
     */
    public Result<String> quickSubmit(RequirementQuickSubmitReq req) {
        if (!StringUtils.hasText(req.getContactName())) {
            return Result.error("联系人姓名不能为空");
        }
        if (!StringUtils.hasText(req.getContactMobile())) {
            return Result.error("联系电话不能为空");
        }
        // 手机号格式校验, 拦掉明显的假数据
        if (!req.getContactMobile().matches("^1[3-9]\\d{9}$")) {
            return Result.error("手机号格式不正确");
        }
        // 字段长度上限, 防巨大文本撑爆审核队列
        if (req.getContactName().length() > 50
                || (req.getContactWechat() != null && req.getContactWechat().length() > 50)
                || (req.getRequirementDetail() != null && req.getRequirementDetail().length() > 500)) {
            return Result.error("提交内容超过长度限制");
        }
        // IP 维度限流: 每 IP 每小时最多 3 次, 防游客接口被刷
        String ip = IpUtil.getIpAddress(request);
        String rateKey = Constants.RedisPre.RATE_LIMIT_IP + "qjj:" + ip;
        String cnt = cacheRedis.get(rateKey);
        int n = StringUtils.hasText(cnt) ? Integer.parseInt(cnt) : 0;
        if (n >= 3) {
            return Result.error("您的提交过于频繁, 请稍后再试");
        }
        cacheRedis.set(rateKey, String.valueOf(n + 1), 1, TimeUnit.HOURS);

        TutorRequirement requirement = new TutorRequirement();
        requirement.setId(IdWorker.getId());
        requirement.setUserId(0L); // 游客
        requirement.setContactName(req.getContactName());
        requirement.setContactMobile(req.getContactMobile());
        requirement.setContactWechat(req.getContactWechat());
        requirement.setRequirementDetail(req.getRequirementDetail());
        requirement.setCityId(req.getCityId());
        requirement.setDistrictId(req.getDistrictId());
        requirement.setGradeId(req.getGradeId());
        requirement.setTeachingMethod(req.getTeachingMethod() != null ? req.getTeachingMethod() : 0);
        requirement.setReqStatus(RequirementStatusEnum.PENDING.getCode());
        // 生成展示编号
        requirement.setDisplayNo("S" + (100000 + requirement.getId() % 900000));
        requirement.setTitle(req.getRequirementDetail() != null && req.getRequirementDetail().length() > 20
                ? req.getRequirementDetail().substring(0, 20) : req.getRequirementDetail());
        tutorRequirementDao.save(requirement);
        return Result.success("提交成功，工作人员将尽快与您联系");
    }
}
