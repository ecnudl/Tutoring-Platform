package com.roncoo.education.user.service.admin.biz;

import com.roncoo.education.common.base.BaseBiz;
import com.roncoo.education.common.base.ThreadContext;
import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.base.page.PageUtil;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.common.core.enums.TutorAuditStatusEnum;
import com.roncoo.education.common.tools.BeanUtil;
import com.roncoo.education.user.dao.TutorAuditRecordDao;
import com.roncoo.education.user.dao.TutorCertificationDao;
import com.roncoo.education.user.dao.TutorProfileDao;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorAuditRecord;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorCertification;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorProfile;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorProfileExample;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AdminTutorAuditBiz extends BaseBiz {
    @NotNull
    private final TutorProfileDao tutorProfileDao;
    @NotNull
    private final TutorAuditRecordDao tutorAuditRecordDao;
    @NotNull
    private final TutorCertificationDao tutorCertificationDao;
    @NotNull
    private final com.roncoo.education.user.dao.UsersDao usersDao;

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

        // 简历通过 → 直接 PUBLISHED，进教员库可见（蓝对勾）
        // 注意：is_verified 单独由"证件审核"驱动，此处不写；简历通过只代表基本资料已核
        TutorProfile update = new TutorProfile();
        update.setId(id);
        update.setAuditStatus(TutorAuditStatusEnum.PUBLISHED.getCode());
        update.setAuditRemark(remark);
        // 兜底：历史数据 display_no 可能为空，此时补一个，避免详情页 404
        if (profile.getDisplayNo() == null || profile.getDisplayNo().isEmpty()) {
            update.setDisplayNo("T" + (100000 + id % 900000));
        }
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
     * 查询教员的证书列表（管理端用）
     */
    public Result<List<TutorCertification>> certList(Long tutorId) {
        if (tutorId == null) {
            return Result.error("tutorId不能为空");
        }
        List<TutorCertification> certs = tutorCertificationDao.listByTutorId(tutorId);
        return Result.success(certs);
    }

    /**
     * 证件审核通过：将某一个证件置为通过，并重建 tutor_profile.is_verified
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<String> certApprove(Map<String, Object> req) {
        Long certId = req.get("id") != null ? Long.parseLong(req.get("id").toString()) : null;
        String remark = req.get("auditRemark") != null ? req.get("auditRemark").toString() : "";
        if (certId == null) {
            return Result.error("id不能为空");
        }
        TutorCertification cert = tutorCertificationDao.getById(certId);
        if (cert == null) {
            return Result.error("证件不存在");
        }
        TutorCertification update = new TutorCertification();
        update.setId(certId);
        update.setAuditStatus(1);
        update.setAuditRemark(remark);
        tutorCertificationDao.updateById(update);

        refreshTutorVerified(cert.getTutorId());
        return Result.success("证件已通过");
    }

    /**
     * 证件审核驳回
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<String> certReject(Map<String, Object> req) {
        Long certId = req.get("id") != null ? Long.parseLong(req.get("id").toString()) : null;
        String remark = req.get("auditRemark") != null ? req.get("auditRemark").toString() : "";
        if (certId == null) {
            return Result.error("id不能为空");
        }
        if (!StringUtils.hasText(remark)) {
            return Result.error("驳回原因不能为空");
        }
        TutorCertification cert = tutorCertificationDao.getById(certId);
        if (cert == null) {
            return Result.error("证件不存在");
        }
        TutorCertification update = new TutorCertification();
        update.setId(certId);
        update.setAuditStatus(2);
        update.setAuditRemark(remark);
        tutorCertificationDao.updateById(update);

        refreshTutorVerified(cert.getTutorId());
        return Result.success("证件已驳回");
    }

    /**
     * 设置或取消明星教员标记
     */
    public Result<String> setStar(Map<String, Object> req) {
        Long id = req.get("id") != null ? Long.parseLong(req.get("id").toString()) : null;
        Integer isStar = req.get("isStar") != null ? Integer.parseInt(req.get("isStar").toString()) : null;
        if (id == null || isStar == null) {
            return Result.error("参数错误");
        }
        if (isStar != 0 && isStar != 1) {
            return Result.error("isStar 只能是 0 或 1");
        }
        TutorProfile profile = tutorProfileDao.getById(id);
        if (profile == null) {
            return Result.error("教员不存在");
        }
        TutorProfile update = new TutorProfile();
        update.setId(id);
        update.setIsStar(isStar);
        tutorProfileDao.updateById(update);
        return Result.success(isStar == 1 ? "已设为明星教员" : "已取消明星教员");
    }

    /**
     * 近期被编辑的教员资料: 已审核通过 (audit_status=3 PUBLISHED 或 4 APPROVED) 的 profile 中,
     * 按 gmt_modified 倒序, 默认返回最近 30 天的最多 50 条. 用于运营周期性巡检教员是否擅自修改了已审核内容.
     */
    public Result<?> recentEditedProfiles(Map<String, Object> req) {
        int days = req.get("days") != null ? Integer.parseInt(req.get("days").toString()) : 30;
        int limit = req.get("limit") != null ? Integer.parseInt(req.get("limit").toString()) : 50;

        TutorProfileExample example = new TutorProfileExample();
        TutorProfileExample.Criteria c = example.createCriteria();
        c.andAuditStatusIn(java.util.Arrays.asList(3, 4));
        c.andGmtModifiedGreaterThanOrEqualTo(java.time.LocalDateTime.now().minusDays(days));
        example.setOrderByClause("gmt_modified desc");

        com.roncoo.education.common.base.page.Page<com.roncoo.education.user.dao.impl.mapper.entity.TutorProfile> page =
                tutorProfileDao.page(1, limit, example);
        return Result.success(page);
    }

    /**
     * 分页查询所有教员的证件（连带教员姓名、手机号），供 admin 证件审核页使用
     */
    public Result<?> certPage(Map<String, Object> req) {
        int pageCurrent = req.get("pageCurrent") != null ? Integer.parseInt(req.get("pageCurrent").toString()) : 1;
        int pageSize = req.get("pageSize") != null ? Integer.parseInt(req.get("pageSize").toString()) : 20;
        Integer auditStatus = req.get("auditStatus") != null ? Integer.parseInt(req.get("auditStatus").toString()) : null;
        Integer certType = req.get("certType") != null ? Integer.parseInt(req.get("certType").toString()) : null;

        com.roncoo.education.user.dao.impl.mapper.entity.TutorCertificationExample example = new com.roncoo.education.user.dao.impl.mapper.entity.TutorCertificationExample();
        com.roncoo.education.user.dao.impl.mapper.entity.TutorCertificationExample.Criteria c = example.createCriteria();
        if (auditStatus != null) c.andAuditStatusEqualTo(auditStatus);
        if (certType != null) c.andCertTypeEqualTo(certType);
        example.setOrderByClause("audit_status asc, gmt_create desc");

        Page<TutorCertification> page = ((com.roncoo.education.user.dao.TutorCertificationDao) tutorCertificationDao).page(pageCurrent, pageSize, example);

        // 批量补齐教员姓名、手机号
        java.util.List<Long> tutorIds = page.getList().stream().map(TutorCertification::getTutorId).distinct().collect(java.util.stream.Collectors.toList());
        java.util.Map<Long, TutorProfile> tutorMap = new java.util.HashMap<>();
        java.util.Map<Long, String> mobileMap = new java.util.HashMap<>();
        if (!tutorIds.isEmpty()) {
            java.util.List<TutorProfile> profiles = tutorProfileDao.listByIds(tutorIds);
            for (TutorProfile p : profiles) tutorMap.put(p.getId(), p);
            java.util.List<Long> userIds = profiles.stream().map(TutorProfile::getUserId).distinct().collect(java.util.stream.Collectors.toList());
            if (!userIds.isEmpty()) {
                java.util.List<com.roncoo.education.user.dao.impl.mapper.entity.Users> users = usersDao.listByIds(userIds);
                java.util.Map<Long, String> userIdToMobile = new java.util.HashMap<>();
                for (com.roncoo.education.user.dao.impl.mapper.entity.Users u : users) userIdToMobile.put(u.getId(), u.getMobile());
                for (TutorProfile p : profiles) mobileMap.put(p.getId(), userIdToMobile.get(p.getUserId()));
            }
        }

        java.util.List<java.util.Map<String, Object>> list = new java.util.ArrayList<>();
        for (TutorCertification cert : page.getList()) {
            java.util.Map<String, Object> row = new java.util.LinkedHashMap<>();
            row.put("id", cert.getId());
            row.put("tutorId", cert.getTutorId());
            row.put("certType", cert.getCertType());
            row.put("certName", cert.getCertName());
            row.put("certUrl", cert.getCertUrl());
            row.put("certNo", cert.getCertNo());
            row.put("auditStatus", cert.getAuditStatus());
            row.put("auditRemark", cert.getAuditRemark());
            row.put("gmtCreate", cert.getGmtCreate());
            TutorProfile tp = tutorMap.get(cert.getTutorId());
            row.put("tutorName", tp != null ? tp.getRealName() : null);
            row.put("tutorMobile", mobileMap.get(cert.getTutorId()));
            list.add(row);
        }

        java.util.Map<String, Object> result = new java.util.LinkedHashMap<>();
        result.put("list", list);
        result.put("totalCount", page.getTotalCount());
        result.put("totalPage", page.getTotalPage());
        result.put("pageCurrent", page.getPageCurrent());
        result.put("pageSize", page.getPageSize());
        return Result.success(result);
    }

    /**
     * 根据教员当前证件状态，重建 is_verified：
     * 身份证正面(1) + 身份证反面(2) 两张都已审核通过(auditStatus=1)，才算证件已认证。
     */
    private void refreshTutorVerified(Long tutorId) {
        List<TutorCertification> list = tutorCertificationDao.listByTutorId(tutorId);
        boolean idFrontOk = list.stream().anyMatch(c -> Integer.valueOf(1).equals(c.getCertType()) && Integer.valueOf(1).equals(c.getAuditStatus()));
        boolean idBackOk  = list.stream().anyMatch(c -> Integer.valueOf(2).equals(c.getCertType()) && Integer.valueOf(1).equals(c.getAuditStatus()));
        TutorProfile update = new TutorProfile();
        update.setId(tutorId);
        update.setIsVerified(idFrontOk && idBackOk ? 1 : 0);
        tutorProfileDao.updateById(update);
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
