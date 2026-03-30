package com.roncoo.education.user.service.admin.biz;

import com.roncoo.education.common.base.BaseBiz;
import com.roncoo.education.common.base.ThreadContext;
import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.base.page.PageUtil;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.common.core.enums.TutorAuditStatusEnum;
import com.roncoo.education.common.tools.BeanUtil;
import com.roncoo.education.user.dao.TutorAuditRecordDao;
import com.roncoo.education.user.dao.TutorProfileDao;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorAuditRecord;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorProfile;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorProfileExample;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class AdminTutorAuditBiz extends BaseBiz {
    @NotNull
    private final TutorProfileDao tutorProfileDao;
    @NotNull
    private final TutorAuditRecordDao tutorAuditRecordDao;

    /**
     * 待审核教员分页列表
     */
    public Result<?> page(Map<String, Object> req) {
        int pageCurrent = req.get("pageCurrent") != null ? Integer.parseInt(req.get("pageCurrent").toString()) : 1;
        int pageSize = req.get("pageSize") != null ? Integer.parseInt(req.get("pageSize").toString()) : 20;
        Integer auditStatus = req.get("auditStatus") != null ? Integer.parseInt(req.get("auditStatus").toString()) : null;

        TutorProfileExample example = new TutorProfileExample();
        TutorProfileExample.Criteria c = example.createCriteria();
        if (auditStatus != null) {
            c.andAuditStatusEqualTo(auditStatus);
        }
        if (req.get("keyword") != null && StringUtils.hasText(req.get("keyword").toString())) {
            c.andRealNameLike(PageUtil.like(req.get("keyword").toString()));
        }
        example.setOrderByClause("gmt_create desc");
        Page<TutorProfile> page = tutorProfileDao.page(pageCurrent, pageSize, example);
        return Result.success(page);
    }

    /**
     * 教员审核详情
     */
    public Result<?> view(Long id) {
        TutorProfile profile = tutorProfileDao.getById(id);
        if (profile == null) {
            return Result.error("教员不存在");
        }
        return Result.success(profile);
    }

    /**
     * 审核通过
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<String> approve(Map<String, Object> req) {
        Long id = Long.parseLong(req.get("id").toString());
        String remark = req.get("auditRemark") != null ? req.get("auditRemark").toString() : "";

        TutorProfile profile = tutorProfileDao.getById(id);
        if (profile == null) {
            return Result.error("教员不存在");
        }
        if (!TutorAuditStatusEnum.PENDING.getCode().equals(profile.getAuditStatus())) {
            return Result.error("当前状态不允许审核，仅待审核状态可操作");
        }

        // 更新审核状态为通过
        TutorProfile update = new TutorProfile();
        update.setId(id);
        update.setAuditStatus(TutorAuditStatusEnum.APPROVED.getCode());
        update.setAuditRemark(remark);
        tutorProfileDao.updateById(update);

        // 写审核记录
        saveAuditRecord(id, 1, remark);

        return Result.success("审核通过");
    }

    /**
     * 审核驳回
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<String> reject(Map<String, Object> req) {
        Long id = Long.parseLong(req.get("id").toString());
        String remark = req.get("auditRemark") != null ? req.get("auditRemark").toString() : "";

        if (!StringUtils.hasText(remark)) {
            return Result.error("驳回原因不能为空");
        }

        TutorProfile profile = tutorProfileDao.getById(id);
        if (profile == null) {
            return Result.error("教员不存在");
        }
        if (!TutorAuditStatusEnum.PENDING.getCode().equals(profile.getAuditStatus())) {
            return Result.error("当前状态不允许审核，仅待审核状态可操作");
        }

        // 更新审核状态为驳回
        TutorProfile update = new TutorProfile();
        update.setId(id);
        update.setAuditStatus(TutorAuditStatusEnum.REJECTED.getCode());
        update.setAuditRemark(remark);
        tutorProfileDao.updateById(update);

        // 写审核记录
        saveAuditRecord(id, 2, remark);

        return Result.success("已驳回");
    }

    /**
     * 保存审核记录
     */
    private void saveAuditRecord(Long tutorId, Integer auditAction, String remark) {
        TutorAuditRecord record = new TutorAuditRecord();
        record.setTutorId(tutorId);
        record.setAuditAction(auditAction);
        record.setAuditRemark(remark);
        try {
            record.setAuditorId(ThreadContext.userId());
        } catch (Exception e) {
            // admin 场景下 ThreadContext 可能有值也可能没有
            record.setAuditorId(0L);
        }
        tutorAuditRecordDao.save(record);
    }
}
