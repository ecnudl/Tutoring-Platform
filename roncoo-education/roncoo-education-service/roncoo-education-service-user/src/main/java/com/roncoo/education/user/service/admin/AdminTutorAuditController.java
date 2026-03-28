package com.roncoo.education.user.service.admin;

import com.roncoo.education.common.log.SysLog;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.user.service.admin.biz.AdminTutorAuditBiz;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.NotNull;

@Api(tags = "admin-教员审核")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/admin/tutor-audit")
public class AdminTutorAuditController {
    @NotNull
    private final AdminTutorAuditBiz biz;

    @ApiOperation(value = "分页") @PostMapping("/page")
    public Result<?> page(@RequestBody java.util.Map<String, Object> req) { return biz.page(req); }
    @ApiOperation(value = "查看") @GetMapping("/view")
    public Result<?> view(@RequestParam Long id) { return biz.view(id); }
    @SysLog(value = "审核通过") @ApiOperation(value = "审核通过") @PutMapping("/approve")
    public Result<String> approve(@RequestBody java.util.Map<String, Object> req) { return biz.approve(req); }
    @SysLog(value = "审核拒绝") @ApiOperation(value = "审核拒绝") @PutMapping("/reject")
    public Result<String> reject(@RequestBody java.util.Map<String, Object> req) { return biz.reject(req); }
}
