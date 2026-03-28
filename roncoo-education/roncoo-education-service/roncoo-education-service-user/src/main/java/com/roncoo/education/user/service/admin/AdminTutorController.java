package com.roncoo.education.user.service.admin;

import com.roncoo.education.common.log.SysLog;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.user.service.admin.biz.AdminTutorBiz;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.NotNull;

@Api(tags = "admin-教员管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/admin/tutor")
public class AdminTutorController {
    @NotNull
    private final AdminTutorBiz biz;

    @ApiOperation(value = "分页") @PostMapping("/page")
    public Result<?> page(@RequestBody java.util.Map<String, Object> req) { return biz.page(req); }
    @ApiOperation(value = "查看") @GetMapping("/view")
    public Result<?> view(@RequestParam Long id) { return biz.view(id); }
    @SysLog(value = "修改") @ApiOperation(value = "修改") @PutMapping("/edit")
    public Result<String> edit(@RequestBody java.util.Map<String, Object> req) { return biz.edit(req); }
    @SysLog(value = "删除") @ApiOperation(value = "删除") @DeleteMapping("/delete")
    public Result<String> delete(@RequestParam Long id) { return biz.delete(id); }
}
