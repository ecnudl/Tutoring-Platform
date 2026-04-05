package com.roncoo.education.user.service.api.biz;

import com.roncoo.education.common.base.BaseBiz;
import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.base.page.PageUtil;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.common.core.enums.TutorAuditStatusEnum;
import com.roncoo.education.common.tools.BeanUtil;
import com.roncoo.education.user.dao.TutorCertificationDao;
import com.roncoo.education.user.dao.TutorProfileDao;
import com.roncoo.education.user.dao.TutorSubjectDao;
import com.roncoo.education.user.dao.TutorTeachingAreaDao;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorCertification;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorProfile;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorProfileExample;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorSubject;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorTeachingArea;
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

    /**
     * 教员搜索（分页+多条件）
     */
    public Result<Page<TutorSearchResp>> search(TutorSearchReq req) {
        TutorProfileExample example = new TutorProfileExample();
        TutorProfileExample.Criteria c = example.createCriteria();
        // 只展示审核通过的教员
        c.andAuditStatusEqualTo(TutorAuditStatusEnum.APPROVED.getCode());
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
        return Result.success(PageUtil.transform(page, TutorSearchResp.class));
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
        // 查询科目
        List<TutorSubject> subjects = tutorSubjectDao.listByTutorId(profile.getId());
        resp.setSubjects(BeanUtil.copyProperties(subjects, TutorDetailResp.SubjectItem.class));
        // 查询授课区域
        List<TutorTeachingArea> areas = tutorTeachingAreaDao.listByTutorId(profile.getId());
        resp.setTeachingAreas(BeanUtil.copyProperties(areas, TutorDetailResp.TeachingAreaItem.class));
        // 查询资质证书
        List<TutorCertification> certs = tutorCertificationDao.listByTutorId(profile.getId());
        resp.setCertifications(BeanUtil.copyProperties(certs, TutorDetailResp.CertificationItem.class));
        return Result.success(resp);
    }

    /**
     * 首页推荐教员列表
     */
    @Cacheable
    public Result<List<TutorListResp>> recommend(Long cityId, Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 18;
        }
        TutorProfileExample example = new TutorProfileExample();
        TutorProfileExample.Criteria c = example.createCriteria();
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
