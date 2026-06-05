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
            // tutor_profile.subjects 已规范化为 CSV of new IDs (如 "3002,3003"); 前端传科目名.
            // 步骤: dict_subject 翻译 (name → id) + tutor_subject 链接表 IN 查询 (含"全科"通配).
            // 通配规则: 教员选了"全科"(3000) → 学员搜任何科目都命中 → 加 3000 作为 OR 条件.
            // 反向: 学员搜"全科" → 只匹配教员明确选了"全科"的, 不再展开.
            Long subjectId = null;
            for (DictSubject ds : dictSubjectDao.listAll()) {
                if (req.getSubject().equals(ds.getSubjectName())) { subjectId = ds.getId(); break; }
            }
            if (subjectId == null) {
                return Result.success(emptyTutorSearchPage(req));
            }
            final long ALL_SUBJECTS_ID = 3000L;
            java.util.List<Long> filterIds = new java.util.ArrayList<>();
            filterIds.add(subjectId);
            if (subjectId.longValue() != ALL_SUBJECTS_ID) {
                filterIds.add(ALL_SUBJECTS_ID);
            }
            java.util.List<TutorSubject> tsList = tutorSubjectDao.listBySubjectIds(filterIds);
            java.util.List<Long> tutorIds = tsList.stream()
                    .map(TutorSubject::getTutorId)
                    .filter(java.util.Objects::nonNull)
                    .distinct()
                    .collect(java.util.stream.Collectors.toList());
            if (tutorIds.isEmpty()) {
                return Result.success(emptyTutorSearchPage(req));
            }
            c.andIdIn(tutorIds);
        }
        if (StringUtils.hasText(req.getUniversity())) {
            // 模糊匹配, 兼容教员自由文本填写的院校 ("复旦"/"复旦大学"/"复旦大学(邯郸校区)" 都命中"复旦")
            // 院校"不限"时前端不传该字段 → 不加条件, 自由文本(如"湖南大学")的教员自然落入"不限"结果
            c.andUniversityLike(PageUtil.like(req.getUniversity()));
        }
        // 关键词不在此处加到 SQL — 改为多字段(姓名/编号/科目/院校/工作单位/身份)在 Java 端模糊匹配, 见下方
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

        // 无关键词: 原 SQL 分页
        if (!StringUtils.hasText(req.getKeyword())) {
            Page<TutorProfile> page = tutorProfileDao.page(req.getPageCurrent(), req.getPageSize(), example);
            Page<TutorSearchResp> outPage = PageUtil.transform(page, TutorSearchResp.class);
            // 翻译 subjects: entity 存的是 JSON id 数组(如 "[2013, 2014]") → resp 改写为 name CSV ("小学全科,数学")
            translateSubjectsForList(page.getList(), outPage.getList());
            enrichTeachingAreas(outPage.getList()); // 授课区域 CSV
            return Result.success(outPage);
        }

        // 有关键词: 取出(结构化筛选后的)全部, 翻译科目, 在 Java 端对 姓名/编号/科目/院校/工作单位/身份 做模糊匹配, 再切片分页
        // (教员量级小; 结构化筛选仍为 AND, 关键词跨字段为 OR; 与后台教员列表同套做法)
        int pc = req.getPageCurrent() <= 0 ? 1 : req.getPageCurrent();
        int ps = req.getPageSize() <= 0 ? 15 : req.getPageSize();
        // 取结构化筛选后的全部 (DAO 单页上限 PageUtil.MAX_PAGE_SIZE=1000, 循环取直到取完, 教员量大也不漏)
        java.util.List<TutorProfile> allRows = new java.util.ArrayList<>();
        for (int p = 1; ; p++) {
            java.util.List<TutorProfile> batch = tutorProfileDao.page(p, PageUtil.MAX_PAGE_SIZE, example).getList();
            if (batch == null || batch.isEmpty()) {
                break;
            }
            allRows.addAll(batch);
            if (batch.size() < PageUtil.MAX_PAGE_SIZE) {
                break;
            }
        }
        java.util.List<TutorSearchResp> allResp = BeanUtil.copyProperties(allRows, TutorSearchResp.class);
        translateSubjectsForList(allRows, allResp); // 填科目名(用于匹配 + 展示)
        final String kw = req.getKeyword().trim().toLowerCase();
        // 教师编号常带 T 前缀, 去前缀后仍非空时也参与匹配 (输 374657 / T374657 都行)
        final String kwNoT = (kw.startsWith("t") && kw.length() > 1) ? kw.substring(1) : kw;
        java.util.List<TutorSearchResp> matched = new java.util.ArrayList<>();
        for (TutorSearchResp r : allResp) {
            if (likeField(r.getRealName(), kw)
                    || likeField(r.getUniversity(), kw)
                    || likeField(r.getWorkUnit(), kw)
                    || likeField(r.getIdentityDetail(), kw)
                    || likeField(r.getSubjects(), kw)
                    || likeDisplayNo(r.getDisplayNo(), kw, kwNoT)) {
                matched.add(r);
            }
        }
        int total = matched.size();
        int totalPage = (int) Math.ceil(total / (double) ps);
        int from = Math.max(0, (pc - 1) * ps);
        int to = Math.min(from + ps, total);
        java.util.List<TutorSearchResp> slice = from < to ? matched.subList(from, to) : new java.util.ArrayList<>();
        enrichTeachingAreas(slice); // 仅本页教员查授课区域
        return Result.success(new Page<>(total, totalPage, pc, ps, slice));
    }

    /** 批量补充授课区域名 CSV (按本页教员逐个查 tutor_teaching_area, 区域名做请求内缓存) */
    private void enrichTeachingAreas(java.util.List<TutorSearchResp> resps) {
        if (resps == null || resps.isEmpty()) {
            return;
        }
        java.util.Map<Long, String> distCache = new java.util.HashMap<>();
        for (TutorSearchResp r : resps) {
            if (r.getId() == null) {
                continue;
            }
            java.util.List<TutorTeachingArea> areas = tutorTeachingAreaDao.listByTutorId(r.getId());
            if (areas == null || areas.isEmpty()) {
                continue;
            }
            java.util.List<String> names = new java.util.ArrayList<>();
            for (TutorTeachingArea a : areas) {
                Long did = a.getDistrictId();
                if (did == null) {
                    continue;
                }
                String name = distCache.computeIfAbsent(did, k -> {
                    DictDistrict d = dictDistrictDao.getById(k);
                    return d != null ? d.getDistrictName() : null;
                });
                if (name != null && !names.contains(name)) {
                    names.add(name);
                }
            }
            if (!names.isEmpty()) {
                r.setDistrictNames(String.join(" · ", names));
            }
        }
    }

    /** 字段不区分大小写包含关键词 */
    private static boolean likeField(String field, String kwLower) {
        return field != null && field.toLowerCase().contains(kwLower);
    }

    /** 教师编号匹配: 原串或去掉 T 前缀的串命中即可 */
    private static boolean likeDisplayNo(String displayNo, String kw, String kwNoT) {
        if (displayNo == null) {
            return false;
        }
        String d = displayNo.toLowerCase();
        return d.contains(kw) || d.contains(kwNoT);
    }

    /** 提前返回的空 page (subject 翻译失败 / 子表 0 行的快路径) */
    private Page<TutorSearchResp> emptyTutorSearchPage(TutorSearchReq req) {
        Page<TutorSearchResp> empty = new Page<>();
        empty.setList(java.util.Collections.emptyList());
        empty.setPageCurrent(req.getPageCurrent() <= 0 ? 1 : req.getPageCurrent());
        empty.setPageSize(req.getPageSize() <= 0 ? 20 : req.getPageSize());
        empty.setTotalCount(0);
        empty.setTotalPage(0);
        return empty;
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
                    item.setTitle(req.getTitle());
                    item.setDisplayNo(req.getDisplayNo());
                    item.setGrade(req.getGradeName());
                    item.setSubjects(req.getSubjectIds());
                    item.setDetail(req.getRequirementDetail());
                    item.setLocation(buildLocation(req.getCityId(), req.getDistrictId(), req.getAddress()));
                    item.setDistrictNames(req.getDistrictNames());
                    item.setOtherRequirements(req.getOtherRequirements());
                    item.setTutorGender(req.getTutorGender());
                    item.setTutorTypePref(req.getTutorTypePref());
                    item.setTeachingMethod(req.getTeachingMethod());
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
