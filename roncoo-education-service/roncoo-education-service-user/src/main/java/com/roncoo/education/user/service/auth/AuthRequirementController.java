package com.roncoo.education.user.service.auth;

import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.user.service.auth.biz.AuthRequirementBiz;
import com.roncoo.education.user.service.auth.req.AuthRequirementSaveReq;
import com.roncoo.education.user.service.auth.req.AuthRequirementPageReq;
import com.roncoo.education.user.service.auth.resp.AuthRequirementResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.NotNull;

@Api(tags = "auth-需求管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/auth/requirement")
public class AuthRequirementController {
    @NotNull
    private final AuthRequirementBiz biz;

    @ApiOperation(value = "我的需求列表") @PostMapping("/page")
    public Result<?> page(@RequestBody AuthRequirementPageReq req) { return biz.page(req); }
    @ApiOperation(value = "保存需求") @PostMapping("/save")
    public Result<String> save(@RequestBody AuthRequirementSaveReq req) { return biz.save(req); }
    @ApiOperation(value = "提交审核") @PostMapping("/submit-audit")
    public Result<String> submitAudit(@RequestParam Long id) { return biz.submitAudit(id); }
    @ApiOperation(value = "需求详情") @GetMapping("/view")
    public Result<?> view(@RequestParam Long id) { return biz.view(id); }
    @ApiOperation(value = "删除需求") @DeleteMapping("/delete")
    public Result<String> delete(@RequestParam Long id) { return biz.delete(id); }
    @ApiOperation(value = "关闭需求") @PutMapping("/close")
    public Result<String> close(@RequestParam Long id) { return biz.close(id); }
}
