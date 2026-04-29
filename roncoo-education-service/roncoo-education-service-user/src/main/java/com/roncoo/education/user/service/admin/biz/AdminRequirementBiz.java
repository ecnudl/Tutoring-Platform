package com.roncoo.education.user.service.admin.biz;

import com.roncoo.education.common.base.BaseBiz;
import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.base.page.PageUtil;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.common.core.enums.ApplicationStatusEnum;
import com.roncoo.education.common.core.enums.RequirementStatusEnum;
import com.roncoo.education.common.tools.IdWorker;
import com.roncoo.education.user.dao.MsgDao;
import com.roncoo.education.user.dao.MsgUserDao;
import com.roncoo.education.user.dao.TutorApplicationDao;
import com.roncoo.education.user.dao.TutorRequirementDao;
import com.roncoo.education.user.dao.impl.mapper.entity.Msg;
import com.roncoo.education.user.dao.impl.mapper.entity.MsgUser;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorApplication;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorRequirement;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorRequirementExample;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * admin-需求管理 biz: 日常管理/CRUD/接单确认
 */
@Component
@RequiredArgsConstructor
public class AdminRequirementBiz extends BaseBiz {

    @NotNull
    private final TutorRequirementDao requirementDao;
    @NotNull
    private final TutorApplicationDao applicationDao;
    @NotNull
    private final com.roncoo.education.user.dao.TutorReservationDao reservationDao;
    @NotNull
    private final com.roncoo.education.user.dao.UsersDao usersDao;
    @NotNull
    private final MsgDao msgDao;
    @NotNull
    private final MsgUserDao msgUserDao;

    public Result<?> page(Map<String, Object> req) {
        int pageCurrent = req.get("pageCurrent") != null ? Integer.parseInt(req.get("pageCurrent").toString()) : 1;
        int pageSize = req.get("pageSize") != null ? Integer.parseInt(req.get("pageSize").toString()) : 20;
        TutorRequirementExample example = new TutorRequirementExample();
        TutorRequirementExample.Criteria c = example.createCriteria();
        if (req.get("reqStatus") != null && StringUtils.hasText(req.get("reqStatus").toString())) {
            c.andReqStatusEqualTo(Integer.parseInt(req.get("reqStatus").toString()));
        }
        if (req.get("cityId") != null && StringUtils.hasText(req.get("cityId").toString())) {
            c.andCityIdEqualTo(Long.parseLong(req.get("cityId").toString()));
        }
        if (req.get("districtId") != null && StringUtils.hasText(req.get("districtId").toString())) {
            c.andDistrictIdEqualTo(Long.parseLong(req.get("districtId").toString()));
        }
        if (req.get("displayNo") != null && StringUtils.hasText(req.get("displayNo").toString())) {
            c.andDisplayNoLike(PageUtil.like(req.get("displayNo").toString()));
        }
        if (req.get("keyword") != null && StringUtils.hasText(req.get("keyword").toString())) {
            c.andTitleLike(PageUtil.like(req.get("keyword").toString()));
        }
        example.setOrderByClause("id desc");
        Page<TutorRequirement> page = requirementDao.page(pageCurrent, pageSize, example);
        return Result.success(page);
    }

    public Result<?> view(Long id) {
        TutorRequirement r = requirementDao.getById(id);
        if (r == null) return Result.error("需求不存在");
        return Result.success(r);
    }

