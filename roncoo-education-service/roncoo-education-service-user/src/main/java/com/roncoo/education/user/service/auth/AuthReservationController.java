package com.roncoo.education.user.service.auth;

import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.user.service.auth.biz.AuthReservationBiz;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.NotNull;

@Api(tags = "auth-预约管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/auth/reservation")
public class AuthReservationController {
    @NotNull
    private final AuthReservationBiz biz;

    @ApiOperation(value = "我的预约") @PostMapping("/page")
    public Result<?> page(@RequestBody java.util.Map<String, Object> req) { return biz.page(req); }
    @ApiOperation(value = "创建预约") @PostMapping("/create")
    public Result<String> create(@RequestBody java.util.Map<String, Object> req) { return biz.create(req); }
    @ApiOperation(value = "确认预约") @PutMapping("/confirm")
    public Result<String> confirm(@RequestParam Long id) { return biz.confirm(id); }
    @ApiOperation(value = "完成预约") @PutMapping("/complete")
    public Result<String> complete(@RequestParam Long id) { return biz.complete(id); }
    @ApiOperation(value = "取消预约") @PutMapping("/cancel")
    public Result<String> cancel(@RequestParam Long id, @RequestParam(required = false) String reason) { return biz.cancel(id, reason); }
}
