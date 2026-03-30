package com.roncoo.education.user.service.admin.biz;

import com.roncoo.education.common.base.BaseBiz;
import com.roncoo.education.common.base.ThreadContext;
import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.common.tools.IdWorker;
import com.roncoo.education.user.dao.VipMembershipDao;
import com.roncoo.education.user.dao.impl.mapper.entity.VipMembership;
import com.roncoo.education.user.dao.impl.mapper.entity.VipMembershipExample;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AdminVipBiz extends BaseBiz {
    @NotNull
    private final VipMembershipDao vipMembershipDao;

    public Result<?> page(Map<String, Object> req) {
        int pageCurrent = req.get("pageCurrent") != null ? Integer.parseInt(req.get("pageCurrent").toString()) : 1;
        int pageSize = req.get("pageSize") != null ? Integer.parseInt(req.get("pageSize").toString()) : 20;
        Integer vipLevel = req.get("vipLevel") != null ? Integer.parseInt(req.get("vipLevel").toString()) : null;

        VipMembershipExample example = new VipMembershipExample();
        VipMembershipExample.Criteria c = example.createCriteria();
        if (vipLevel != null) {
            c.andVipLevelEqualTo(vipLevel);
        }
        example.setOrderByClause("gmt_create desc");
        Page<VipMembership> page = vipMembershipDao.page(pageCurrent, pageSize, example);
        return Result.success(page);
    }

    public Result<?> view(Long id) {
        VipMembership vip = vipMembershipDao.getById(id);
        if (vip == null) {
            return Result.error("VIP记录不存在");
        }
        return Result.success(vip);
    }

    public Result<String> grant(Map<String, Object> req) {
        Long userId = Long.parseLong(req.get("userId").toString());
        Integer vipLevel = req.get("vipLevel") != null ? Integer.parseInt(req.get("vipLevel").toString()) : 1;
        Integer days = req.get("days") != null ? Integer.parseInt(req.get("days").toString()) : 30;
        String remark = req.get("remark") != null ? req.get("remark").toString() : "";

        // 检查是否已有生效中的VIP
        VipMembership existing = vipMembershipDao.getByUserId(userId);
        if (existing != null && existing.getEndTime() != null && existing.getEndTime().isAfter(LocalDateTime.now())) {
            // 续期：在现有到期时间基础上加天数
            VipMembership update = new VipMembership();
            update.setId(existing.getId());
            update.setEndTime(existing.getEndTime().plusDays(days));
            update.setVipLevel(vipLevel);
            update.setRemark(remark);
            try { update.setOperatorId(ThreadContext.userId()); } catch (Exception e) { update.setOperatorId(0L); }
            vipMembershipDao.updateById(update);
            return Result.success("续期成功");
        }

        VipMembership vip = new VipMembership();
        vip.setId(IdWorker.getId());
        vip.setUserId(userId);
        vip.setVipLevel(vipLevel);
        vip.setStartTime(LocalDateTime.now());
        vip.setEndTime(LocalDateTime.now().plusDays(days));
        vip.setStatusId(1);
        vip.setRemark(remark);
        try { vip.setOperatorId(ThreadContext.userId()); } catch (Exception e) { vip.setOperatorId(0L); }
        vipMembershipDao.save(vip);
        return Result.success("开通成功");
    }

    public Result<String> revoke(Long id) {
        VipMembership vip = vipMembershipDao.getById(id);
        if (vip == null) {
            return Result.error("VIP记录不存在");
        }
        // 设置到期时间为当前时间（立即失效）
        VipMembership update = new VipMembership();
        update.setId(id);
        update.setEndTime(LocalDateTime.now());
        update.setStatusId(0);
        vipMembershipDao.updateById(update);
        return Result.success("撤销成功");
    }
}
