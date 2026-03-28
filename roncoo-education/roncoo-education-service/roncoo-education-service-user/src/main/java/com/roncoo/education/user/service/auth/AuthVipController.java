package com.roncoo.education.user.service.auth;

import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.user.service.auth.biz.AuthVipBiz;
import com.roncoo.education.user.service.auth.resp.AuthVipResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.NotNull;

@Api(tags = "auth-VIP")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/auth/vip")
public class AuthVipController {
    @NotNull
    private final AuthVipBiz biz;

    @ApiOperation(value = "VIP状态") @GetMapping("/status")
    public Result<?> status() { return biz.status(); }
}
