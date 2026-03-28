package com.roncoo.education.user.service.auth;

import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.user.service.auth.biz.AuthApplicationBiz;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.NotNull;

@Api(tags = "auth-申请管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/auth/application")
public class AuthApplicationController {
    @NotNull
    private final AuthApplicationBiz biz;

    @ApiOperation(value = "我的申请列表") @PostMapping("/page")
    public Result<?> page(@RequestBody java.util.Map<String, Object> req) { return biz.page(req); }
    @ApiOperation(value = "申请需求") @PostMapping("/apply")
    public Result<String> apply(@RequestBody java.util.Map<String, Object> req) { return biz.apply(req); }
    @ApiOperation(value = "学员查看收到的申请") @PostMapping("/received")
    public Result<?> received(@RequestBody java.util.Map<String, Object> req) { return biz.received(req); }
    @ApiOperation(value = "取消申请") @DeleteMapping("/cancel")
    public Result<String> cancel(@RequestParam Long id) { return biz.cancel(id); }
}
