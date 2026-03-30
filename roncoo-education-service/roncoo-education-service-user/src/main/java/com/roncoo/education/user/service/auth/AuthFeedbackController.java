package com.roncoo.education.user.service.auth;

import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.user.service.auth.biz.AuthFeedbackBiz;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.NotNull;

@Api(tags = "auth-意见反馈")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/auth/feedback")
public class AuthFeedbackController {
    @NotNull
    private final AuthFeedbackBiz biz;

    @ApiOperation(value = "反馈列表") @PostMapping("/page")
    public Result<?> page(@RequestBody java.util.Map<String, Object> req) { return biz.page(req); }
    @ApiOperation(value = "提交反馈") @PostMapping("/submit")
    public Result<String> submit(@RequestBody java.util.Map<String, Object> req) { return biz.submit(req); }
}
