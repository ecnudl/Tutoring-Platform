package com.roncoo.education.user.service.auth;

import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.user.service.auth.biz.AuthStudentProfileBiz;
import com.roncoo.education.user.service.auth.req.AuthStudentProfileSaveReq;
import com.roncoo.education.user.service.auth.resp.AuthStudentProfileResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.NotNull;

@Api(tags = "auth-学员资料")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/auth/student-profile")
public class AuthStudentProfileController {
    @NotNull
    private final AuthStudentProfileBiz biz;

    @ApiOperation(value = "查看学员资料") @GetMapping("/view")
    public Result<?> view() { return biz.view(); }
    @ApiOperation(value = "保存学员资料") @PostMapping("/save")
    public Result<String> save(@RequestBody AuthStudentProfileSaveReq req) { return biz.save(req); }
}
