package com.roncoo.education.user.service.api.biz;

import com.roncoo.education.common.base.BaseBiz;
import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.base.page.PageUtil;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.common.core.enums.RequirementStatusEnum;
import com.roncoo.education.common.tools.BeanUtil;
import com.roncoo.education.common.tools.IdWorker;
import com.roncoo.education.user.dao.TutorRequirementDao;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorRequirement;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorRequirementExample;
import com.roncoo.education.user.service.api.req.RequirementQuickSubmitReq;
import com.roncoo.education.user.service.api.req.RequirementSearchReq;
import com.roncoo.education.user.service.api.resp.RequirementDetailResp;
import com.roncoo.education.user.service.api.resp.RequirementListResp;
import com.roncoo.education.user.service.api.resp.RequirementSearchResp;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

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

    /**
     * 需求搜索（分页+多条件）
     * 排序: PUBLISHED 在前, MATCHED 在后
     * 过滤: MATCHED 状态停留 24h 后从列表中消失 (Java post-filter)
     */
    public Result<Page<RequirementSearchResp>> search(RequirementSearchReq req) {
        TutorRequirementExample example = new TutorRequirementExample();
        TutorRequirementExample.Criteria c = example.createCriteria();
        c.andReqStatusIn(java.util.Arrays.asList(
                RequirementStatusEnum.PUBLISHED.getCode(),
                RequirementStatusEnum.MATCHED.getCode()));
        if (req.getCityId() != null) c.andCityIdEqualTo(req.getCityId());
        if (req.getGradeId() != null) c.andGradeIdEqualTo(req.getGradeId());
        if (StringUtils.hasText(req.getKeyword())) c.andTitleLike(PageUtil.like(req.getKeyword()));

        // 已匹配的沉底, 同状态内按 id desc
        example.setOrderByClause(
                "CASE WHEN req_status = " + RequirementStatusEnum.MATCHED.getCode()
                        + " THEN 1 ELSE 0 END ASC, id DESC");
        Page<TutorRequirement> page = tutorRequirementDao.page(req.getPageCurrent(), req.getPageSize(), example);

        // 过滤: MATCHED 且 matched_at < 24h 前的去掉
        applyMatchedCutoff(page);
        return Result.success(PageUtil.transform(page, RequirementSearchResp.class));
    }

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
                        + " THEN 1 ELSE 0 END ASC, id DESC");
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
                        + " THEN 1 ELSE 0 END ASC, id DESC");
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
        requirement.setDisplayNo("A" + (100000 + requirement.getId() % 900000));
        requirement.setTitle(req.getRequirementDetail() != null && req.getRequirementDetail().length() > 20
                ? req.getRequirementDetail().substring(0, 20) : req.getRequirementDetail());
        tutorRequirementDao.save(requirement);
        return Result.success("提交成功，工作人员将尽快与您联系");
    }
}
