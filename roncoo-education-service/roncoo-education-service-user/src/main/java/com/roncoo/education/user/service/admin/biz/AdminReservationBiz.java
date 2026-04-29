package com.roncoo.education.user.service.admin.biz;

import com.roncoo.education.common.base.BaseBiz;
import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.common.core.enums.RequirementStatusEnum;
import com.roncoo.education.common.core.enums.ReservationStatusEnum;
import com.roncoo.education.user.dao.MsgDao;
import com.roncoo.education.user.dao.MsgUserDao;
import com.roncoo.education.user.dao.TutorRequirementDao;
import com.roncoo.education.user.dao.TutorReservationDao;
import com.roncoo.education.user.dao.impl.mapper.entity.*;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AdminReservationBiz extends BaseBiz {
    @NotNull
    private final TutorReservationDao tutorReservationDao;
    @NotNull
    private final TutorRequirementDao tutorRequirementDao;
    @NotNull
    private final MsgDao msgDao;
    @NotNull
    private final MsgUserDao msgUserDao;

    public Result<?> page(Map<String, Object> req) {
        int pageCurrent = req.get("pageCurrent") != null ? Integer.parseInt(req.get("pageCurrent").toString()) : 1;
        int pageSize = req.get("pageSize") != null ? Integer.parseInt(req.get("pageSize").toString()) : 20;
        Integer resStatus = req.get("resStatus") != null ? Integer.parseInt(req.get("resStatus").toString()) : null;

        TutorReservationExample example = new TutorReservationExample();
        TutorReservationExample.Criteria c = example.createCriteria();
        if (resStatus != null) c.andResStatusEqualTo(resStatus);
        example.setOrderByClause("gmt_create desc");
        Page<TutorReservation> page = tutorReservationDao.page(pageCurrent, pageSize, example);
        return Result.success(page);
    }

    public Result<?> view(Long id) {
        TutorReservation res = tutorReservationDao.getById(id);
        if (res == null) return Result.error("预约不存在");
        return Result.success(res);
    }

    public Result<Long> pendingCount() {
        TutorReservationExample ex = new TutorReservationExample();
        ex.createCriteria().andResStatusEqualTo(ReservationStatusEnum.PENDING.getCode());
        return Result.success((long) tutorReservationDao.page(1,1,ex).getTotalCount());
    }

    /** Admin 确认匹配: reservation status -> CONFIRMED(MATCHED), 关联 requirement -> MATCHED */
    @Transactional(rollbackFor = Exception.class)
    public Result<String> match(Map<String, Object> req) {
        Long id = Long.parseLong(req.get("id").toString());
        TutorReservation r = tutorReservationDao.getById(id);
        if (r == null) return Result.error("预约不存在");
        if (!ReservationStatusEnum.PENDING.getCode().equals(r.getResStatus())) {
            return Result.error("该预约已处理");
        }
        LocalDateTime now = LocalDateTime.now();
        TutorReservation update = new TutorReservation();
        update.setId(id);
        update.setResStatus(ReservationStatusEnum.CONFIRMED.getCode()); // 1=MATCHED
        update.setMatchedAt(now);
        tutorReservationDao.updateById(update);

        if (r.getRequirementId() != null) {
            TutorRequirement reqUp = new TutorRequirement();
            reqUp.setId(r.getRequirementId());
            reqUp.setReqStatus(RequirementStatusEnum.MATCHED.getCode());
            reqUp.setMatchedAt(now);
            String matchedRemark = req.get("matchedTutorRemark") != null
                    ? req.get("matchedTutorRemark").toString() : null;
            if (StringUtils.hasText(matchedRemark)) reqUp.setMatchedTutorRemark(matchedRemark);
            tutorRequirementDao.updateById(reqUp);
        }
        return Result.success("已确认匹配, 学员库该需求显示已接单");
    }

    /** Admin 驳回: 给学员发"很抱歉"站内信, reservation+requirement 都置 REJECTED/CANCELLED */
    @Transactional(rollbackFor = Exception.class)
    public Result<String> reject(Map<String, Object> req) {
        Long id = Long.parseLong(req.get("id").toString());
        String reason = req.get("reason") != null ? req.get("reason").toString() : "";
        TutorReservation r = tutorReservationDao.getById(id);
        if (r == null) return Result.error("预约不存在");
        if (!ReservationStatusEnum.PENDING.getCode().equals(r.getResStatus())) {
            return Result.error("该预约已处理");
        }
        TutorReservation update = new TutorReservation();
        update.setId(id);
        update.setResStatus(ReservationStatusEnum.CANCELLED.getCode());
        update.setCancelReason(reason);
        tutorReservationDao.updateById(update);

        if (r.getRequirementId() != null) {
            TutorRequirement reqUp = new TutorRequirement();
            reqUp.setId(r.getRequirementId());
            reqUp.setReqStatus(RequirementStatusEnum.REJECTED.getCode());
            reqUp.setAuditRemark(reason);
            tutorRequirementDao.updateById(reqUp);
        }

        // 给学员站内信; 失败不阻塞主流程, 但响应文案如实反映
        boolean msgOk = sendMsg(r.getStudentUserId(), "很抱歉, 您的需求未审核通过",
                "我们暂时无法为您安排合适的教员" + (StringUtils.hasText(reason) ? "(原因: " + reason + ")" : "") + "。如有疑问可联系客服。");
        return Result.success(msgOk ? "已驳回, 已通知学员" : "已驳回 (站内信发送失败, 请人工电话告知学员)");
    }

    /** Admin: 目标教员不接, 把关联 requirement 推到 PUBLISHED, 让其他教员看到 */
    @Transactional(rollbackFor = Exception.class)
    public Result<String> publishRequirement(Map<String, Object> req) {
        Long id = Long.parseLong(req.get("id").toString());
        TutorReservation r = tutorReservationDao.getById(id);
        if (r == null) return Result.error("预约不存在");
        if (!ReservationStatusEnum.PENDING.getCode().equals(r.getResStatus())) {
            return Result.error("该预约已处理, 无法转入公开池");
        }
        if (r.getRequirementId() == null) return Result.error("该预约未关联需求");
        TutorRequirement requirement = tutorRequirementDao.getById(r.getRequirementId());
        if (requirement == null) return Result.error("关联需求不存在");
        // 关联需求必须仍是 PENDING (待审核) 才允许直接发布; 已处理过的不允许覆盖
        if (!RequirementStatusEnum.PENDING.getCode().equals(requirement.getReqStatus())) {
            return Result.error("关联需求当前状态不允许转入公开池");
        }

        TutorRequirement reqUp = new TutorRequirement();
        reqUp.setId(requirement.getId());
        reqUp.setReqStatus(RequirementStatusEnum.PUBLISHED.getCode());
        tutorRequirementDao.updateById(reqUp);
        // reservation 也置为 cancelled (因为目标教员不接), 学员可被告知已转入公开池
        TutorReservation upd = new TutorReservation();
        upd.setId(id);
        upd.setResStatus(ReservationStatusEnum.CANCELLED.getCode());
        upd.setCancelReason("目标教员未接, 已转入公开需求池");
        tutorReservationDao.updateById(upd);
        return Result.success("已转入公开需求池");
    }

    private boolean sendMsg(Long userId, String title, String content) {
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
