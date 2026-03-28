package com.roncoo.education.user.service.admin;

import com.roncoo.education.common.log.SysLog;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.user.service.admin.biz.AdminDictTagBiz;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.NotNull;

@Api(tags = "admin-标签字典")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/admin/dict-tag")
public class AdminDictTagController {
    @NotNull
    private final AdminDictTagBiz biz;

    @ApiOperation(value = "分页") @PostMapping("/page")
    public Result<?> page(@RequestBody java.util.Map<String, Object> req) { return biz.page(req); }
    @SysLog(value = "添加") @ApiOperation(value = "添加") @PostMapping("/save")
    public Result<String> save(@RequestBody java.util.Map<String, Object> req) { return biz.save(req); }
    @SysLog(value = "修改") @ApiOperation(value = "修改") @PutMapping("/edit")
    public Result<String> edit(@RequestBody java.util.Map<String, Object> req) { return biz.edit(req); }
    @SysLog(value = "删除") @ApiOperation(value = "删除") @DeleteMapping("/delete")
    public Result<String> delete(@RequestParam Long id) { return biz.delete(id); }
}
