package com.roncoo.education.user.service.auth.biz;

import com.roncoo.education.common.base.BaseBiz;
import com.roncoo.education.common.base.ThreadContext;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.common.core.enums.RequirementStatusEnum;
import com.roncoo.education.common.core.enums.ReservationStatusEnum;
import com.roncoo.education.common.core.enums.TutorAuditStatusEnum;
import com.roncoo.education.common.core.enums.UserTypeEnum;
import com.roncoo.education.user.dao.TutorProfileDao;
import com.roncoo.education.user.dao.TutorRequirementDao;
import com.roncoo.education.user.dao.TutorReservationDao;
import com.roncoo.education.user.dao.UsersDao;
import com.roncoo.education.user.dao.impl.mapper.entity.*;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 学员预约（Flow 1·定向预约）业务。
 *
 * 改造后行为：
 * 1. 学员提交预约 → 同时写 reservation + requirement (双写, 后者待 admin 审核)
 * 2. 教员不再收到任何站内信通知 (匹配由 admin 主导)
 * 3. 教员调 page() 直接返回空 (教员看不到学员手机号)
 * 4. confirm/cancel 端点保留, 但仅 admin 视图需要; 教员侧栏已下架, 不会被触发
 */
@Component
@RequiredArgsConstructor
public class AuthReservationBiz extends BaseBiz {
    @NotNull
    private final TutorReservationDao tutorReservationDao;
    @NotNull
    private final TutorProfileDao tutorProfileDao;
    @NotNull
    private final TutorRequirementDao tutorRequirementDao;
    @NotNull
    private final UsersDao usersDao;

    /** 列表: 学员看自己发起的; 教员一律返回空 (匹配信息只 admin 可见) */
    public Result<?> page(Map<String, Object> req) {
        Long userId = ThreadContext.userId();
        Users user = usersDao.getById(userId);
        if (user == null) return Result.error("用户不存在");

        if (UserTypeEnum.TUTOR.getCode().equals(user.getUserType())) {
            // 教员看不到自己被预约的信息 (含学员手机/微信)
            com.roncoo.education.common.base.page.Page<TutorReservation> empty =
                new com.roncoo.education.common.base.page.Page<>(0, 0, 1, 20, java.util.Collections.emptyList());
            return Result.success(empty);
        }

        int pageCurrent = req.get("pageCurrent") != null ? Integer.parseInt(req.get("pageCurrent").toString()) : 1;
        int pageSize = req.get("pageSize") != null ? Integer.parseInt(req.get("pageSize").toString()) : 20;
        TutorReservationExample example = new TutorReservationExample();
        if (UserTypeEnum.STUDENT.getCode().equals(user.getUserType())) {
            example.createCriteria().andStudentUserIdEqualTo(userId);
        } else {
            return Result.error("用户类型不支持此操作");
        }
        example.setOrderByClause("gmt_create desc");
        return Result.success(tutorReservationDao.page(pageCurrent, pageSize, example));
    }

