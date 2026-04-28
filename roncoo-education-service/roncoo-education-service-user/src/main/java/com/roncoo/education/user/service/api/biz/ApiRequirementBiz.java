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
     */
    public Result<Page<RequirementSearchResp>> search(RequirementSearchReq req) {
        TutorRequirementExample example = new TutorRequirementExample();
        TutorRequirementExample.Criteria c = example.createCriteria();
        c.andReqStatusIn(java.util.Arrays.asList(
                RequirementStatusEnum.PUBLISHED.getCode(),
                RequirementStatusEnum.MATCHED.getCode()));
        if (req.getCityId() != null) {
            c.andCityIdEqualTo(req.getCityId());
        }
        if (req.getSubjectId() != null) {
            // TODO 按科目ID筛选需求，需要关联查询或JSON字段匹配
        }
        if (req.getGradeId() != null) {
            c.andGradeIdEqualTo(req.getGradeId());
        }
        if (StringUtils.hasText(req.getKeyword())) {
            c.andTitleLike(PageUtil.like(req.getKeyword()));
        }
        example.setOrderByClause("id desc");
        Page<TutorRequirement> page = tutorRequirementDao.page(req.getPageCurrent(), req.getPageSize(), example);
        return Result.success(PageUtil.transform(page, RequirementSearchResp.class));
    }

    /**
     * 根据展示编号查看需求详情
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
        update.setViewCount(requirement.getViewCount() + 1);
        tutorRequirementDao.updateById(update);

        RequirementDetailResp resp = BeanUtil.copyProperties(requirement, RequirementDetailResp.class);
        // 脱敏联系方式
        if (resp.getContactMobile() != null && resp.getContactMobile().length() > 7) {
            resp.setContactMobile(resp.getContactMobile().substring(0, 3) + "****" + resp.getContactMobile().substring(7));
        }
        return Result.success(resp);
    }

    /**
     * 首页最新需求列表
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
        if (cityId != null) {
            c.andCityIdEqualTo(cityId);
        }
        example.setOrderByClause("id desc");
        Page<TutorRequirement> page = tutorRequirementDao.page(1, limit, example);
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
