package com.roncoo.education.user.service.admin;

import com.roncoo.education.common.log.SysLog;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.user.service.admin.biz.AdminReservationBiz;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.NotNull;

@Api(tags = "admin-学员预约")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/admin/reservation")
public class AdminReservationController {
    @NotNull
    private final AdminReservationBiz biz;

    @ApiOperation(value = "分页") @PostMapping("/page")
    public Result<?> page(@RequestBody java.util.Map<String, Object> req) { return biz.page(req); }

    @ApiOperation(value = "查看") @GetMapping("/view")
    public Result<?> view(@RequestParam Long id) { return biz.view(id); }

    @ApiOperation(value = "待处理统计") @GetMapping("/pending-count")
    public Result<Long> pendingCount() { return biz.pendingCount(); }

    @SysLog(value="确认匹配") @ApiOperation(value="确认匹配(双方都同意, 已收中介费)")
    @PutMapping("/match")
    public Result<String> match(@RequestBody java.util.Map<String, Object> req) { return biz.match(req); }

    @SysLog(value="驳回预约") @ApiOperation(value="驳回预约")
    @PutMapping("/reject")
    public Result<String> reject(@RequestBody java.util.Map<String, Object> req) { return biz.reject(req); }

    @SysLog(value="转入公开需求池") @ApiOperation(value="目标教员未接, 把关联需求推到 PUBLISHED")
    @PutMapping("/publish-requirement")
    public Result<String> publishRequirement(@RequestBody java.util.Map<String, Object> req) { return biz.publishRequirement(req); }
}
