package com.roncoo.education.user.service.auth;

import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.user.service.auth.biz.AuthFootprintBiz;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.NotNull;

@Api(tags = "auth-浏览足迹")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/auth/footprint")
public class AuthFootprintController {
    @NotNull
    private final AuthFootprintBiz biz;

    @ApiOperation(value = "足迹列表") @PostMapping("/page")
    public Result<?> page(@RequestBody java.util.Map<String, Object> req) { return biz.page(req); }
    @ApiOperation(value = "清空足迹") @DeleteMapping("/clear")
    public Result<String> clear() { return biz.clear(); }
}
