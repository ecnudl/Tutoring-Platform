package com.roncoo.education.user.service.auth.biz;

import com.roncoo.education.common.base.BaseBiz;
import com.roncoo.education.common.base.ThreadContext;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.common.core.enums.TutorAuditStatusEnum;
import com.roncoo.education.common.core.enums.UserTypeEnum;
import com.roncoo.education.common.tools.BeanUtil;
import com.roncoo.education.user.dao.TutorCertificationDao;
import com.roncoo.education.user.dao.TutorProfileDao;
import com.roncoo.education.user.dao.UsersDao;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorCertification;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorProfile;
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
        update.setUniversityId(req.getUniversityId());
        update.setMajor(req.getMajor());
        update.setGradeYear(req.getGradeYear());
        update.setHighSchool(req.getHighSchool());
        update.setHometownProvince(req.getHometownProvince());
        update.setEmail(req.getEmail());
        update.setWechat(req.getWechat());
        update.setSelfIntroduction(req.getSelfIntroduction());
        update.setCertificatesDesc(req.getCertificatesDesc());
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

        // 如果之前被驳回，修改后回到草稿状态
        if (TutorAuditStatusEnum.REJECTED.getCode().equals(profile.getAuditStatus())) {
            update.setAuditStatus(TutorAuditStatusEnum.DRAFT.getCode());
        }

        tutorProfileDao.updateById(update);
        return Result.success("保存成功");
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
        return Result.success("删除成功");
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
