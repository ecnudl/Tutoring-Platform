package com.roncoo.education.user.service.auth;

import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.user.service.auth.biz.AuthMsgUserBiz;
import com.roncoo.education.user.service.auth.req.AuthMsgUserGetReq;
import com.roncoo.education.user.service.auth.req.AuthMsgUserPageReq;
import com.roncoo.education.user.service.auth.resp.AuthMsgResp;
import com.roncoo.education.user.service.auth.resp.AuthMsgUserResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.NotNull;

@Api(tags = "auth-站内信用户记录表")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/auth/msg/user")
public class AuthMsgUserController {

    @NotNull
    private final AuthMsgUserBiz biz;

    @ApiOperation(value = "学员站内信分页列表接口")
    @PostMapping("/list")
    Result<Page<AuthMsgUserResp>> list(@RequestBody AuthMsgUserPageReq req) {
        return biz.list(req);
    }

    @ApiOperation(value = "用户查看站内信")
    @PostMapping("/read")
    Result<AuthMsgResp> readMsg(@RequestBody AuthMsgUserGetReq req) {
        return biz.readMsg(req);
    }

    @ApiOperation(value = "未读站内信数量(用于角标)")
    @GetMapping("/unread-count")
    Result<Long> unreadCount() {
        return biz.unreadCount();
    }

    @ApiOperation(value = "全部标记已读")
    @PostMapping("/mark-all-read")
    Result<String> markAllRead() {
        return biz.markAllRead();
    }
}
