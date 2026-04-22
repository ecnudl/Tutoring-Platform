package com.roncoo.education.user.service.api;

import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.user.service.api.biz.ApiUsersBiz;
import com.roncoo.education.user.service.api.resp.UsersLoginResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 短信登录 / 找回密码
 */
@Api(tags = "api-短信登录与找回密码")
@RestController
@RequestMapping("/user/api")
@RequiredArgsConstructor
public class ApiAuthController {

    private final ApiUsersBiz biz;

    @ApiOperation(value = "短信验证码登录", notes = "校验短信验证码后免密登录")
    @PostMapping("/login/sms")
    public Result<UsersLoginResp> loginSms(@RequestBody Map<String, String> req) {
        Integer expectedType = null; try { if (req.get("userType") != null) expectedType = Integer.parseInt(String.valueOf(req.get("userType"))); } catch (Exception ignored) {} return biz.loginBySms(req.get("mobile"), req.get("code"), expectedType);
    }

    @ApiOperation(value = "找回密码", notes = "校验短信验证码后重置密码")
    @PostMapping("/password/reset")
    public Result<String> passwordReset(@RequestBody Map<String, String> req) {
        return biz.resetPasswordBySms(req.get("mobile"), req.get("code"), req.get("password"));
    }
}
