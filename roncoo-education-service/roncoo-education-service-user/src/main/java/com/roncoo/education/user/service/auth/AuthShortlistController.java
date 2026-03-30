package com.roncoo.education.user.service.auth;

import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.user.service.auth.biz.AuthShortlistBiz;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.NotNull;

@Api(tags = "auth-备选管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/auth/shortlist")
public class AuthShortlistController {
    @NotNull
    private final AuthShortlistBiz biz;

    @ApiOperation(value = "备选列表") @PostMapping("/page")
    public Result<?> page(@RequestBody java.util.Map<String, Object> req) { return biz.page(req); }
    @ApiOperation(value = "添加备选") @PostMapping("/add")
    public Result<String> add(@RequestBody java.util.Map<String, Object> req) { return biz.add(req); }
    @ApiOperation(value = "移除备选") @DeleteMapping("/remove")
    public Result<String> remove(@RequestParam Long id) { return biz.remove(id); }
}
