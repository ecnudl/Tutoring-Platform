package com.roncoo.education.system.service.admin;

import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.system.service.admin.biz.AdminLoginBiz;
import com.roncoo.education.system.service.admin.req.AdminSysUserLoginReq;
import com.roncoo.education.system.service.admin.resp.AdminSysUserLoginResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

/**
 * @author fengyw
 */
@Api(tags = "admin-登录接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/admin/login")
public class AdminLoginController {

    private final AdminLoginBiz biz;

    @ApiOperation(value = "密码登录", notes = "用户使用密码登录")
    @PostMapping(value = "/password")
    public Result<AdminSysUserLoginResp> login(@RequestBody @Valid AdminSysUserLoginReq req) {
        return biz.login(req);
    }

    @ApiOperation(value = "简化登录", notes = "开发环境，明文密码，无需验证码")
    @PostMapping(value = "/simple")
    public Result<AdminSysUserLoginResp> loginSimple(@RequestBody Map<String, String> req) {
        return biz.loginSimple(req.get("mobile"), req.get("password"));
    }

}
