package com.roncoo.education.user.service.api.biz;

import com.roncoo.education.common.base.BaseBiz;
import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.base.page.PageUtil;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.common.core.enums.TutorAuditStatusEnum;
import com.roncoo.education.common.tools.BeanUtil;
import com.roncoo.education.common.core.enums.ReservationStatusEnum;
import com.roncoo.education.user.dao.DictCityDao;
import com.roncoo.education.user.dao.DictDistrictDao;
import com.roncoo.education.user.dao.DictSubjectDao;
import com.roncoo.education.user.dao.TutorCertificationDao;
import com.roncoo.education.user.dao.TutorProfileDao;
import com.roncoo.education.user.dao.TutorRequirementDao;
import com.roncoo.education.user.dao.TutorReservationDao;
import com.roncoo.education.user.dao.TutorSubjectDao;
import com.roncoo.education.user.dao.TutorTeachingAreaDao;
import com.roncoo.education.user.dao.impl.mapper.entity.DictCity;
import com.roncoo.education.user.dao.impl.mapper.entity.DictDistrict;
import com.roncoo.education.user.dao.impl.mapper.entity.DictSubject;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorCertification;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorProfile;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorProfileExample;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorRequirement;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorReservation;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorReservationExample;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorSubject;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorTeachingArea;
import java.time.format.DateTimeFormatter;
import com.roncoo.education.user.service.api.req.TutorSearchReq;
import com.roncoo.education.user.service.api.resp.TutorDetailResp;
import com.roncoo.education.user.service.api.resp.TutorListResp;
import com.roncoo.education.user.service.api.resp.TutorSearchResp;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

/**
 * API-教员库
 *
 * @author fengyw
 */
@Component
@RequiredArgsConstructor
@CacheConfig(cacheNames = {"user"})
public class ApiTutorBiz extends BaseBiz {

    @NotNull
    private final TutorProfileDao tutorProfileDao;

    @NotNull
    private final TutorSubjectDao tutorSubjectDao;

    @NotNull
    private final TutorTeachingAreaDao tutorTeachingAreaDao;

    @NotNull
    private final TutorCertificationDao tutorCertificationDao;

    @NotNull
    private final TutorReservationDao tutorReservationDao;

    @NotNull
    private final TutorRequirementDao tutorRequirementDao;

    @NotNull
    private final DictCityDao dictCityDao;

    @NotNull
    private final DictDistrictDao dictDistrictDao;

    @NotNull
    private final DictSubjectDao dictSubjectDao;