    /** 学员: 发起定向预约. 双写 reservation + requirement(待审核) */
    @Transactional(rollbackFor = Exception.class)
    public Result<String> create(Map<String, Object> req) {
        Long userId = ThreadContext.userId();
        Users user = usersDao.getById(userId);
        if (user == null || !UserTypeEnum.STUDENT.getCode().equals(user.getUserType())) {
            return Result.error("非学员用户无法发起预约");
        }

        TutorProfile tutorProfile = null;
        if (req.get("tutorId") != null) {
            tutorProfile = tutorProfileDao.getById(Long.parseLong(req.get("tutorId").toString()));
        } else if (req.get("tutorUserId") != null) {
            tutorProfile = tutorProfileDao.getByUserId(Long.parseLong(req.get("tutorUserId").toString()));
        }
        if (tutorProfile == null) return Result.error("教员不存在");

        Long tutorUserId = tutorProfile.getUserId();
        if (tutorUserId.equals(userId)) return Result.error("不能预约自己");

        Integer audit = tutorProfile.getAuditStatus();
        if (!TutorAuditStatusEnum.APPROVED.getCode().equals(audit)
                && !TutorAuditStatusEnum.PUBLISHED.getCode().equals(audit)) {
            return Result.error("该教员未通过审核, 暂不可预约");
        }
        if (tutorProfile.getStatusId() == null || tutorProfile.getStatusId() != 1) {
            return Result.error("该教员当前不可预约");
        }

        // 重复预约校验
        List<TutorReservation> existing = tutorReservationDao.listByStudentUserId(userId);
        for (TutorReservation r : existing) {
            if (r.getTutorUserId().equals(tutorUserId)
                    && ReservationStatusEnum.PENDING.getCode().equals(r.getResStatus())) {
                return Result.error("您已有一个待处理的预约, 请等待客服联系");
            }
        }

        String contactName = req.get("contactName") != null ? req.get("contactName").toString() : null;
        String contactMobile = req.get("contactMobile") != null ? req.get("contactMobile").toString() : null;
        String contactWechat = req.get("contactWechat") != null ? req.get("contactWechat").toString() : null;
        String remark = req.get("remark") != null ? req.get("remark").toString()
                : (req.get("message") != null ? req.get("message").toString() : "");

        // 1) 双写 - 先建 requirement (PENDING 待审核, 锁定 target_tutor_user_id)
        TutorRequirement requirement = new TutorRequirement();
        requirement.setUserId(userId);
        requirement.setTargetTutorUserId(tutorUserId);
        String tutorName = tutorProfile.getRealName() != null ? tutorProfile.getRealName() : "教员";
        requirement.setTitle("定向预约 " + tutorName.charAt(0) + "教员 (" + (tutorProfile.getDisplayNo() == null ? "" : tutorProfile.getDisplayNo()) + ")");
        requirement.setRequirementDetail(remark);
        requirement.setContactName(contactName);
        requirement.setContactMobile(contactMobile);
        requirement.setContactWechat(contactWechat);
        requirement.setReqStatus(RequirementStatusEnum.PENDING.getCode());
        tutorRequirementDao.save(requirement);

        // 2) 再建 reservation, 关联到 requirement.id
        TutorReservation reservation = new TutorReservation();
        reservation.setStudentUserId(userId);
        reservation.setTutorUserId(tutorUserId);
        reservation.setTutorId(tutorProfile.getId());
        reservation.setRequirementId(requirement.getId());
        reservation.setContactName(contactName);
        reservation.setContactMobile(contactMobile);
        reservation.setContactWechat(contactWechat);
        reservation.setRemark(remark);
        reservation.setResStatus(ReservationStatusEnum.PENDING.getCode());
        tutorReservationDao.save(reservation);

        // 不再发站内信给教员 (改由 admin 主导匹配)
        return Result.success("预约请求已提交, 工作人员将在 24 小时内联系您");
    }

    /** Deprecated: 教员侧确认入口已下架; 保留方法以防外部调用 (admin 应改用 /user/admin/reservation/match) */
    @Transactional(rollbackFor = Exception.class)
    public Result<String> confirm(Long id) {
        return Result.error("该入口已下线, 请联系客服");
    }

    public Result<String> complete(Long id) {
        return Result.error("该入口已下线, 请联系客服");
    }

    @Transactional(rollbackFor = Exception.class)
    public Result<String> cancel(Long id, String reason) {
        // 学员可主动取消自己发起的且 status=PENDING 的预约
        Long userId = ThreadContext.userId();
        TutorReservation reservation = tutorReservationDao.getById(id);
        if (reservation == null) return Result.error("预约不存在");
        if (!reservation.getStudentUserId().equals(userId)) {
            return Result.error("只有发起人可以取消");
        }
        if (!ReservationStatusEnum.PENDING.getCode().equals(reservation.getResStatus())) {
            return Result.error("当前状态不允许取消");
        }
        TutorReservation update = new TutorReservation();
        update.setId(id);
        update.setResStatus(ReservationStatusEnum.CANCELLED.getCode());
        update.setCancelReason(reason);
        tutorReservationDao.updateById(update);

        // 关联的 requirement 也置为驳回(REJECTED)
        if (reservation.getRequirementId() != null) {
            TutorRequirement reqUpdate = new TutorRequirement();
            reqUpdate.setId(reservation.getRequirementId());
            reqUpdate.setReqStatus(RequirementStatusEnum.REJECTED.getCode());
            tutorRequirementDao.updateById(reqUpdate);
        }
        return Result.success("已取消");
    }
}
