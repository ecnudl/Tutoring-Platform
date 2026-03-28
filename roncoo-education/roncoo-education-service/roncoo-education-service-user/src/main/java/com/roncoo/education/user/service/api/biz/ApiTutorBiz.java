package com.roncoo.education.user.service.api.biz;

import com.roncoo.education.common.base.BaseBiz;
import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.base.page.PageUtil;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.common.core.enums.TutorAuditStatusEnum;
import com.roncoo.education.common.tools.BeanUtil;
import com.roncoo.education.user.dao.TutorProfileDao;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorProfile;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorProfileExample;
import com.roncoo.education.user.service.api.req.TutorSearchReq;
import com.roncoo.education.user.service.api.resp.TutorSearchResp;
import com.roncoo.education.user.service.api.resp.TutorViewResp;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class ApiTutorBiz extends BaseBiz {
    @NotNull
    private final TutorProfileDao tutorProfileDao;

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
        if (StringUtils.hasText(req.getKeyword())) {
            c.andRealNameLike(PageUtil.like(req.getKeyword()));
        }
        example.setOrderByClause("sort asc, id desc");
        Page<TutorProfile> page = tutorProfileDao.page(req.getPageCurrent(), req.getPageSize(), example);
        return Result.success(PageUtil.transform(page, TutorSearchResp.class));
    }

    public Result<?> view(Long id) {
        TutorProfile profile = tutorProfileDao.getById(id);
        if (profile == null) {
            return Result.error("教员不存在");
        }
        // 公开详情只允许查看审核通过的教员
        if (!TutorAuditStatusEnum.APPROVED.getCode().equals(profile.getAuditStatus())) {
            return Result.error("该教员暂未通过审核");
        }
        // 增加浏览次数
        TutorProfile update = new TutorProfile();
        update.setId(id);
        update.setViewCount(profile.getViewCount() == null ? 1 : profile.getViewCount() + 1);
        tutorProfileDao.updateById(update);
        // 直接返回 entity，确保 userId 等字段不丢失
        return Result.success(profile);
    }
}
