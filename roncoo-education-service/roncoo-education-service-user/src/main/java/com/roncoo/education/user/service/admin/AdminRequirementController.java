package com.roncoo.education.user.service.admin;

import com.roncoo.education.common.log.SysLog;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.user.service.admin.biz.AdminRequirementBiz;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * admin-需求管理 (CRUD, 与 requirement-audit 区分: 这里负责日常管理与编辑发布)
 */
@Api(tags = "admin-需求管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/admin/requirement")
public class AdminRequirementController {

    @NotNull
    private final AdminRequirementBiz biz;

    @ApiOperation(value = "分页") @PostMapping("/page")
    public Result<?> page(@RequestBody Map<String, Object> req) { return biz.page(req); }

    @ApiOperation(value = "查看") @GetMapping("/view")
    public Result<?> view(@RequestParam Long id) { return biz.view(id); }

    @SysLog(value = "新增需求") @ApiOperation(value = "新增需求 (admin 代家长录入)")
    @PostMapping("/save")
    public Result<?> save(@RequestBody Map<String, Object> req) { return biz.save(req); }

    @SysLog(value = "编辑需求") @ApiOperation(value = "编辑需求")
    @PutMapping("/edit")
    public Result<?> edit(@RequestBody Map<String, Object> req) { return biz.edit(req); }

    @SysLog(value = "删除需求") @ApiOperation(value = "删除需求")
    @DeleteMapping("/delete")
    public Result<?> delete(@RequestParam Long id) { return biz.delete(id); }

    @SysLog(value = "确认接单") @ApiOperation(value = "确认接单 (admin 双方电话沟通后)")
    @PutMapping("/confirm-match")
    public Result<?> confirmMatch(@RequestBody Map<String, Object> req) { return biz.confirmMatch(req); }
}
