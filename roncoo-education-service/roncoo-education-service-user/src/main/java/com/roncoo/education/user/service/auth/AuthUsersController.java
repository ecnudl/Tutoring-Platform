package com.roncoo.education.user.service.auth;

import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.user.service.auth.biz.AuthUsersBiz;
import com.roncoo.education.user.service.auth.req.AuthBindingReq;
import com.roncoo.education.user.service.auth.req.AuthUsersReq;
import com.roncoo.education.user.service.auth.resp.AuthUsersResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import jakarta.validation.constraints.NotNull;

/**
 * AUTH-用户信息
 *
 * @author wujing
 * @date 2022-08-25
 */
@Api(tags = "auth-用户信息")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/auth/users")
public class AuthUsersController {

    @NotNull
    private final AuthUsersBiz biz;

    /**
     * 用户信息查看接口
     */
    @ApiOperation(value = "查看接口", notes = "获取当前用户的基本信息")
    @GetMapping(value = "/view")
    public Result<AuthUsersResp> view() {
        return biz.view();
    }

    @ApiOperation(value = "更新接口", notes = "更新当前用户的基本信息")
    @PostMapping(value = "/edit")
    public Result<String> update(@RequestBody @Valid AuthUsersReq req) {
        return biz.update(req);
    }

    @ApiOperation(value = "更新头像", notes = "仅更新 userHead 字段")
    @PostMapping(value = "/avatar")
    public Result<String> updateAvatar(@RequestBody java.util.Map<String, Object> req) {
        Object url = req.get("userHead");
        if (url == null || url.toString().isEmpty()) return Result.error("头像 URL 不能为空");
        return biz.updateAvatar(url.toString());
    }

    @ApiOperation(value = "修改密码", notes = "已登录用户用原密码修改为新密码 {oldPassword, newPassword}")
    @PostMapping(value = "/change-password")
    public Result<String> changePassword(@RequestBody java.util.Map<String, Object> req) {
        return biz.changePassword(req);
    }

    @ApiOperation(value = "绑定接口", notes = "绑定微信")
    @PostMapping(value = "/binding")
    public Result<String> binding(@RequestBody AuthBindingReq req) throws WxErrorException {
        return biz.binding(req);
    }

    @ApiOperation(value = "解绑接口", notes = "解绑微信")
    @GetMapping(value = "/unbind")
    public Result<String> unbind() {
        return biz.unbind();
    }

}
