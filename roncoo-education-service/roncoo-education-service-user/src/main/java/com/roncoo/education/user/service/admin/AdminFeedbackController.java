package com.roncoo.education.user.service.admin;

import com.roncoo.education.common.log.SysLog;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.user.service.admin.biz.AdminFeedbackBiz;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.NotNull;

@Api(tags = "admin-感言/反馈管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/admin/feedback")
public class AdminFeedbackController {
    @NotNull
    private final AdminFeedbackBiz biz;

    @ApiOperation(value = "分页") @PostMapping("/page")
    public Result<?> page(@RequestBody java.util.Map<String, Object> req) { return biz.page(req); }

    @ApiOperation(value = "查看") @GetMapping("/view")
    public Result<?> view(@RequestParam Long id) { return biz.view(id); }

    @SysLog(value = "回复") @ApiOperation(value = "回复反馈") @PutMapping("/reply")
    public Result<String> reply(@RequestBody java.util.Map<String, Object> req) { return biz.reply(req); }

    @SysLog(value = "通过") @ApiOperation(value = "审核通过") @PutMapping("/audit-pass")
    public Result<String> auditPass(@RequestBody java.util.Map<String, Object> req) { return biz.auditPass(req); }

    @SysLog(value = "驳回") @ApiOperation(value = "审核驳回") @PutMapping("/audit-reject")
    public Result<String> auditReject(@RequestBody java.util.Map<String, Object> req) { return biz.auditReject(req); }
}
