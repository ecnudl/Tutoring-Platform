package com.roncoo.education.user.service.auth.biz;

import com.roncoo.education.common.base.BaseBiz;
import com.roncoo.education.common.base.ThreadContext;
import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.base.page.PageUtil;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.common.core.enums.ReservationStatusEnum;
import com.roncoo.education.common.core.enums.TutorAuditStatusEnum;
import com.roncoo.education.common.core.enums.UserTypeEnum;
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

    /**
     * 我的预约列表（学员看自己发起的，教员看收到的）
     */
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
     * 学员：发起预约
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<String> create(Map<String, Object> req) {
        Long userId = ThreadContext.userId();
        Users user = usersDao.getById(userId);
        if (user == null || !UserTypeEnum.STUDENT.getCode().equals(user.getUserType())) {
            return Result.error("非学员用户无法发起预约");
        }

        Long tutorUserId = req.get("tutorUserId") != null ? Long.parseLong(req.get("tutorUserId").toString()) : null;
        if (tutorUserId == null) {
            return Result.error("教员用户ID不能为空");
        }
        // 不能预约自己
        if (tutorUserId.equals(userId)) {
            return Result.error("不能预约自己");
        }

        // 校验教员存在且已审核通过
        TutorProfile tutorProfile = tutorProfileDao.getByUserId(tutorUserId);
        if (tutorProfile == null) {
            return Result.error("教员不存在");
        }
        if (!TutorAuditStatusEnum.APPROVED.getCode().equals(tutorProfile.getAuditStatus())) {
            return Result.error("该教员未通过审核，暂不可预约");
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
        String remark = req.get("remark") != null ? req.get("remark").toString() : "";
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

        return Result.success("预约成功，等待教员确认");
    }

    /**
     * 教员：确认预约
     */
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
        return Result.success("已确认预约");
    }

    /**
     * 教员/学员：完成预约
     */
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

    /**
     * 教员：拒绝预约 / 学员或教员：取消预约
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<String> cancel(Long id, String reason) {
        Long userId = ThreadContext.userId();
        TutorReservation reservation = tutorReservationDao.getById(id);
        if (reservation == null) {
            return Result.error("预约不存在");
        }
        if (!reservation.getTutorUserId().equals(userId) && !reservation.getStudentUserId().equals(userId)) {
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
        return Result.success("已取消/拒绝");
    }
}
