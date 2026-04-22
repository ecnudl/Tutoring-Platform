package com.roncoo.education.user.service.admin;

import com.roncoo.education.common.log.SysLog;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.user.service.admin.biz.AdminTutorAuditBiz;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

@Api(tags = "admin-证件审核")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/admin/cert")
public class AdminCertController {
    @NotNull
    private final AdminTutorAuditBiz biz;

    @ApiOperation(value = "分页 - 所有教员的证件") @PostMapping("/page")
    public Result<?> page(@RequestBody Map<String, Object> req) { return biz.certPage(req); }

    @SysLog(value = "证件审核通过") @ApiOperation(value = "证件审核通过") @PutMapping("/approve")
    public Result<String> approve(@RequestBody Map<String, Object> req) { return biz.certApprove(req); }

    @SysLog(value = "证件审核驳回") @ApiOperation(value = "证件审核驳回") @PutMapping("/reject")
    public Result<String> reject(@RequestBody Map<String, Object> req) { return biz.certReject(req); }
}
