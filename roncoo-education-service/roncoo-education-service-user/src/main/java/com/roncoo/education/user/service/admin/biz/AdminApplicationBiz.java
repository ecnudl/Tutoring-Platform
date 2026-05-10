package com.roncoo.education.user.service.admin.biz;

import com.roncoo.education.common.base.BaseBiz;
import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.common.core.enums.ApplicationStatusEnum;
import com.roncoo.education.common.core.enums.RequirementStatusEnum;
import com.roncoo.education.common.core.enums.ReservationStatusEnum;
import com.roncoo.education.common.tools.IdWorker;
import com.roncoo.education.user.dao.MsgDao;
import com.roncoo.education.user.dao.MsgUserDao;
import com.roncoo.education.user.dao.TutorApplicationDao;
import com.roncoo.education.user.dao.TutorProfileDao;
import com.roncoo.education.user.dao.TutorRequirementDao;
import com.roncoo.education.user.dao.TutorReservationDao;
import com.roncoo.education.user.dao.impl.mapper.entity.Msg;
import com.roncoo.education.user.dao.impl.mapper.entity.MsgUser;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorApplication;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorApplicationExample;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorProfile;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorRequirement;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorReservation;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AdminApplicationBiz extends BaseBiz {
    @NotNull
    private final TutorApplicationDao tutorApplicationDao;
    @NotNull
    private final TutorRequirementDao tutorRequirementDao;
    @NotNull
    private final TutorReservationDao tutorReservationDao;
    @NotNull
    private final TutorProfileDao tutorProfileDao;
    @NotNull
    private final MsgDao msgDao;
    @NotNull
    private final MsgUserDao msgUserDao;

    public Result<?> page(Map<String, Object> req) {
        int pageCurrent = req.get("pageCurrent") != null ? Integer.parseInt(req.get("pageCurrent").toString()) : 1;
        int pageSize = req.get("pageSize") != null ? Integer.parseInt(req.get("pageSize").toString()) : 20;
        Integer appStatus = req.get("appStatus") != null ? Integer.parseInt(req.get("appStatus").toString()) : null;

        TutorApplicationExample example = new TutorApplicationExample();
        TutorApplicationExample.Criteria c = example.createCriteria();
        // 过滤教员主动取消的软删行 (status_id=0), admin 不看
        c.andStatusIdEqualTo(1);
        if (appStatus != null) {
            c.andAppStatusEqualTo(appStatus);
        }
        if (req.get("requirementId") != null) {
            c.andRequirementIdEqualTo(Long.parseLong(req.get("requirementId").toString()));
        }
        example.setOrderByClause("gmt_create desc");
        Page<TutorApplication> page = tutorApplicationDao.page(pageCurrent, pageSize, example);

        // 富化: 把教员资料 (姓名/学校/学历/性别/displayNo) 一起带回, admin 审核不需要再点开教员看
        java.util.List<java.util.Map<String, Object>> enriched = new java.util.ArrayList<>();
        if (page.getList() != null) {
            for (TutorApplication app : page.getList()) {
                java.util.Map<String, Object> row = new java.util.HashMap<>();
                row.put("id", app.getId());
                row.put("requirementId", app.getRequirementId());
                row.put("tutorId", app.getTutorId());
                row.put("userId", app.getUserId());
                row.put("mobile", app.getMobile());
                row.put("applyMessage", app.getApplyMessage());
                row.put("appStatus", app.getAppStatus());
                row.put("gmtCreate", app.getGmtCreate());
                if (app.getTutorId() != null) {
                    TutorProfile tp = tutorProfileDao.getById(app.getTutorId());
                    if (tp != null) {
                        row.put("tutorRealName", tp.getRealName());
                        row.put("tutorUniversity", tp.getUniversity());
                        row.put("tutorDegree", tp.getDegree());
                        row.put("tutorGender", tp.getGender());
                        row.put("tutorDisplayNo", tp.getDisplayNo());
                    }
                }
                enriched.add(row);
            }
        }
        java.util.Map<String, Object> resp = new java.util.HashMap<>();
        resp.put("list", enriched);
        resp.put("totalCount", page.getTotalCount());
        resp.put("pageCurrent", page.getPageCurrent());
        resp.put("pageSize", page.getPageSize());
        return Result.success(resp);
    }

    public Result<?> view(Long id) {
        TutorApplication app = tutorApplicationDao.getById(id);
        if (app == null) {
            return Result.error("申请不存在");
        }
        return Result.success(app);
    }

    /**
     * Admin 撮合: 把 application 标 ACCEPTED, requirement 标 MATCHED, 创建 reservation,
     * 同 requirement 下其他 application 自动 REJECTED, 双方发站内信.
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<String> match(Long id) {
        TutorApplication app = tutorApplicationDao.getById(id);
        if (app == null) return Result.error("申请不存在");
        if (ApplicationStatusEnum.ACCEPTED.getCode().equals(app.getAppStatus())) {
            return Result.error("该申请已被录用");
        }
        TutorRequirement requirement = tutorRequirementDao.getById(app.getRequirementId());
        if (requirement == null) return Result.error("需求不存在");
        if (RequirementStatusEnum.MATCHED.getCode().equals(requirement.getReqStatus())) {
            return Result.error("该需求已匹配其他教员");
        }
        if (RequirementStatusEnum.CLOSED.getCode().equals(requirement.getReqStatus())
                || RequirementStatusEnum.REJECTED.getCode().equals(requirement.getReqStatus())) {
            return Result.error("该需求已关闭, 无法匹配");
        }

        LocalDateTime now = LocalDateTime.now();

        // 1. application -> ACCEPTED
        TutorApplication acceptUp = new TutorApplication();
        acceptUp.setId(id);
        acceptUp.setAppStatus(ApplicationStatusEnum.ACCEPTED.getCode());
        tutorApplicationDao.updateById(acceptUp);

        // 2. requirement -> MATCHED + matched_at
        TutorRequirement reqUp = new TutorRequirement();
        reqUp.setId(requirement.getId());
        reqUp.setReqStatus(RequirementStatusEnum.MATCHED.getCode());
        reqUp.setMatchedAt(now);
        tutorRequirementDao.updateById(reqUp);

        // 3. 同需求下其他 application -> REJECTED (用 listByRequirementId, 过滤掉自己 + 已 REJECTED)
        List<TutorApplication> siblings = tutorApplicationDao.listByRequirementId(requirement.getId());
        for (TutorApplication o : siblings) {
            if (id.equals(o.getId())) continue;
            if (ApplicationStatusEnum.REJECTED.getCode().equals(o.getAppStatus())) continue;
            TutorApplication rejUp = new TutorApplication();
            rejUp.setId(o.getId());
            rejUp.setAppStatus(ApplicationStatusEnum.REJECTED.getCode());
            tutorApplicationDao.updateById(rejUp);
        }

        // 4. 创建 reservation 关联 (这样 /jy/t<id> "成功记录" 能显示这一单)
        TutorProfile tProfile = tutorProfileDao.getById(app.getTutorId());
        Long tutorUserId = tProfile != null ? tProfile.getUserId() : app.getUserId();
        TutorReservation reservation = new TutorReservation();
        reservation.setId(IdWorker.getId());
        reservation.setStudentUserId(requirement.getUserId() == null ? 0L : requirement.getUserId());
        reservation.setTutorUserId(tutorUserId == null ? 0L : tutorUserId);
        reservation.setTutorId(app.getTutorId());
        reservation.setRequirementId(requirement.getId());
        reservation.setContactName(tProfile != null ? tProfile.getRealName() : null);
        reservation.setContactMobile(app.getMobile());
        reservation.setAddress(requirement.getAddress());
        reservation.setRemark(app.getApplyMessage());
        reservation.setResStatus(ReservationStatusEnum.CONFIRMED.getCode());
        reservation.setMatchedAt(now);
        tutorReservationDao.save(reservation);

        // 5. 站内信
        sendMsg(tutorUserId, "您的申请已被录用",
                "请保持电话畅通, 客服稍后会与您联系撮合家教订单事宜.");
        if (requirement.getUserId() != null && requirement.getUserId() > 0) {
            sendMsg(requirement.getUserId(), "已为您匹配教员",
                    "客服已为您匹配教员, 稍后会与您联系。");
        }
        return Result.success("已匹配, 已通知双方");
    }

    /**
     * Admin 主动驳回某条申请: 仅把 application.appStatus = REJECTED, 不动 requirement.
     * 需求保持 PUBLISHED, 等其他教员申请.
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<String> reject(Long id) {
        TutorApplication app = tutorApplicationDao.getById(id);
        if (app == null) return Result.error("申请不存在");
        if (ApplicationStatusEnum.REJECTED.getCode().equals(app.getAppStatus())) {
            return Result.error("该申请已驳回");
        }
        if (ApplicationStatusEnum.ACCEPTED.getCode().equals(app.getAppStatus())) {
            return Result.error("该申请已被录用, 无法驳回");
        }
        TutorApplication update = new TutorApplication();
        update.setId(id);
        update.setAppStatus(ApplicationStatusEnum.REJECTED.getCode());
        tutorApplicationDao.updateById(update);
        // 通知教员
        Long notifyUserId = app.getUserId();
        if (notifyUserId != null && notifyUserId > 0) {
            sendMsg(notifyUserId, "您的申请未被录用",
                    "很抱歉, 该订单未与您匹配, 您可继续申请其他需求。");
        }
        return Result.success("已驳回");
    }

    private boolean sendMsg(Long userId, String title, String content) {
        if (userId == null || userId <= 0) return false;
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
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