    /**
     * admin 代家长录入新需求. 直接置为 PUBLISHED 出现在学员库.
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<?> save(Map<String, Object> req) {
        TutorRequirement r = new TutorRequirement();
        r.setId(IdWorker.getId());
        r.setUserId(0L); // admin 录入
        applyEditableFields(r, req);
        if (r.getReqStatus() == null) {
            r.setReqStatus(RequirementStatusEnum.PUBLISHED.getCode());
        }
        if (r.getDisplayNo() == null) {
            r.setDisplayNo(generateDisplayNo(r.getId()));
        }
        if (r.getViewCount() == null) r.setViewCount(0);
        if (r.getApplicationCount() == null) r.setApplicationCount(0);
        if (r.getTitle() == null && r.getRequirementDetail() != null) {
            String d = r.getRequirementDetail();
            r.setTitle(d.length() > 20 ? d.substring(0, 20) : d);
        }
        requirementDao.save(r);
        Map<String, Object> resp = new HashMap<>();
        resp.put("id", r.getId());
        resp.put("displayNo", r.getDisplayNo());
        return Result.success(resp);
    }

    @Transactional(rollbackFor = Exception.class)
    public Result<?> edit(Map<String, Object> req) {
        if (req.get("id") == null) return Result.error("缺少 id");
        Long id = Long.parseLong(req.get("id").toString());
        TutorRequirement existing = requirementDao.getById(id);
        if (existing == null) return Result.error("需求不存在");

        TutorRequirement update = new TutorRequirement();
        update.setId(id);
        applyEditableFields(update, req);
        requirementDao.updateById(update);
        return Result.success("已保存");
    }

    @Transactional(rollbackFor = Exception.class)
    public Result<?> delete(Long id) {
        TutorRequirement existing = requirementDao.getById(id);
        if (existing == null) return Result.error("需求不存在");
        // 软删除: 把 reqStatus 设成 CLOSED, 不动数据
        TutorRequirement update = new TutorRequirement();
        update.setId(id);
        update.setReqStatus(RequirementStatusEnum.CLOSED.getCode());
        requirementDao.updateById(update);
        return Result.success("已关闭");
    }

    /**
     * admin 与教员/家长电话沟通后确认接单
     * - 把需求 reqStatus 置为 MATCHED
     * - 写入 target_tutor_user_id + matched_at + matched_tutor_remark
     * - 把对应 application 置为 ACCEPTED
     * - 给教员发站内信
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<?> confirmMatch(Map<String, Object> req) {
        if (req.get("requirementId") == null) return Result.error("缺少 requirementId");
        if (req.get("tutorUserId") == null) return Result.error("缺少 tutorUserId");
        Long reqId = Long.parseLong(req.get("requirementId").toString());
        Long tutorUserId = Long.parseLong(req.get("tutorUserId").toString());
        String remark = req.get("remark") != null ? req.get("remark").toString() : null;

        // 校验目标教员存在且类型 = TUTOR
        com.roncoo.education.user.dao.impl.mapper.entity.Users tutor = usersDao.getById(tutorUserId);
        if (tutor == null) return Result.error("教员账号不存在");
        if (!com.roncoo.education.common.core.enums.UserTypeEnum.TUTOR.getCode().equals(tutor.getUserType())) {
            return Result.error("目标用户不是教员账号 (user_type ≠ 1)");
        }

        TutorRequirement r = requirementDao.getById(reqId);
        if (r == null) return Result.error("需求不存在");
        if (RequirementStatusEnum.MATCHED.getCode().equals(r.getReqStatus())) {
            return Result.error("该需求已接单");
        }

        LocalDateTime now = LocalDateTime.now();
        TutorRequirement update = new TutorRequirement();
        update.setId(reqId);
        update.setReqStatus(RequirementStatusEnum.MATCHED.getCode());
        update.setMatchedAt(now);
        update.setTargetTutorUserId(tutorUserId);
        if (remark != null) update.setMatchedTutorRemark(remark);
        requirementDao.updateById(update);

        // 把这个教员对该需求的 application 置成 ACCEPTED (如果存在)
        List<TutorApplication> apps = applicationDao.listByRequirementId(reqId);
        if (apps != null) {
            for (TutorApplication app : apps) {
                if (tutorUserId.equals(app.getUserId())) {
                    TutorApplication appUp = new TutorApplication();
                    appUp.setId(app.getId());
                    appUp.setAppStatus(ApplicationStatusEnum.ACCEPTED.getCode());
                    applicationDao.updateById(appUp);
                } else if (ApplicationStatusEnum.APPLIED.getCode().equals(app.getAppStatus())) {
                    // 其他申请者置为已拒绝
                    TutorApplication appUp = new TutorApplication();
                    appUp.setId(app.getId());
                    appUp.setAppStatus(ApplicationStatusEnum.REJECTED.getCode());
                    applicationDao.updateById(appUp);
                }
            }
        }

        // 同步关联的 reservation (如果存在): 学员从 /reserve 走过来的请求, requirement.id 就是 reservation.requirement_id
        com.roncoo.education.user.dao.impl.mapper.entity.TutorReservationExample resExample =
                new com.roncoo.education.user.dao.impl.mapper.entity.TutorReservationExample();
        resExample.createCriteria().andRequirementIdEqualTo(reqId);
        com.roncoo.education.common.base.page.Page<com.roncoo.education.user.dao.impl.mapper.entity.TutorReservation> resPage =
                reservationDao.page(1, 10, resExample);
        if (resPage != null && resPage.getList() != null) {
            for (com.roncoo.education.user.dao.impl.mapper.entity.TutorReservation res : resPage.getList()) {
                com.roncoo.education.user.dao.impl.mapper.entity.TutorReservation resUp =
                        new com.roncoo.education.user.dao.impl.mapper.entity.TutorReservation();
                resUp.setId(res.getId());
                // res_status: 2 = COMPLETED (per ReservationStatusEnum, 已完成)
                resUp.setResStatus(2);
                resUp.setMatchedAt(now);
                reservationDao.updateById(resUp);
            }
        }

        // 通知接单教员
        String reqTitle = r.getTitle() != null ? r.getTitle() : "需求";
        sendMsg(tutorUserId, "接单确认",
                "您已成功接单《" + reqTitle + "》。请联系客服 或 等候客服将家长联系方式发给您。");

        return Result.success("已确认接单");
    }

    /**
     * 通用字段写入: 把 req map 中的字段拷贝到 entity. 只覆盖 admin 可编辑的字段.
     */
    private void applyEditableFields(TutorRequirement r, Map<String, Object> req) {
        // 标题 / 联系信息
        if (req.containsKey("title")) r.setTitle(strOrNull(req.get("title")));
        if (req.containsKey("contactName")) r.setContactName(strOrNull(req.get("contactName")));
        if (req.containsKey("contactMobile")) r.setContactMobile(strOrNull(req.get("contactMobile")));
        if (req.containsKey("contactWechat")) r.setContactWechat(strOrNull(req.get("contactWechat")));

        // 学员
        if (req.containsKey("studentGender")) r.setStudentGender(intOrNull(req.get("studentGender")));
        if (req.containsKey("gradeName")) r.setGradeName(strOrNull(req.get("gradeName")));

        // 地理
        if (req.containsKey("cityId")) r.setCityId(longOrNull(req.get("cityId")));
        if (req.containsKey("districtNames")) r.setDistrictNames(strOrNull(req.get("districtNames")));
        if (req.containsKey("address")) r.setAddress(strOrNull(req.get("address")));
        if (req.containsKey("transport")) r.setTransport(strOrNull(req.get("transport")));

        // 家教内容
        if (req.containsKey("subjectIds")) r.setSubjectIds(strOrNull(req.get("subjectIds")));
        if (req.containsKey("requirementDetail")) r.setRequirementDetail(strOrNull(req.get("requirementDetail")));
        if (req.containsKey("frequency")) r.setFrequency(strOrNull(req.get("frequency")));
        if (req.containsKey("schedule")) r.setSchedule(strOrNull(req.get("schedule")));
        if (req.containsKey("requirementType")) r.setRequirementType(intOrNull(req.get("requirementType")));
        if (req.containsKey("tutorGender")) r.setTutorGender(intOrNull(req.get("tutorGender")));
        if (req.containsKey("tutorTypePref")) r.setTutorTypePref(strOrNull(req.get("tutorTypePref")));
        if (req.containsKey("otherRequirements")) r.setOtherRequirements(strOrNull(req.get("otherRequirements")));
        if (req.containsKey("budgetMin")) r.setBudgetMin(decimalOrNull(req.get("budgetMin")));
        if (req.containsKey("budgetMax")) r.setBudgetMax(decimalOrNull(req.get("budgetMax")));
        if (req.containsKey("teachingMethod")) r.setTeachingMethod(intOrNull(req.get("teachingMethod")));
        if (req.containsKey("transportSubsidy")) r.setTransportSubsidy(strOrNull(req.get("transportSubsidy")));

        // 状态: 仅允许 草稿(0) / 审核中(1) / 已发布(2) / 已关闭(5);
        // 已匹配(3) 必须走 confirmMatch, 已驳回(6) 走 audit/reject
        if (req.containsKey("reqStatus")) {
            Integer st = intOrNull(req.get("reqStatus"));
            if (st != null && (st == 0 || st == 1 || st == 2 || st == 5)) {
                r.setReqStatus(st);
            }
        }
        // 加急标记
        if (req.containsKey("isUrgent")) r.setIsUrgent(intOrNull(req.get("isUrgent")));
    }

    private static String strOrNull(Object v) {
        if (v == null) return null;
        String s = v.toString().trim();
        return s.isEmpty() ? null : s;
    }
    private static Integer intOrNull(Object v) {
        if (v == null || v.toString().isEmpty()) return null;
        return Integer.parseInt(v.toString());
    }
    private static Long longOrNull(Object v) {
        if (v == null || v.toString().isEmpty()) return null;
        return Long.parseLong(v.toString());
    }
    private static BigDecimal decimalOrNull(Object v) {
        if (v == null || v.toString().isEmpty()) return null;
        return new BigDecimal(v.toString());
    }

    private static String generateDisplayNo(Long id) {
        // A + 6 位数字, 取雪花 id 后 6 位再做 padding
        long abs = Math.abs(id);
        return "S" + (100000 + abs % 900000);
    }

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
