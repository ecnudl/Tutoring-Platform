package com.roncoo.education.user.service.admin;

import com.roncoo.education.common.log.SysLog;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.user.service.admin.biz.AdminVipBiz;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.NotNull;

@Api(tags = "admin-VIP管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/admin/vip")
public class AdminVipController {
    @NotNull
    private final AdminVipBiz biz;

    @ApiOperation(value = "分页") @PostMapping("/page")
    public Result<?> page(@RequestBody java.util.Map<String, Object> req) { return biz.page(req); }
    @SysLog(value = "开通VIP") @ApiOperation(value = "开通VIP") @PostMapping("/grant")
    public Result<String> grant(@RequestBody java.util.Map<String, Object> req) { return biz.grant(req); }
    @SysLog(value = "关闭VIP") @ApiOperation(value = "关闭VIP") @PutMapping("/revoke")
    public Result<String> revoke(@RequestParam Long id) { return biz.revoke(id); }
}
