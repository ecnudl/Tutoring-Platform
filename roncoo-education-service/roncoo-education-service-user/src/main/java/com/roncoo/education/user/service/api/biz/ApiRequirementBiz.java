package com.roncoo.education.user.service.api.biz;

import com.roncoo.education.common.base.BaseBiz;
import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.base.page.PageUtil;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.common.core.enums.RequirementStatusEnum;
import com.roncoo.education.common.tools.BeanUtil;
import com.roncoo.education.user.dao.TutorRequirementDao;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorRequirement;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorRequirementExample;
import com.roncoo.education.user.service.api.req.RequirementSearchReq;
import com.roncoo.education.user.service.api.resp.RequirementSearchResp;
import com.roncoo.education.user.service.api.resp.RequirementViewResp;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class ApiRequirementBiz extends BaseBiz {
    @NotNull
    private final TutorRequirementDao tutorRequirementDao;

    public Result<Page<RequirementSearchResp>> search(RequirementSearchReq req) {
        TutorRequirementExample example = new TutorRequirementExample();
        TutorRequirementExample.Criteria c = example.createCriteria();
        c.andReqStatusEqualTo(RequirementStatusEnum.PUBLISHED.getCode());
        if (StringUtils.hasText(req.getKeyword())) { c.andTitleLike(PageUtil.like(req.getKeyword())); }
        example.setOrderByClause("id desc");
        Page<TutorRequirement> page = tutorRequirementDao.page(req.getPageCurrent(), req.getPageSize(), example);
        return Result.success(PageUtil.transform(page, RequirementSearchResp.class));
    }

    public Result<RequirementViewResp> view(Long id) {
        TutorRequirement requirement = tutorRequirementDao.getById(id);
        if (requirement == null) {
            return Result.error("需求不存在");
        }
        if (!RequirementStatusEnum.PUBLISHED.getCode().equals(requirement.getReqStatus())) {
            return Result.error("该需求暂不可查看");
        }
        TutorRequirement update = new TutorRequirement();
        update.setId(id);
        update.setViewCount(requirement.getViewCount() == null ? 1 : requirement.getViewCount() + 1);
        tutorRequirementDao.updateById(update);
        return Result.success(BeanUtil.copyProperties(requirement, RequirementViewResp.class));
    }
}
