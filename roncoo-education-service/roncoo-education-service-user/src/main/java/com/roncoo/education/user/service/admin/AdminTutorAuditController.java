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
    @ApiOperation(value = "教员证书列表") @GetMapping("/cert/list")
    public Result<?> certList(@RequestParam Long tutorId) { return biz.certList(tutorId); }
    @SysLog(value = "证件审核通过") @ApiOperation(value = "证件审核通过") @PutMapping("/cert/approve")
    public Result<String> certApprove(@RequestBody java.util.Map<String, Object> req) { return biz.certApprove(req); }
    @SysLog(value = "证件审核驳回") @ApiOperation(value = "证件审核驳回") @PutMapping("/cert/reject")
    public Result<String> certReject(@RequestBody java.util.Map<String, Object> req) { return biz.certReject(req); }
    @SysLog(value = "设置/取消明星教员") @ApiOperation(value = "设置/取消明星教员") @PutMapping("/star")
    public Result<String> setStar(@RequestBody java.util.Map<String, Object> req) { return biz.setStar(req); }

    @ApiOperation(value = "近期被编辑的教员资料") @PostMapping("/recent-edited")
    public Result<?> recentEdited(@RequestBody java.util.Map<String, Object> req) { return biz.recentEditedProfiles(req); }
}
