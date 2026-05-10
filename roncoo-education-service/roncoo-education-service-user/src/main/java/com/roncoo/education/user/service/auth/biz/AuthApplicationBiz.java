package com.roncoo.education.user.service.auth.biz;

import com.roncoo.education.common.base.BaseBiz;
import com.roncoo.education.common.base.ThreadContext;
import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.base.page.PageUtil;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.common.core.enums.ApplicationStatusEnum;
import com.roncoo.education.common.core.enums.RequirementStatusEnum;
import com.roncoo.education.common.core.enums.UserTypeEnum;
import com.roncoo.education.user.dao.MsgDao;
import com.roncoo.education.user.dao.MsgUserDao;
import com.roncoo.education.user.dao.TutorApplicationDao;
import com.roncoo.education.user.dao.TutorProfileDao;
import com.roncoo.education.user.dao.TutorRequirementDao;
import com.roncoo.education.user.dao.UsersDao;
import com.roncoo.education.user.dao.impl.mapper.entity.*;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AuthApplicationBiz extends BaseBiz {
    @NotNull
    private final TutorApplicationDao tutorApplicationDao;
    @NotNull
    private final TutorProfileDao tutorProfileDao;
    @NotNull
    private final TutorRequirementDao tutorRequirementDao;
    @NotNull
    private final UsersDao usersDao;
    @NotNull
    private final MsgDao msgDao;
    @NotNull
    private final MsgUserDao msgUserDao;

    private static final int DAILY_APPLY_LIMIT = 5;

    public Result<?> page(Map<String, Object> req) {
        Long userId = ThreadContext.userId();
        Users user = usersDao.getById(userId);
        if (user == null || !UserTypeEnum.TUTOR.getCode().equals(user.getUserType())) {
            return Result.error("非教员用户无法查看申请列表");
        }
        int pageCurrent = req.get("pageCurrent") != null ? Integer.parseInt(req.get("pageCurrent").toString()) : 1;
        int pageSize = req.get("pageSize") != null ? Integer.parseInt(req.get("pageSize").toString()) : 20;
        TutorApplicationExample example = new TutorApplicationExample();
        // 过滤软删 (cancel 后 status_id=0), 教员看不到自己取消的
        example.createCriteria().andUserIdEqualTo(userId).andStatusIdEqualTo(1);
        example.setOrderByClause("gmt_create desc");
        return Result.success(tutorApplicationDao.page(pageCurrent, pageSize, example));
    }

    public Result<?> received(Map<String, Object> req) {
        Long userId = ThreadContext.userId();
        Users user = usersDao.getById(userId);
        if (user == null || !UserTypeEnum.STUDENT.getCode().equals(user.getUserType())) {
            return Result.error("非学员用户无法查看收到的申请");
        }
        Long requirementId = req.get("requirementId") != null ? Long.parseLong(req.get("requirementId").toString()) : null;
        if (requirementId == null) {
            return Result.error("请指定需求ID");
        }
        TutorRequirement requirement = tutorRequirementDao.getById(requirementId);
        if (requirement == null || !requirement.getUserId().equals(userId)) {
            return Result.error("需求不存在或无权查看");
        }
        return Result.success(tutorApplicationDao.listByRequirementId(requirementId));
    }

    @Transactional(rollbackFor = Exception.class)
    public Result<String> apply(Map<String, Object> req) {
        Long userId = ThreadContext.userId();
        Users user = usersDao.getById(userId);
        if (user == null || !UserTypeEnum.TUTOR.getCode().equals(user.getUserType())) {
            return Result.error("非教员用户无法申请需求");
        }
        Long requirementId = req.get("requirementId") != null ? Long.parseLong(req.get("requirementId").toString()) : null;
        String applyMessage = req.get("applyMessage") != null ? req.get("applyMessage").toString() : "";
        String mobile = req.get("mobile") != null ? req.get("mobile").toString().trim() : "";
        if (requirementId == null) {
            return Result.error("需求ID不能为空");
        }
        if (mobile.isEmpty()) {
            return Result.error("联系电话不能为空");
        }
        if (!mobile.matches("^1[3-9]\\d{9}$")) {
            return Result.error("联系电话格式不正确");
        }
        TutorRequirement requirement = tutorRequirementDao.getById(requirementId);
        if (requirement == null) {
            return Result.error("需求不存在");
        }
        if (!RequirementStatusEnum.PUBLISHED.getCode().equals(requirement.getReqStatus())) {
            return Result.error("该需求未发布，不可申请");
        }
        if (requirement.getUserId().equals(userId)) {
            return Result.error("不能申请自己的需求");
        }
        TutorProfile profile = tutorProfileDao.getByUserId(userId);
        if (profile == null) {
            return Result.error("教员资料不存在");
        }
        TutorApplication existing = tutorApplicationDao.getByRequirementIdAndTutorId(requirementId, profile.getId());
        if (existing != null) {
            return Result.error("您已申请过该需求，不能重复申请");
        }
        // 每日申请上限 (统一限制, 无 VIP 例外)
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        List<TutorApplication> todayApps = tutorApplicationDao.listByUserId(userId);
        long todayCount = todayApps.stream()
                .filter(a -> a.getGmtCreate() != null && a.getGmtCreate().isAfter(todayStart))
                .count();
        if (todayCount >= DAILY_APPLY_LIMIT) {
            return Result.error("今日申请次数已用完 (" + DAILY_APPLY_LIMIT + " 次/天), 请明天再试");
        }
        TutorApplication application = new TutorApplication();
        application.setRequirementId(requirementId);
        application.setTutorId(profile.getId());
        application.setUserId(userId);
        application.setApplyMessage(applyMessage);
        application.setMobile(mobile);
        application.setAppStatus(ApplicationStatusEnum.APPLIED.getCode());
        tutorApplicationDao.save(application);

        // 通知需求发布者：有新申请
        String tutorName = profile.getRealName() != null ? profile.getRealName() : "教员";
        String reqTitle = requirement.getTitle() != null ? requirement.getTitle() : "您的需求";
        sendMsg(requirement.getUserId(),
                "新的家教申请",
                tutorName + " 已申请您的需求《" + reqTitle + "》，可在「我发布的需求 → 收到的申请」中查看。");
        return Result.success("申请成功");
    }

    public Result<String> shortlist(Long id) {
        return updateAppStatus(id, ApplicationStatusEnum.APPLIED, ApplicationStatusEnum.SHORTLISTED, "入围成功", "您的申请已被入围");
    }

    public Result<String> accept(Long id) {
        return updateAppStatus(id, null, ApplicationStatusEnum.ACCEPTED, "已录用", "您的申请已被录用！请尽快与学员联系");
    }

    public Result<String> reject(Long id) {
        return updateAppStatus(id, null, ApplicationStatusEnum.REJECTED, "已拒绝", "您的申请未被采纳");
    }

    private Result<String> updateAppStatus(Long id, ApplicationStatusEnum requiredFrom, ApplicationStatusEnum target, String successMsg, String tutorNotifyText) {
        Long userId = ThreadContext.userId();
        TutorApplication app = tutorApplicationDao.getById(id);
        if (app == null) {
            return Result.error("申请不存在");
        }
        TutorRequirement req = tutorRequirementDao.getById(app.getRequirementId());
        if (req == null || !req.getUserId().equals(userId)) {
            return Result.error("无权操作此申请");
        }
        // 需求必须仍在 PUBLISHED 状态才允许 accept/reject 申请;
        // admin 通过 confirmMatch 已锁定后, 家长不能再切换其他申请的状态
        if (!RequirementStatusEnum.PUBLISHED.getCode().equals(req.getReqStatus())) {
            return Result.error("此需求已不再接受新决策 (已匹配 / 已关闭)");
        }
        if (requiredFrom != null && !requiredFrom.getCode().equals(app.getAppStatus())) {
            return Result.error("当前状态不允许此操作");
        }
        if (ApplicationStatusEnum.ACCEPTED.getCode().equals(app.getAppStatus()) || ApplicationStatusEnum.REJECTED.getCode().equals(app.getAppStatus())) {
            return Result.error("该申请已最终处理，不可修改");
        }
        TutorApplication update = new TutorApplication();
        update.setId(id);
        update.setAppStatus(target.getCode());
        tutorApplicationDao.updateById(update);

        // 录用 -> 需求转为「已匹配」
        if (target == ApplicationStatusEnum.ACCEPTED
                && RequirementStatusEnum.PUBLISHED.getCode().equals(req.getReqStatus())) {
            TutorRequirement reqUpdate = new TutorRequirement();
            reqUpdate.setId(req.getId());
            reqUpdate.setReqStatus(RequirementStatusEnum.MATCHED.getCode());
            tutorRequirementDao.updateById(reqUpdate);
        }

        // 通知教员：申请状态变化
        String reqTitle = req.getTitle() != null ? req.getTitle() : "需求";
        sendMsg(app.getUserId(), "申请状态更新", tutorNotifyText + "（需求《" + reqTitle + "》）。");

        return Result.success(successMsg);
    }

    public Result<String> cancel(Long id) {
        Long userId = ThreadContext.userId();
        TutorApplication application = tutorApplicationDao.getById(id);
        if (application == null || !application.getUserId().equals(userId)) {
            return Result.error("申请不存在或无权操作");
        }
        if (!ApplicationStatusEnum.APPLIED.getCode().equals(application.getAppStatus())) {
            return Result.error("当前状态不允许取消");
        }
        // 软取消: status_id=0 软删 (查询过滤) + appStatus=REJECTED 业务态终结;
        // 教员/admin 视图都隐藏, 但记录保留用于反作弊 (cancel-then-reapply 仍计入每日额度).
        TutorApplication update = new TutorApplication();
        update.setId(id);
        update.setStatusId(0);
        update.setAppStatus(ApplicationStatusEnum.REJECTED.getCode());
        tutorApplicationDao.updateById(update);
        return Result.success("取消成功");
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
            // 通知失败不阻塞业务
        }
    }

}
