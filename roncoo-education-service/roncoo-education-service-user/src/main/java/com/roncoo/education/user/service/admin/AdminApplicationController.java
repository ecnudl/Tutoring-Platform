package com.roncoo.education.user.service.admin;

import com.roncoo.education.common.log.SysLog;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.user.service.admin.biz.AdminApplicationBiz;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.NotNull;

@Api(tags = "admin-申请管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/admin/application")
public class AdminApplicationController {
    @NotNull
    private final AdminApplicationBiz biz;

    @ApiOperation(value = "分页") @PostMapping("/page")
    public Result<?> page(@RequestBody java.util.Map<String, Object> req) { return biz.page(req); }
    @ApiOperation(value = "查看") @GetMapping("/view")
    public Result<?> view(@RequestParam Long id) { return biz.view(id); }
    @SysLog(value = "申请-匹配") @ApiOperation(value = "匹配申请 (撮合)") @PutMapping("/match")
    public Result<String> match(@RequestParam Long id) { return biz.match(id); }
    @SysLog(value = "申请-驳回") @ApiOperation(value = "驳回申请") @PutMapping("/reject")
    public Result<String> reject(@RequestParam Long id) { return biz.reject(id); }
}
