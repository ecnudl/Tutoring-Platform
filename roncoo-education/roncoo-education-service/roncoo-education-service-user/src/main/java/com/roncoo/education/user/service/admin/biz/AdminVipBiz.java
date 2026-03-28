package com.roncoo.education.user.service.admin.biz;

import com.roncoo.education.common.base.BaseBiz;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.user.dao.VipMembershipDao;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminVipBiz extends BaseBiz {
    @NotNull
    private final VipMembershipDao vipMembershipDao;

    public Result<?> page(java.util.Map<String, Object> req) { return Result.success("操作成功"); }
    public Result<?> view(Long id) { return Result.success(vipMembershipDao.getById(id)); }
    public Result<String> grant(java.util.Map<String, Object> req) { return Result.success("操作成功"); }
    public Result<String> revoke(Long id) { return Result.success("操作成功"); }
}
