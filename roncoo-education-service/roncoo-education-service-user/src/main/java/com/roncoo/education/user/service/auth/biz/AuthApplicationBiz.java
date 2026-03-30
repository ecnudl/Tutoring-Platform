package com.roncoo.education.user.service.auth.biz;

import com.roncoo.education.common.base.BaseBiz;
import com.roncoo.education.common.base.ThreadContext;
import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.base.page.PageUtil;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.common.core.enums.ApplicationStatusEnum;
import com.roncoo.education.common.core.enums.RequirementStatusEnum;
import com.roncoo.education.common.core.enums.UserTypeEnum;
import com.roncoo.education.user.dao.TutorApplicationDao;
import com.roncoo.education.user.dao.TutorProfileDao;
import com.roncoo.education.user.dao.TutorRequirementDao;
import com.roncoo.education.user.dao.UsersDao;
import com.roncoo.education.user.dao.VipMembershipDao;
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
    private final VipMembershipDao vipMembershipDao;

    private static final int DAILY_APPLY_LIMIT = 5;

    /**
     * 教员：我的申请列表
     */
    public Result<?> page(Map<String, Object> req) {
        Long userId = ThreadContext.userId();
        Users user = usersDao.getById(userId);
        if (user == null || !UserTypeEnum.TUTOR.getCode().equals(user.getUserType())) {
            return Result.error("非教员用户无法查看申请列表");
        }
        int pageCurrent = req.get("pageCurrent") != null ? Integer.parseInt(req.get("pageCurrent").toString()) : 1;
        int pageSize = req.get("pageSize") != null ? Integer.parseInt(req.get("pageSize").toString()) : 20;
        TutorApplicationExample example = new TutorApplicationExample();
        example.createCriteria().andUserIdEqualTo(userId);
        example.setOrderByClause("gmt_create desc");
        return Result.success(tutorApplicationDao.page(pageCurrent, pageSize, example));
    }

    /**
     * 学员：查看我的需求收到的申请
     */
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

    /**
     * 教员：申请需求
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<String> apply(Map<String, Object> req) {
        Long userId = ThreadContext.userId();
        Users user = usersDao.getById(userId);
        if (user == null || !UserTypeEnum.TUTOR.getCode().equals(user.getUserType())) {
            return Result.error("非教员用户无法申请需求");
        }
        Long requirementId = req.get("requirementId") != null ? Long.parseLong(req.get("requirementId").toString()) : null;
        String applyMessage = req.get("applyMessage") != null ? req.get("applyMessage").toString() : "";
        if (requirementId == null) {
            return Result.error("需求ID不能为空");
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
        // VIP权益：非VIP教员每日申请上限5次
        if (!isVipUser(userId)) {
            LocalDateTime todayStart = LocalDate.now().atStartOfDay();
            List<TutorApplication> todayApps = tutorApplicationDao.listByUserId(userId);
            long todayCount = todayApps.stream()
                    .filter(a -> a.getGmtCreate() != null && a.getGmtCreate().isAfter(todayStart))
                    .count();
            if (todayCount >= DAILY_APPLY_LIMIT) {
                return Result.error("今日申请次数已用完（" + DAILY_APPLY_LIMIT + "次/天），开通VIP可不受限制");
            }
        }
        TutorApplication application = new TutorApplication();
        application.setRequirementId(requirementId);
        application.setTutorId(profile.getId());
        application.setUserId(userId);
        application.setApplyMessage(applyMessage);
        application.setAppStatus(ApplicationStatusEnum.APPLIED.getCode());
        tutorApplicationDao.save(application);
        return Result.success("申请成功");
    }

    /**
     * 学员：将申请标为入围
     */
    public Result<String> shortlist(Long id) {
        return updateAppStatus(id, ApplicationStatusEnum.APPLIED, ApplicationStatusEnum.SHORTLISTED, "入围成功");
    }

    /**
     * 学员：接受申请
     */
    public Result<String> accept(Long id) {
        return updateAppStatus(id, null, ApplicationStatusEnum.ACCEPTED, "已录用");
    }

    /**
     * 学员：拒绝申请
     */
    public Result<String> reject(Long id) {
        return updateAppStatus(id, null, ApplicationStatusEnum.REJECTED, "已拒绝");
    }

    private Result<String> updateAppStatus(Long id, ApplicationStatusEnum requiredFrom, ApplicationStatusEnum target, String successMsg) {
        Long userId = ThreadContext.userId();
        TutorApplication app = tutorApplicationDao.getById(id);
        if (app == null) {
            return Result.error("申请不存在");
        }
        // 验证当前用户是需求发布者
        TutorRequirement req = tutorRequirementDao.getById(app.getRequirementId());
        if (req == null || !req.getUserId().equals(userId)) {
            return Result.error("无权操作此申请");
        }
        if (requiredFrom != null && !requiredFrom.getCode().equals(app.getAppStatus())) {
            return Result.error("当前状态不允许此操作");
        }
        // 已录用或已拒绝不可再操作
        if (ApplicationStatusEnum.ACCEPTED.getCode().equals(app.getAppStatus()) || ApplicationStatusEnum.REJECTED.getCode().equals(app.getAppStatus())) {
            return Result.error("该申请已最终处理，不可修改");
        }
        TutorApplication update = new TutorApplication();
        update.setId(id);
        update.setAppStatus(target.getCode());
        tutorApplicationDao.updateById(update);
        return Result.success(successMsg);
    }

    /**
     * 教员：取消申请
     */
    public Result<String> cancel(Long id) {
        Long userId = ThreadContext.userId();
        TutorApplication application = tutorApplicationDao.getById(id);
        if (application == null || !application.getUserId().equals(userId)) {
            return Result.error("申请不存在或无权操作");
        }
        if (!ApplicationStatusEnum.APPLIED.getCode().equals(application.getAppStatus())) {
            return Result.error("当前状态不允许取消");
        }
        tutorApplicationDao.deleteById(id);
        return Result.success("取消成功");
    }

    /**
     * 检查用户是否为生效中的VIP
     */
    private boolean isVipUser(Long userId) {
        VipMembership vip = vipMembershipDao.getByUserId(userId);
        if (vip == null) {
            return false;
        }
        return vip.getStatusId() != null && vip.getStatusId() == 1
                && vip.getEndTime() != null && vip.getEndTime().isAfter(LocalDateTime.now());
    }
}
