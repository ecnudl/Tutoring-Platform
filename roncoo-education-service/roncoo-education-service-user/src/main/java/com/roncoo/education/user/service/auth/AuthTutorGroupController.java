package com.roncoo.education.user.service.auth;

import com.roncoo.education.common.base.ThreadContext;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.common.core.enums.UserTypeEnum;
import com.roncoo.education.user.dao.UsersDao;
import com.roncoo.education.user.dao.impl.mapper.entity.Users;
import com.roncoo.education.system.feign.interfaces.IFeignSysConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 教员接单群二维码 — 教员独享, 不暴露给学员/匿名用户.
 */
@Api(tags = "auth-教员接单群")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/auth/tutor-group")
public class AuthTutorGroupController {

    @NotNull
    private final UsersDao usersDao;

    @NotNull
    private final IFeignSysConfig feignSysConfig;

    @ApiOperation(value = "获取接单群二维码 URL (仅教员)")
    @GetMapping("/qr")
    public Result<Map<String, String>> qr() {
        Long userId = ThreadContext.userId();
        if (userId == null) return Result.error("未登录");
        Users user = usersDao.getById(userId);
        if (user == null) return Result.error("用户不存在");
        if (!UserTypeEnum.TUTOR.getCode().equals(user.getUserType())) {
            return Result.error("仅教员可访问");
        }
        Map<String, String> data = new HashMap<>();
        String url = feignSysConfig.getSys() != null ? feignSysConfig.getSys().getTutorGroupQrUrl() : "";
        data.put("qrUrl", url == null ? "" : url);
        return Result.success(data);
    }
}