    /**
     * 教员搜索（分页+多条件）
     */
    public Result<Page<TutorSearchResp>> search(TutorSearchReq req) {
        TutorProfileExample example = new TutorProfileExample();
        TutorProfileExample.Criteria c = example.createCriteria();
        // 展示审核通过及已发布的教员（兼容历史数据）, 且后台未禁用 (status_id=1)
        c.andStatusIdEqualTo(1);
        c.andAuditStatusIn(java.util.Arrays.asList(
                TutorAuditStatusEnum.APPROVED.getCode(),
                TutorAuditStatusEnum.PUBLISHED.getCode()));
        if (req.getTutorType() != null) {
            c.andTutorTypeEqualTo(req.getTutorType());
        }
        if (req.getGender() != null && req.getGender() > 0) {
            c.andGenderEqualTo(req.getGender());
        }
        if (req.getProvinceId() != null) {
            c.andProvinceIdEqualTo(req.getProvinceId());
        }
        if (req.getCityId() != null) {
            c.andCityIdEqualTo(req.getCityId());
        }
        if (req.getDistrictId() != null) {
            c.andDistrictIdEqualTo(req.getDistrictId());
        }
        if (req.getDegree() != null) {
            c.andDegreeEqualTo(req.getDegree());
        }
        if (req.getPriceMin() != null) {
            c.andPriceMaxGreaterThan(req.getPriceMin().subtract(java.math.BigDecimal.ONE));
        }
        if (req.getPriceMax() != null) {
            c.andPriceMinLessThan(req.getPriceMax().add(java.math.BigDecimal.ONE));
        }
        if (StringUtils.hasText(req.getSubject())) {
            c.andSubjectsLike(PageUtil.like(req.getSubject()));
        }
        if (StringUtils.hasText(req.getUniversity())) {
            // 模糊匹配, 兼容教员自由文本填写的院校 ("复旦"/"复旦大学"/"复旦大学(邯郸校区)" 都命中"复旦")
            // 院校"不限"时前端不传该字段 → 不加条件, 自由文本(如"湖南大学")的教员自然落入"不限"结果
            c.andUniversityLike(PageUtil.like(req.getUniversity()));
        }
        if (StringUtils.hasText(req.getKeyword())) {
            c.andRealNameLike(PageUtil.like(req.getKeyword()));
        }
        // 动态排序：默认按最近登录时间降序（活跃教员优先），再按权重
        String orderBy = "last_login_time desc, sort asc, id desc";
        if (StringUtils.hasText(req.getSortField())) {
            String field = req.getSortField();
            if ("priceMin".equals(field) || "viewCount".equals(field) || "successCount".equals(field)
                    || "lastLoginTime".equals(field) || "loginCount".equals(field)) {
                String dir = "desc".equalsIgnoreCase(req.getSortOrder()) ? "desc" : "asc";
                orderBy = camelToSnake(field) + " " + dir + ", id desc";
            }
        }
        example.setOrderByClause(orderBy);
        Page<TutorProfile> page = tutorProfileDao.page(req.getPageCurrent(), req.getPageSize(), example);
        Page<TutorSearchResp> outPage = PageUtil.transform(page, TutorSearchResp.class);
        // 翻译 subjects: entity 存的是 JSON id 数组(如 "[2013, 2014]") → resp 改写为 name CSV ("小学全科,数学")
        // 前端卡片直接 t.subjects.split(',') 渲染, 不需要前端改
        translateSubjectsForList(page.getList(), outPage.getList());
        return Result.success(outPage);
    }

    /** 解析 "[1,2,3]" 或 "1,2,3" → List<Long>, 容错 */
    private static java.util.List<Long> parseSubjectIds(String s) {
        if (s == null) return java.util.Collections.emptyList();
        String t = s.trim();
        if (t.isEmpty() || "[]".equals(t)) return java.util.Collections.emptyList();
        if (t.startsWith("[") && t.endsWith("]")) t = t.substring(1, t.length() - 1);
        java.util.List<Long> out = new java.util.ArrayList<>();
        for (String p : t.split(",")) {
            String x = p.trim().replace("\"", "").replace("'", "");
            if (x.isEmpty()) continue;
            try { out.add(Long.parseLong(x)); } catch (NumberFormatException ignore) {}
        }
        return out;
    }

    /** 批量翻译: 一页教员的 subjects 字段 id JSON → name CSV */
    private void translateSubjectsForList(java.util.List<TutorProfile> rows, java.util.List<TutorSearchResp> resps) {
        if (rows == null || resps == null || rows.isEmpty()) return;
        // 收集本页所有用到的 subject_id, 去重
        java.util.Set<Long> allIds = new java.util.HashSet<>();
        java.util.List<java.util.List<Long>> perRow = new java.util.ArrayList<>(rows.size());
        for (TutorProfile p : rows) {
            java.util.List<Long> ids = parseSubjectIds(p.getSubjects());
            perRow.add(ids);
            allIds.addAll(ids);
        }
        // 一次性查 dict_subject (本页教员涉及到的 id, 通常去重后 < 20 个)
        java.util.Map<Long, String> idToName = new java.util.HashMap<>();
        for (Long sid : allIds) {
            DictSubject ds = dictSubjectDao.getById(sid);
            if (ds != null && ds.getSubjectName() != null) idToName.put(sid, ds.getSubjectName());
        }
        // 改写每行 resp.subjects
        for (int i = 0; i < resps.size() && i < perRow.size(); i++) {
            java.util.List<String> names = new java.util.ArrayList<>();
            for (Long sid : perRow.get(i)) {
                String n = idToName.get(sid);
                if (n != null) names.add(n);
            }
            resps.get(i).setSubjects(String.join(",", names));
        }
    }

