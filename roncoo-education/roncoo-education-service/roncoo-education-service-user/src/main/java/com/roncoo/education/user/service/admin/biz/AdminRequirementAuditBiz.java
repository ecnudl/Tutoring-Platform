package com.roncoo.education.user.service.admin.biz;

import com.roncoo.education.common.base.BaseBiz;
import com.roncoo.education.common.base.ThreadContext;
import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.base.page.PageUtil;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.common.core.enums.RequirementStatusEnum;
import com.roncoo.education.user.dao.TutorRequirementAuditDao;
import com.roncoo.education.user.dao.TutorRequirementDao;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorRequirement;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorRequirementAudit;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorRequirementExample;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class AdminRequirementAuditBiz extends BaseBiz {
    @NotNull
    private final TutorRequirementDao tutorRequirementDao;
    @NotNull
    private final TutorRequirementAuditDao tutorRequirementAuditDao;

    /**
     * 需求审核分页列表
     */
    public Result<?> page(Map<String, Object> req) {
        int pageCurrent = req.get("pageCurrent") != null ? Integer.parseInt(req.get("pageCurrent").toString()) : 1;
        int pageSize = req.get("pageSize") != null ? Integer.parseInt(req.get("pageSize").toString()) : 20;
        Integer reqStatus = req.get("reqStatus") != null ? Integer.parseInt(req.get("reqStatus").toString()) : null;

        TutorRequirementExample example = new TutorRequirementExample();
        TutorRequirementExample.Criteria c = example.createCriteria();
        if (reqStatus != null) {
            c.andReqStatusEqualTo(reqStatus);
        }
        if (req.get("keyword") != null && StringUtils.hasText(req.get("keyword").toString())) {
            c.andTitleLike(PageUtil.like(req.get("keyword").toString()));
        }
        example.setOrderByClause("gmt_create desc");
        Page<TutorRequirement> page = tutorRequirementDao.page(pageCurrent, pageSize, example);
        return Result.success(page);
    }

    /**
     * 需求详情
     */
    public Result<?> view(Long id) {
        TutorRequirement requirement = tutorRequirementDao.getById(id);
        if (requirement == null) {
            return Result.error("需求不存在");
        }
        return Result.success(requirement);
    }

    /**
     * 审核通过 -> PUBLISHED
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<String> approve(Map<String, Object> req) {
        Long id = Long.parseLong(req.get("id").toString());
        String remark = req.get("auditRemark") != null ? req.get("auditRemark").toString() : "";

        TutorRequirement requirement = tutorRequirementDao.getById(id);
        if (requirement == null) {
            return Result.error("需求不存在");
        }
        if (!RequirementStatusEnum.PENDING.getCode().equals(requirement.getReqStatus())) {
            return Result.error("当前状态不允许审核，仅待审核状态可操作");
        }

        TutorRequirement update = new TutorRequirement();
        update.setId(id);
        update.setReqStatus(RequirementStatusEnum.PUBLISHED.getCode());
        update.setAuditRemark(remark);
        tutorRequirementDao.updateById(update);

        saveAuditRecord(id, 1, remark);
        return Result.success("审核通过，需求已发布");
    }

    /**
     * 审核驳回 -> REJECTED
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<String> reject(Map<String, Object> req) {
        Long id = Long.parseLong(req.get("id").toString());
        String remark = req.get("auditRemark") != null ? req.get("auditRemark").toString() : "";

        if (!StringUtils.hasText(remark)) {
            return Result.error("驳回原因不能为空");
        }

        TutorRequirement requirement = tutorRequirementDao.getById(id);
        if (requirement == null) {
            return Result.error("需求不存在");
        }
        if (!RequirementStatusEnum.PENDING.getCode().equals(requirement.getReqStatus())) {
            return Result.error("当前状态不允许审核，仅待审核状态可操作");
        }

        TutorRequirement update = new TutorRequirement();
        update.setId(id);
        update.setReqStatus(RequirementStatusEnum.REJECTED.getCode());
        update.setAuditRemark(remark);
        tutorRequirementDao.updateById(update);

        saveAuditRecord(id, 2, remark);
        return Result.success("已驳回");
    }

    private void saveAuditRecord(Long requirementId, Integer auditAction, String remark) {
        TutorRequirementAudit record = new TutorRequirementAudit();
        record.setRequirementId(requirementId);
        record.setAuditAction(auditAction);
        record.setAuditRemark(remark);
        try {
            record.setAuditorId(ThreadContext.userId());
        } catch (Exception e) {
            record.setAuditorId(0L);
        }
        tutorRequirementAuditDao.save(record);
    }
}
