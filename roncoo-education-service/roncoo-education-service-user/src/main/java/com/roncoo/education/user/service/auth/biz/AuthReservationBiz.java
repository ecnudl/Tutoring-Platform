package com.roncoo.education.user.service.auth.biz;

import com.roncoo.education.common.base.BaseBiz;
import com.roncoo.education.common.base.ThreadContext;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.common.core.enums.ReservationStatusEnum;
import com.roncoo.education.common.core.enums.TutorAuditStatusEnum;
import com.roncoo.education.common.core.enums.UserTypeEnum;
import com.roncoo.education.user.dao.MsgDao;
import com.roncoo.education.user.dao.MsgUserDao;
import com.roncoo.education.user.dao.TutorProfileDao;
import com.roncoo.education.user.dao.TutorReservationDao;
import com.roncoo.education.user.dao.UsersDao;
import com.roncoo.education.user.dao.impl.mapper.entity.*;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AuthReservationBiz extends BaseBiz {
    @NotNull
    private final TutorReservationDao tutorReservationDao;
    @NotNull
    private final TutorProfileDao tutorProfileDao;
    @NotNull
    private final UsersDao usersDao;
    @NotNull
    private final MsgDao msgDao;
    @NotNull
    private final MsgUserDao msgUserDao;

    public Result<?> page(Map<String, Object> req) {
        Long userId = ThreadContext.userId();
        Users user = usersDao.getById(userId);
        if (user == null) {
            return Result.error("用户不存在");
        }
        int pageCurrent = req.get("pageCurrent") != null ? Integer.parseInt(req.get("pageCurrent").toString()) : 1;
        int pageSize = req.get("pageSize") != null ? Integer.parseInt(req.get("pageSize").toString()) : 20;

        TutorReservationExample example = new TutorReservationExample();
        TutorReservationExample.Criteria c = example.createCriteria();
        if (UserTypeEnum.STUDENT.getCode().equals(user.getUserType())) {
            c.andStudentUserIdEqualTo(userId);
        } else if (UserTypeEnum.TUTOR.getCode().equals(user.getUserType())) {
            c.andTutorUserIdEqualTo(userId);
        } else {
            return Result.error("用户类型不支持此操作");
        }
        example.setOrderByClause("gmt_create desc");
        return Result.success(tutorReservationDao.page(pageCurrent, pageSize, example));
    }

    /**
     * 学员: 发起预约。
     * 入参兼容: tutorId (= tutor_profile.id, 前端教员详情页常用) 或 tutorUserId (= users.id)
     * 入参兼容: remark 或 message (历史前端传 message)
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<String> create(Map<String, Object> req) {
        Long userId = ThreadContext.userId();
        Users user = usersDao.getById(userId);
        if (user == null || !UserTypeEnum.STUDENT.getCode().equals(user.getUserType())) {
            return Result.error("非学员用户无法发起预约");
        }

        // 既接受 profile id 也接受 user id
        TutorProfile tutorProfile = null;
        if (req.get("tutorId") != null) {
            tutorProfile = tutorProfileDao.getById(Long.parseLong(req.get("tutorId").toString()));
        } else if (req.get("tutorUserId") != null) {
            tutorProfile = tutorProfileDao.getByUserId(Long.parseLong(req.get("tutorUserId").toString()));
        }
        if (tutorProfile == null) {
            return Result.error("教员不存在");
        }
        Long tutorUserId = tutorProfile.getUserId();
        if (tutorUserId.equals(userId)) {
            return Result.error("不能预约自己");
        }
        // 接受 APPROVED 或 PUBLISHED, 且后台未禁用
        Integer audit = tutorProfile.getAuditStatus();
        if (!TutorAuditStatusEnum.APPROVED.getCode().equals(audit)
                && !TutorAuditStatusEnum.PUBLISHED.getCode().equals(audit)) {
            return Result.error("该教员未通过审核，暂不可预约");
        }
        if (tutorProfile.getStatusId() == null || tutorProfile.getStatusId() != 1) {
            return Result.error("该教员当前不可预约");
        }

        // 重复预约校验：同一学员对同一教员有 PENDING 状态的预约不可重复
        List<TutorReservation> existing = tutorReservationDao.listByStudentUserId(userId);
        for (TutorReservation r : existing) {
            if (r.getTutorUserId().equals(tutorUserId)
                    && ReservationStatusEnum.PENDING.getCode().equals(r.getResStatus())) {
                return Result.error("您已有一个待确认的预约，请等待教员处理");
            }
        }

        String scheduleTime = req.get("scheduleTime") != null ? req.get("scheduleTime").toString() : "";
        String address = req.get("address") != null ? req.get("address").toString() : "";
        String remark = req.get("remark") != null ? req.get("remark").toString()
                : (req.get("message") != null ? req.get("message").toString() : "");
        Long subjectId = req.get("subjectId") != null ? Long.parseLong(req.get("subjectId").toString()) : null;

        TutorReservation reservation = new TutorReservation();
        reservation.setStudentUserId(userId);
        reservation.setTutorUserId(tutorUserId);
        reservation.setTutorId(tutorProfile.getId());
        reservation.setSubjectId(subjectId);
        reservation.setScheduleTime(scheduleTime);
        reservation.setAddress(address);
        reservation.setRemark(remark);
        reservation.setResStatus(ReservationStatusEnum.PENDING.getCode());
        tutorReservationDao.save(reservation);

        // 通知教员: 收到新预约 (链路 A/B 都需要; 若以后选 B 再额外加管理员通知)
        sendMsg(tutorUserId, "新的预约请求",
                "您收到了一条新的预约请求，请前往「我的预约」查看并确认。" + (remark.isEmpty() ? "" : "（学员留言：" + remark + "）"));
        return Result.success("预约成功，等待教员确认");
    }

    @Transactional(rollbackFor = Exception.class)
    public Result<String> confirm(Long id) {
        Long userId = ThreadContext.userId();
        TutorReservation reservation = tutorReservationDao.getById(id);
        if (reservation == null || !reservation.getTutorUserId().equals(userId)) {
            return Result.error("预约不存在或无权操作");
        }
        if (!ReservationStatusEnum.PENDING.getCode().equals(reservation.getResStatus())) {
            return Result.error("当前状态不允许确认");
        }
        TutorReservation update = new TutorReservation();
        update.setId(id);
        update.setResStatus(ReservationStatusEnum.CONFIRMED.getCode());
        tutorReservationDao.updateById(update);

        sendMsg(reservation.getStudentUserId(), "预约已确认",
                "教员已确认您的预约，请保持手机畅通。");
        return Result.success("已确认预约");
    }

    public Result<String> complete(Long id) {
        Long userId = ThreadContext.userId();
        TutorReservation reservation = tutorReservationDao.getById(id);
        if (reservation == null) {
            return Result.error("预约不存在");
        }
        if (!reservation.getTutorUserId().equals(userId) && !reservation.getStudentUserId().equals(userId)) {
            return Result.error("无权操作");
        }
        if (!ReservationStatusEnum.CONFIRMED.getCode().equals(reservation.getResStatus())) {
            return Result.error("当前状态不允许标记完成");
        }
        TutorReservation update = new TutorReservation();
        update.setId(id);
        update.setResStatus(ReservationStatusEnum.COMPLETED.getCode());
        tutorReservationDao.updateById(update);
        return Result.success("预约已完成");
    }

    @Transactional(rollbackFor = Exception.class)
    public Result<String> cancel(Long id, String reason) {
        Long userId = ThreadContext.userId();
        TutorReservation reservation = tutorReservationDao.getById(id);
        if (reservation == null) {
            return Result.error("预约不存在");
        }
        boolean isTutor = reservation.getTutorUserId().equals(userId);
        boolean isStudent = reservation.getStudentUserId().equals(userId);
        if (!isTutor && !isStudent) {
            return Result.error("无权操作");
        }
        if (!ReservationStatusEnum.PENDING.getCode().equals(reservation.getResStatus())) {
            return Result.error("当前状态不允许取消/拒绝");
        }
        TutorReservation update = new TutorReservation();
        update.setId(id);
        update.setResStatus(ReservationStatusEnum.CANCELLED.getCode());
        update.setCancelReason(reason);
        tutorReservationDao.updateById(update);

        // 通知对方
        Long otherSide = isTutor ? reservation.getStudentUserId() : reservation.getTutorUserId();
        String title = isTutor ? "预约被拒绝" : "预约被取消";
        String text = isTutor ? "教员未接受您的预约。" : "学员取消了预约。";
        if (reason != null && !reason.isEmpty()) {
            text += "（原因：" + reason + "）";
        }
        sendMsg(otherSide, title, text);
        return Result.success("已取消/拒绝");
    }

    /** 写一条站内信指向某用户. msg_type=1 (系统通知). 失败不影响主流程. */
    private void sendMsg(Long userId, String title, String content) {
        try {
            Msg msg = new Msg();
            msg.setMsgType(1);
            msg.setMsgTitle(title);
            msg.setMsgText(content);
            msgDao.save(msg);
            MsgUser mu = new MsgUser();
            mu.setMsgId(msg.getId());
            mu.setUserId(userId);
            mu.setIsRead(0);
            msgUserDao.save(mu);
        } catch (Exception ignored) {
        }
    }
}