    /**
     * 根据展示编号查看教员详情
     */
    public Result<TutorDetailResp> viewByDisplayNo(String displayNo) {
        TutorProfile profile = tutorProfileDao.getByDisplayNo(displayNo);
        if (profile == null) {
            return Result.error("教员不存在");
        }
        if (!TutorAuditStatusEnum.PUBLISHED.getCode().equals(profile.getAuditStatus())
                && !TutorAuditStatusEnum.APPROVED.getCode().equals(profile.getAuditStatus())) {
            return Result.error("该教员暂未通过审核");
        }
        // 增加浏览次数
        TutorProfile update = new TutorProfile();
        update.setId(profile.getId());
        update.setViewCount(profile.getViewCount() + 1);
        tutorProfileDao.updateById(update);

        TutorDetailResp resp = BeanUtil.copyProperties(profile, TutorDetailResp.class);
        // 查询科目 + 翻译科目名 (BeanUtil 不会自动 join dict_subject)
        List<TutorSubject> subjects = tutorSubjectDao.listByTutorId(profile.getId());
        java.util.List<TutorDetailResp.SubjectItem> subjectItems = new java.util.ArrayList<>(subjects.size());
        java.util.List<String> subjectNames = new java.util.ArrayList<>(subjects.size());
        for (TutorSubject s : subjects) {
            TutorDetailResp.SubjectItem item = new TutorDetailResp.SubjectItem();
            item.setId(s.getId());
            item.setSubjectId(s.getSubjectId());
            if (s.getSubjectId() != null) {
                DictSubject ds = dictSubjectDao.getById(s.getSubjectId());
                if (ds != null) item.setSubjectName(ds.getSubjectName());
            }
            if (item.getSubjectName() != null) subjectNames.add(item.getSubjectName());
            subjectItems.add(item);
        }
        resp.setSubjects(subjectItems);
        resp.setSubjectNames(subjectNames);
        // 翻译 cityId/districtId → cityName/districtName (entity 只存 ID, resp 暴露名字给前端展示)
        if (profile.getCityId() != null) {
            DictCity city = dictCityDao.getById(profile.getCityId());
            if (city != null) resp.setCityName(city.getCityName());
        }
        if (profile.getDistrictId() != null) {
            DictDistrict district = dictDistrictDao.getById(profile.getDistrictId());
            if (district != null) resp.setDistrictName(district.getDistrictName());
        }
        // 查询授课区域 + 翻译区域名
        List<TutorTeachingArea> areas = tutorTeachingAreaDao.listByTutorId(profile.getId());
        java.util.List<TutorDetailResp.TeachingAreaItem> areaItems = new java.util.ArrayList<>(areas.size());
        for (TutorTeachingArea a : areas) {
            TutorDetailResp.TeachingAreaItem item = new TutorDetailResp.TeachingAreaItem();
            item.setId(a.getId());
            item.setCityId(a.getCityId());
            item.setDistrictId(a.getDistrictId());
            if (a.getDistrictId() != null) {
                DictDistrict dd = dictDistrictDao.getById(a.getDistrictId());
                if (dd != null) item.setDistrictName(dd.getDistrictName());
            }
            areaItems.add(item);
        }
        resp.setTeachingAreas(areaItems);
        // 查询资质证书
        List<TutorCertification> certs = tutorCertificationDao.listByTutorId(profile.getId());
        resp.setCertifications(BeanUtil.copyProperties(certs, TutorDetailResp.CertificationItem.class));

        // 成功记录板块: 教员可在 /center/tutor-profile 关闭, 默认开
        Integer showFlag = profile.getShowSuccessRecord() == null ? 1 : profile.getShowSuccessRecord();
        resp.setShowSuccessRecord(showFlag);
        if (showFlag != null && showFlag == 1 && profile.getUserId() != null) {
            resp.setSuccessRecords(loadSuccessRecords(profile.getUserId(), 50));
        } else {
            resp.setSuccessRecords(java.util.Collections.emptyList());
        }
        return Result.success(resp);
    }

