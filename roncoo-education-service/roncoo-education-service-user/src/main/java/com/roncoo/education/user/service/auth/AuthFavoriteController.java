package com.roncoo.education.user.service.auth;

import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.user.service.auth.biz.AuthFavoriteBiz;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.NotNull;

@Api(tags = "auth-收藏管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/auth/favorite")
public class AuthFavoriteController {
    @NotNull
    private final AuthFavoriteBiz biz;

    @ApiOperation(value = "收藏列表") @PostMapping("/page")
    public Result<?> page(@RequestBody java.util.Map<String, Object> req) { return biz.page(req); }
    @ApiOperation(value = "添加收藏") @PostMapping("/add")
    public Result<String> add(@RequestParam Integer targetType, @RequestParam Long targetId) { return biz.add(targetType, targetId); }
    @ApiOperation(value = "取消收藏") @DeleteMapping("/remove")
    public Result<String> remove(@RequestParam Long id) { return biz.remove(id); }
    @ApiOperation(value = "检查收藏") @GetMapping("/check")
    public Result<Boolean> check(@RequestParam Integer targetType, @RequestParam Long targetId) { return biz.check(targetType, targetId); }
}
