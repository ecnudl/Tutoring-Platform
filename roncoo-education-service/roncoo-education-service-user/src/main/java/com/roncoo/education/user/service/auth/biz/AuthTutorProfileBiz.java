package com.roncoo.education.user.service.auth.biz;

import com.roncoo.education.common.base.BaseBiz;
import com.roncoo.education.common.base.ThreadContext;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.common.core.enums.TutorAuditStatusEnum;
import com.roncoo.education.common.core.enums.UserTypeEnum;
import com.roncoo.education.common.tools.BeanUtil;
import com.roncoo.education.user.dao.TutorCertificationDao;
import com.roncoo.education.user.dao.TutorProfileDao;
import com.roncoo.education.user.dao.TutorSubjectDao;
import com.roncoo.education.user.dao.TutorTeachingAreaDao;
import com.roncoo.education.user.dao.UsersDao;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorCertification;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorProfile;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorSubject;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorTeachingArea;
import com.roncoo.education.user.dao.impl.mapper.entity.Users;
import com.roncoo.education.user.service.auth.req.AuthCertSaveReq;
import com.roncoo.education.user.service.auth.req.AuthTutorProfileSaveReq;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthTutorProfileBiz extends BaseBiz {
    @NotNull
    private final TutorProfileDao tutorProfileDao;
    @NotNull
    private final TutorCertificationDao tutorCertificationDao;
    @NotNull
    private final TutorSubjectDao tutorSubjectDao;
    @NotNull
    private final TutorTeachingAreaDao tutorTeachingAreaDao;
    @NotNull
    private final UsersDao usersDao;

    /**
     * 查看我的教员资料
     */
    public Result<?> view() {
        Long userId = ThreadContext.userId();
        Result<String> check = checkTutor(userId);
        if (check != null) {
            return check;
        }
        TutorProfile profile = tutorProfileDao.getByUserId(userId);
        if (profile == null) {
            return Result.error("教员资料不存在，请先完善资料");
        }
        return Result.success(profile);
    }

    /**
     * 保存/修改教员资料
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<String> save(AuthTutorProfileSaveReq req) {
        Long userId = ThreadContext.userId();
        Result<String> check = checkTutor(userId);
        if (check != null) {
            return check;
        }

        TutorProfile profile = tutorProfileDao.getByUserId(userId);
        if (profile == null) {
            return Result.error("教员资料不存在");
        }

        // 已提交审核中，不允许修改
        if (TutorAuditStatusEnum.PENDING.getCode().equals(profile.getAuditStatus())) {
            return Result.error("审核中不允许修改资料，请等待审核结果");
        }

        // 复制属性到更新对象
        TutorProfile update = new TutorProfile();
        update.setId(profile.getId());
        update.setAvatar(req.getAvatar());
        update.setRealName(req.getRealName());
        update.setGender(req.getGender());
        update.setBirthDate(req.getBirthDate());
        update.setIdCard(req.getIdCard());
        update.setTutorType(req.getTutorType());
        update.setIdentityDetail(req.getIdentityDetail());
        update.setDegree(req.getDegree());
        update.setUniversity(req.getUniversity());
        update.setWorkUnit(req.getWorkUnit());
        update.setUniversityId(req.getUniversityId());
        update.setMajor(req.getMajor());
        update.setGradeYear(req.getGradeYear());
        update.setHighSchool(req.getHighSchool());
        update.setHometownProvince(req.getHometownProvince());
        update.setEmail(req.getEmail());
        update.setWechat(req.getWechat());
        update.setSelfIntroduction(req.getSelfIntroduction());
        update.setCertificatesDesc(req.getCertificatesDesc());
        update.setTeachingExperience(req.getTeachingExperience());
        update.setTeachingMethod(req.getTeachingMethod());
        update.setSubjects(req.getSubjects());
        update.setGrades(req.getGrades());
        update.setTags(req.getTags());
        update.setProvinceId(req.getProvinceId());
        update.setCityId(req.getCityId());
        update.setDistrictId(req.getDistrictId());
        update.setAddress(req.getAddress());
        update.setPriceMin(req.getPriceMin());
        update.setPriceMax(req.getPriceMax());
        update.setSalaryRemark(req.getSalaryRemark());
        update.setFreeTrial(req.getFreeTrial());
        // 注: showSuccessRecord 走独立端点 /show-success-record (展示偏好, 不触发审核流转), 这里故意不读

        // 审核状态流转: REJECTED→DRAFT, APPROVED/PUBLISHED→PENDING (已发布的修改后回到待审核, 公开页暂时下架)
        Integer curStatus = profile.getAuditStatus();
        if (TutorAuditStatusEnum.REJECTED.getCode().equals(curStatus)) {
            update.setAuditStatus(TutorAuditStatusEnum.DRAFT.getCode());
        } else if (TutorAuditStatusEnum.APPROVED.getCode().equals(curStatus)
                || TutorAuditStatusEnum.PUBLISHED.getCode().equals(curStatus)) {
            update.setAuditStatus(TutorAuditStatusEnum.PENDING.getCode());
        }

        tutorProfileDao.updateById(update);

        // 同步可教科目子表 (tutor_profile.subjects 是 JSON 字符串, 子表用于 join 翻译科目名)
        syncSubjects(profile.getId(), req.getSubjects());
        // 同步授课区域子表 (基于 cityId + districts)
        syncTeachingAreas(profile.getId(), req.getCityId(), req.getDistricts());

        return Result.success("保存成功");
    }

    /** 当前教员的授课区域 districtId 列表 (供编辑表单初始化多选) */
    public Result<?> teachingAreas() {
        Long userId = ThreadContext.userId();
        Result<String> check = checkTutor(userId);
        if (check != null) return check;
        TutorProfile profile = tutorProfileDao.getByUserId(userId);
        if (profile == null) return Result.error("教员资料不存在");
        java.util.List<TutorTeachingArea> list = tutorTeachingAreaDao.listByTutorId(profile.getId());
        java.util.List<Long> ids = list.stream().map(TutorTeachingArea::getDistrictId)
                .filter(java.util.Objects::nonNull).toList();
        return Result.success(ids);
    }

    /** 解析 JSON 数组字符串 "[1,2,3]" 或 CSV "1,2,3" → List<Long>, 容错空字符串和非法值 */
    private static java.util.List<Long> parseIds(String s) {
        if (s == null) return java.util.Collections.emptyList();
        String t = s.trim();
        if (t.isEmpty() || "[]".equals(t)) return java.util.Collections.emptyList();
        // 去掉外层 [] 和所有空白, 然后按逗号分割
        if (t.startsWith("[") && t.endsWith("]")) t = t.substring(1, t.length() - 1);
        java.util.List<Long> out = new java.util.ArrayList<>();
        for (String p : t.split(",")) {
            String x = p.trim().replace("\"", "").replace("'", "");
            if (x.isEmpty()) continue;
            try { out.add(Long.parseLong(x)); } catch (NumberFormatException ignore) {}
        }
        return out;
    }

    private void syncSubjects(Long tutorId, String subjectsJson) {
        tutorSubjectDao.deleteByTutorId(tutorId);
        for (Long sid : parseIds(subjectsJson)) {
            TutorSubject ts = new TutorSubject();
            ts.setTutorId(tutorId);
            ts.setSubjectId(sid);
            ts.setStatusId(1);
            tutorSubjectDao.save(ts);
        }
    }

    private void syncTeachingAreas(Long tutorId, Long cityId, String districtsJson) {
        tutorTeachingAreaDao.deleteByTutorId(tutorId);
        if (cityId == null) return; // 没城市就清空, 不再写入 (city_id NOT NULL)
        for (Long did : parseIds(districtsJson)) {
            TutorTeachingArea ta = new TutorTeachingArea();
            ta.setTutorId(tutorId);
            ta.setCityId(cityId);
            ta.setDistrictId(did);
            ta.setStatusId(1);
            tutorTeachingAreaDao.save(ta);
        }
    }

    /** 仅更新头像, 不重置审核状态 (供 /head-photo 等单字段操作使用) */
    public Result<String> updateAvatar(String url) {
        Long userId = ThreadContext.userId();
        Result<String> check = checkTutor(userId);
        if (check != null) return check;
        TutorProfile profile = tutorProfileDao.getByUserId(userId);
        if (profile == null) return Result.error("教员资料不存在");
        TutorProfile update = new TutorProfile();
        update.setId(profile.getId());
        update.setAvatar(url);
        int n = tutorProfileDao.updateById(update);
        return n > 0 ? Result.success("头像已更新") : Result.error("更新失败");
    }

    /**
     * 仅切换"是否展示成功记录"开关 — 教员自治, 不触发审核流转.
     * flag: 0 = 不展示, 1 = 展示
     */
    public Result<String> updateShowSuccessRecord(Integer flag) {
        if (flag == null || (flag != 0 && flag != 1)) return Result.error("参数错误");
        Long userId = ThreadContext.userId();
        Result<String> check = checkTutor(userId);
        if (check != null) return check;
        TutorProfile profile = tutorProfileDao.getByUserId(userId);
        if (profile == null) return Result.error("教员资料不存在");
        TutorProfile update = new TutorProfile();
        update.setId(profile.getId());
        update.setShowSuccessRecord(flag);
        int n = tutorProfileDao.updateById(update);
        return n > 0 ? Result.success(flag == 1 ? "已开启展示" : "已关闭展示") : Result.error("更新失败");
    }

    /**
     * 提交审核
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<String> submitAudit() {
        Long userId = ThreadContext.userId();
        Result<String> check = checkTutor(userId);
        if (check != null) {
            return check;
        }

        TutorProfile profile = tutorProfileDao.getByUserId(userId);
        if (profile == null) {
            return Result.error("教员资料不存在");
        }

        // 只有草稿和驳回状态可以提交审核
        if (!TutorAuditStatusEnum.DRAFT.getCode().equals(profile.getAuditStatus())
                && !TutorAuditStatusEnum.REJECTED.getCode().equals(profile.getAuditStatus())) {
            return Result.error("当前状态不允许提交审核");
        }

        // 校验必要资料是否完整
        if (!StringUtils.hasText(profile.getRealName())) {
            return Result.error("请先填写真实姓名");
        }
        if (profile.getTutorType() == null || profile.getTutorType() == 0) {
            return Result.error("请先选择教员类型");
        }
        if (!StringUtils.hasText(profile.getSubjects())) {
            return Result.error("请先选择授课科目");
        }

        TutorProfile update = new TutorProfile();
        update.setId(profile.getId());
        update.setAuditStatus(TutorAuditStatusEnum.PENDING.getCode());
        update.setAuditRemark(null);
        tutorProfileDao.updateById(update);

        return Result.success("提交成功，请等待审核");
    }

    /**
     * 证书列表
     */
    public Result<?> certList() {
        Long userId = ThreadContext.userId();
        Result<String> check = checkTutor(userId);
        if (check != null) {
            return check;
        }
        TutorProfile profile = tutorProfileDao.getByUserId(userId);
        if (profile == null) {
            return Result.error("教员资料不存在");
        }
        List<TutorCertification> list = tutorCertificationDao.listByTutorId(profile.getId());
        return Result.success(list);
    }

    /**
     * 保存证书
     */
    // 证书类型的默认名称（前端统一展示，这里兜底避免 certName 为空）
    private static final java.util.Map<Integer, String> CERT_TYPE_DEFAULT_NAME = java.util.Map.of(
            1, "身份证正面",
            2, "身份证反面",
            3, "学生证/毕业证",
            4, "教师资格证",
            5, "其他身份证件"
    );

    public Result<String> certSave(AuthCertSaveReq req) {
        Long userId = ThreadContext.userId();
        Result<String> check = checkTutor(userId);
        if (check != null) {
            return check;
        }
        TutorProfile profile = tutorProfileDao.getByUserId(userId);
        if (profile == null) {
            return Result.error("教员资料不存在");
        }
        if (req.getCertType() == null) {
            return Result.error("证书类型不能为空");
        }

        // certName 缺省时用固定类型名兜底
        String certName = StringUtils.hasText(req.getCertName())
                ? req.getCertName()
                : CERT_TYPE_DEFAULT_NAME.getOrDefault(req.getCertType(), "认证证件");

        // 5 个固定槽位的 certType（1~5），同一个教员同一个类型只保留一条记录 → upsert
        java.util.List<TutorCertification> existing = tutorCertificationDao.listByTutorId(profile.getId());
        TutorCertification same = existing.stream()
                .filter(c -> req.getCertType().equals(c.getCertType()))
                .findFirst()
                .orElse(null);
        if (same != null) {
            TutorCertification update = new TutorCertification();
            update.setId(same.getId());
            update.setCertName(certName);
            update.setCertUrl(req.getCertUrl());
            update.setCertNo(req.getCertNo());
            update.setAuditStatus(0); // 重新上传后重置为待审核
            update.setAuditRemark(null);
            tutorCertificationDao.updateById(update);
            // 重置认证状态: 已审核过的证件被替换 -> 必须重审才能恢复认证标
            refreshTutorVerified(profile.getId());
            return Result.success("证书已更新，等待重新审核");
        }

        TutorCertification cert = new TutorCertification();
        cert.setTutorId(profile.getId());
        cert.setCertType(req.getCertType());
        cert.setCertName(certName);
        cert.setCertUrl(req.getCertUrl());
        cert.setCertNo(req.getCertNo());
        cert.setAuditStatus(0); // 待审核
        tutorCertificationDao.save(cert);
        refreshTutorVerified(profile.getId());
        return Result.success("证书保存成功");
    }

    /**
     * 删除证书
     */
    public Result<String> certDelete(Long id) {
        Long userId = ThreadContext.userId();
        Result<String> check = checkTutor(userId);
        if (check != null) {
            return check;
        }
        TutorProfile profile = tutorProfileDao.getByUserId(userId);
        if (profile == null) {
            return Result.error("教员资料不存在");
        }
        TutorCertification cert = tutorCertificationDao.getById(id);
        if (cert == null || !cert.getTutorId().equals(profile.getId())) {
            return Result.error("证书不存在或无权操作");
        }
        tutorCertificationDao.deleteById(id);
        // 删了已审核通过的身份证 -> 必须收回认证标
        refreshTutorVerified(profile.getId());
        return Result.success("删除成功");
    }

    /**
     * 与 AdminTutorAuditBiz.refreshTutorVerified 同步逻辑:
     * 身份证正面(certType=1) + 反面(certType=2) 都已审核通过(auditStatus=1) 才视为认证.
     */
    private void refreshTutorVerified(Long tutorId) {
        java.util.List<TutorCertification> list = tutorCertificationDao.listByTutorId(tutorId);
        boolean idFrontOk = list.stream().anyMatch(c -> Integer.valueOf(1).equals(c.getCertType()) && Integer.valueOf(1).equals(c.getAuditStatus()));
        boolean idBackOk = list.stream().anyMatch(c -> Integer.valueOf(2).equals(c.getCertType()) && Integer.valueOf(1).equals(c.getAuditStatus()));
        TutorProfile update = new TutorProfile();
        update.setId(tutorId);
        update.setIsVerified(idFrontOk && idBackOk ? 1 : 0);
        tutorProfileDao.updateById(update);
    }

    /**
     * 校验当前用户是否为教员
     */
    private Result<String> checkTutor(Long userId) {
        Users user = usersDao.getById(userId);
        if (user == null) {
            return Result.error("用户不存在");
        }
        if (!UserTypeEnum.TUTOR.getCode().equals(user.getUserType())) {
            return Result.error("非教员用户无法操作教员资料");
        }
        return null;
    }
}