    /**
     * 拉取该教员所有 res_status=CONFIRMED|COMPLETED 的撮合记录, 关联 requirement 拿脱敏字段.
     * 字段: grade / subjects / location (区+地点) / detail / date.
     */
    private List<TutorDetailResp.SuccessRecordItem> loadSuccessRecords(Long tutorUserId, int limit) {
        TutorReservationExample ex = new TutorReservationExample();
        ex.createCriteria().andTutorUserIdEqualTo(tutorUserId)
                .andResStatusIn(java.util.Arrays.asList(
                        ReservationStatusEnum.CONFIRMED.getCode(),
                        ReservationStatusEnum.COMPLETED.getCode()));
        ex.setOrderByClause("matched_at desc, gmt_create desc");
        // page 接口拿前 N 条; pageSize=limit, pageCurrent=1
        Page<TutorReservation> page = tutorReservationDao.page(1, limit, ex);
        List<TutorReservation> rows = page == null ? java.util.Collections.emptyList() : page.getList();
        List<TutorDetailResp.SuccessRecordItem> out = new java.util.ArrayList<>(rows.size());
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (TutorReservation r : rows) {
            TutorDetailResp.SuccessRecordItem item = new TutorDetailResp.SuccessRecordItem();
            if (r.getRequirementId() != null) {
                TutorRequirement req = tutorRequirementDao.getById(r.getRequirementId());
                if (req != null) {
                    item.setGrade(req.getGradeName());
                    item.setSubjects(req.getSubjectIds());
                    item.setDetail(req.getRequirementDetail());
                    item.setLocation(buildLocation(req.getCityId(), req.getDistrictId(), req.getAddress()));
                }
            }
            java.time.LocalDateTime dt = r.getMatchedAt() != null ? r.getMatchedAt() : r.getGmtCreate();
            if (dt != null) item.setDate(dt.format(df));
            out.add(item);
        }
        return out;
    }

    private String buildLocation(Long cityId, Long districtId, String address) {
        StringBuilder sb = new StringBuilder();
        if (cityId != null) {
            DictCity c = dictCityDao.getById(cityId);
            if (c != null && c.getCityName() != null) sb.append(c.getCityName());
        }
        if (districtId != null) {
            DictDistrict d = dictDistrictDao.getById(districtId);
            if (d != null && d.getDistrictName() != null) sb.append(d.getDistrictName());
        }
        if (address != null && !address.isEmpty()) {
            if (sb.length() > 0) sb.append(' ');
            sb.append(address);
        }
        return sb.toString();
    }

    /**
     * 首页推荐教员列表
     */
    public Result<List<TutorListResp>> recommend(Long cityId, Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 18;
        }
        TutorProfileExample example = new TutorProfileExample();
        TutorProfileExample.Criteria c = example.createCriteria();
        c.andStatusIdEqualTo(1);
        c.andAuditStatusEqualTo(TutorAuditStatusEnum.PUBLISHED.getCode());
        if (cityId != null) {
            c.andCityIdEqualTo(cityId);
        }
        example.setOrderByClause("is_star desc, last_login_time desc, sort desc, id desc");
        Page<TutorProfile> page = tutorProfileDao.page(1, limit, example);
        return Result.success(BeanUtil.copyProperties(page.getList(), TutorListResp.class));
    }

    private static String camelToSnake(String camel) {
        return camel.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }
}
