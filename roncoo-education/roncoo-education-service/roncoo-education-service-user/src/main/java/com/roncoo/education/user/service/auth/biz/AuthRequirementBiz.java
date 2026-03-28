package com.roncoo.education.user.service.auth.biz;

import com.roncoo.education.common.base.BaseBiz;
import com.roncoo.education.common.base.ThreadContext;
import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.base.page.PageUtil;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.common.core.enums.RequirementStatusEnum;
import com.roncoo.education.common.core.enums.UserTypeEnum;
import com.roncoo.education.user.dao.TutorRequirementDao;
import com.roncoo.education.user.dao.UsersDao;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorRequirement;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorRequirementExample;
import com.roncoo.education.user.dao.impl.mapper.entity.Users;
import com.roncoo.education.user.service.auth.req.AuthRequirementPageReq;
import com.roncoo.education.user.service.auth.req.AuthRequirementSaveReq;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class AuthRequirementBiz extends BaseBiz {
    @NotNull
    private final TutorRequirementDao tutorRequirementDao;
    @NotNull
    private final UsersDao usersDao;

    /**
     * 我的需求分页列表
     */
    public Result<?> page(AuthRequirementPageReq req) {
        Long userId = ThreadContext.userId();
        Result<String> check = checkStudent(userId);
        if (check != null) {
            return check;
        }

        TutorRequirementExample example = new TutorRequirementExample();
        TutorRequirementExample.Criteria c = example.createCriteria();
        c.andUserIdEqualTo(userId);
        if (req.getReqStatus() != null) {
            c.andReqStatusEqualTo(req.getReqStatus());
        }
        example.setOrderByClause("gmt_create desc");
        Page<TutorRequirement> page = tutorRequirementDao.page(req.getPageCurrent(), req.getPageSize(), example);
        return Result.success(page);
    }

    /**
     * 需求详情
     */
    public Result<?> view(Long id) {
        Long userId = ThreadContext.userId();
        Result<String> check = checkStudent(userId);
        if (check != null) {
            return check;
        }
        TutorRequirement requirement = tutorRequirementDao.getById(id);
        if (requirement == null || !requirement.getUserId().equals(userId)) {
            return Result.error("需求不存在或无权查看");
        }
        return Result.success(requirement);
    }

    /**
     * 保存/修改需求
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<String> save(AuthRequirementSaveReq req) {
        Long userId = ThreadContext.userId();
        Result<String> check = checkStudent(userId);
        if (check != null) {
            return check;
        }

        if (req.getId() != null) {
            // 修改
            TutorRequirement existing = tutorRequirementDao.getById(req.getId());
            if (existing == null || !existing.getUserId().equals(userId)) {
                return Result.error("需求不存在或无权操作");
            }
            if (RequirementStatusEnum.PENDING.getCode().equals(existing.getReqStatus())) {
                return Result.error("审核中不允许修改，请等待审核结果");
            }
            if (RequirementStatusEnum.PUBLISHED.getCode().equals(existing.getReqStatus())) {
                return Result.error("已发布的需求不允许直接修改");
            }

            TutorRequirement update = new TutorRequirement();
            update.setId(existing.getId());
            copyReqToEntity(req, update);
            // 被驳回后修改，回到草稿
            if (RequirementStatusEnum.REJECTED.getCode().equals(existing.getReqStatus())) {
                update.setReqStatus(RequirementStatusEnum.DRAFT.getCode());
            }
            tutorRequirementDao.updateById(update);
            return Result.success("保存成功");
        } else {
            // 新建
            TutorRequirement record = new TutorRequirement();
            record.setUserId(userId);
            record.setReqStatus(RequirementStatusEnum.DRAFT.getCode());
            copyReqToEntity(req, record);
            tutorRequirementDao.save(record);
            return Result.success("创建成功");
        }
    }

    /**
     * 提交审核
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<String> submitAudit(Long id) {
        Long userId = ThreadContext.userId();
        Result<String> check = checkStudent(userId);
        if (check != null) {
            return check;
        }

        TutorRequirement requirement = tutorRequirementDao.getById(id);
        if (requirement == null || !requirement.getUserId().equals(userId)) {
            return Result.error("需求不存在或无权操作");
        }
        if (!RequirementStatusEnum.DRAFT.getCode().equals(requirement.getReqStatus())
                && !RequirementStatusEnum.REJECTED.getCode().equals(requirement.getReqStatus())) {
            return Result.error("当前状态不允许提交审核");
        }

        // 校验必填字段
        if (!StringUtils.hasText(requirement.getTitle())) {
            return Result.error("请先填写需求标题");
        }
        if (!StringUtils.hasText(requirement.getSubjectIds())) {
            return Result.error("请先选择辅导科目");
        }
        if (!StringUtils.hasText(requirement.getContactName())) {
            return Result.error("请先填写联系人姓名");
        }
        if (!StringUtils.hasText(requirement.getContactMobile())) {
            return Result.error("请先填写联系电话");
        }

        TutorRequirement update = new TutorRequirement();
        update.setId(id);
        update.setReqStatus(RequirementStatusEnum.PENDING.getCode());
        update.setAuditRemark(null);
        tutorRequirementDao.updateById(update);

        return Result.success("提交成功，请等待审核");
    }

    /**
     * 删除需求（仅 DRAFT / REJECTED 可删）
     */
    public Result<String> delete(Long id) {
        Long userId = ThreadContext.userId();
        Result<String> check = checkStudent(userId);
        if (check != null) {
            return check;
        }

        TutorRequirement requirement = tutorRequirementDao.getById(id);
        if (requirement == null || !requirement.getUserId().equals(userId)) {
            return Result.error("需求不存在或无权操作");
        }
        if (!RequirementStatusEnum.DRAFT.getCode().equals(requirement.getReqStatus())
                && !RequirementStatusEnum.REJECTED.getCode().equals(requirement.getReqStatus())) {
            return Result.error("当前状态不允许删除");
        }
        tutorRequirementDao.deleteById(id);
        return Result.success("删除成功");
    }

    /**
     * 关闭需求
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<String> close(Long id) {
        Long userId = ThreadContext.userId();
        Result<String> check = checkStudent(userId);
        if (check != null) {
            return check;
        }

        TutorRequirement requirement = tutorRequirementDao.getById(id);
        if (requirement == null || !requirement.getUserId().equals(userId)) {
            return Result.error("需求不存在或无权操作");
        }
        if (RequirementStatusEnum.CLOSED.getCode().equals(requirement.getReqStatus())) {
            return Result.error("需求已关闭");
        }

        TutorRequirement update = new TutorRequirement();
        update.setId(id);
        update.setReqStatus(RequirementStatusEnum.CLOSED.getCode());
        tutorRequirementDao.updateById(update);
        return Result.success("需求已关闭");
    }

    private void copyReqToEntity(AuthRequirementSaveReq req, TutorRequirement entity) {
        entity.setTitle(req.getTitle());
        entity.setSubjectIds(req.getSubjectIds());
        entity.setGradeId(req.getGradeId());
        entity.setProvinceId(req.getProvinceId());
        entity.setCityId(req.getCityId());
        entity.setDistrictId(req.getDistrictId());
        entity.setAddress(req.getAddress());
        entity.setTutorGender(req.getTutorGender());
        entity.setSchedule(req.getSchedule());
        entity.setBudgetMin(req.getBudgetMin());
        entity.setBudgetMax(req.getBudgetMax());
        entity.setRequirementDetail(req.getRequirementDetail());
        entity.setContactName(req.getContactName());
        entity.setContactMobile(req.getContactMobile());
    }

    private Result<String> checkStudent(Long userId) {
        Users user = usersDao.getById(userId);
        if (user == null) {
            return Result.error("用户不存在");
        }
        if (!UserTypeEnum.STUDENT.getCode().equals(user.getUserType())) {
            return Result.error("非学员用户无法操作需求");
        }
        return null;
    }
}
