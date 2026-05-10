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
    @NotNull
    private final com.roncoo.education.user.dao.TutorApplicationDao tutorApplicationDao;

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

        // 实时计算每条需求的活跃申请数 (排除 REJECTED, 让 admin 看到"待处理")
        if (page.getList() != null) {
            for (TutorRequirement r : page.getList()) {
                java.util.List<com.roncoo.education.user.dao.impl.mapper.entity.TutorApplication> apps =
                        tutorApplicationDao.listByRequirementId(r.getId());
                int active = 0;
                for (com.roncoo.education.user.dao.impl.mapper.entity.TutorApplication a : apps) {
                    Integer s = a.getAppStatus();
                    Integer st = a.getStatusId();
                    // status_id=0 (教员主动取消) 不算; 0=已申请, 1=入围, 2=已录用 算; 3=已驳回 不算
                    if (st != null && st == 1 && s != null && s != 3) active++;
                }
                r.setApplicationCount(active);
            }
        }
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

    /** 标为已接单 (admin 离线撮合成功后) */
    @Transactional(rollbackFor = Exception.class)
    public Result<String> match(Map<String, Object> req) {
        Long id = Long.parseLong(req.get("id").toString());
        String tutorRemark = req.get("matchedTutorRemark") != null ? req.get("matchedTutorRemark").toString() : "";
        TutorRequirement requirement = tutorRequirementDao.getById(id);
        if (requirement == null) return Result.error("需求不存在");
        if (!RequirementStatusEnum.PUBLISHED.getCode().equals(requirement.getReqStatus())) {
            return Result.error("仅已发布的需求可标为已接单");
        }
        TutorRequirement update = new TutorRequirement();
        update.setId(id);
        update.setReqStatus(RequirementStatusEnum.MATCHED.getCode());
        update.setMatchedAt(java.time.LocalDateTime.now());
        if (StringUtils.hasText(tutorRemark)) update.setMatchedTutorRemark(tutorRemark);
        tutorRequirementDao.updateById(update);
        saveAuditRecord(id, 3, "已接单: " + tutorRemark);
        return Result.success("已标记为已接单, 学员库该需求显示已接单");
    }

    public Result<Long> pendingCount() {
        TutorRequirementExample ex = new TutorRequirementExample();
        ex.createCriteria().andReqStatusEqualTo(RequirementStatusEnum.PENDING.getCode());
        return Result.success((long) tutorRequirementDao.page(1, 1, ex).getTotalCount());
    }
}
