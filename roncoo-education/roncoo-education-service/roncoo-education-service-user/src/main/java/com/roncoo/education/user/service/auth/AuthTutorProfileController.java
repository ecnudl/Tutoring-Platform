package com.roncoo.education.user.service.auth;

import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.user.service.auth.biz.AuthTutorProfileBiz;
import com.roncoo.education.user.service.auth.req.AuthTutorProfileSaveReq;
import com.roncoo.education.user.service.auth.req.AuthCertSaveReq;
import com.roncoo.education.user.service.auth.resp.AuthTutorProfileResp;
import com.roncoo.education.user.service.auth.resp.AuthCertResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Api(tags = "auth-教员资料")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/auth/tutor-profile")
public class AuthTutorProfileController {
    @NotNull
    private final AuthTutorProfileBiz biz;

    @ApiOperation(value = "查看教员资料") @GetMapping("/view")
    public Result<?> view() { return biz.view(); }
    @ApiOperation(value = "保存教员资料") @PostMapping("/save")
    public Result<String> save(@RequestBody AuthTutorProfileSaveReq req) { return biz.save(req); }
    @ApiOperation(value = "提交审核") @PostMapping("/submit-audit")
    public Result<String> submitAudit() { return biz.submitAudit(); }
    @ApiOperation(value = "保存证书") @PostMapping("/cert/save")
    public Result<String> certSave(@RequestBody AuthCertSaveReq req) { return biz.certSave(req); }
    @ApiOperation(value = "删除证书") @DeleteMapping("/cert/delete")
    public Result<String> certDelete(@RequestParam Long id) { return biz.certDelete(id); }
    @ApiOperation(value = "证书列表") @GetMapping("/cert/list")
    public Result<?> certList() { return biz.certList(); }
}
