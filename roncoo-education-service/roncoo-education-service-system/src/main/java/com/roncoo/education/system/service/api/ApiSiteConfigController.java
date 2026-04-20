package com.roncoo.education.system.service.api;

import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.system.service.api.resp.ApiSiteConfigResp;
import com.roncoo.education.system.service.biz.SysConfigCommonBiz;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 站点公开配置 API（品牌、客服、备案、协议、请家教文案、底部菜单 JSON、友链 JSON、价格说明 JSON）。
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/api/site")
@CacheConfig(cacheNames = {"system"})
public class ApiSiteConfigController {

    @NotNull
    private final SysConfigCommonBiz sysConfigCommonBiz;

    @Cacheable(key = "'site:config'")
    @GetMapping(value = "/config")
    public Result<ApiSiteConfigResp> config() {
        return Result.success(sysConfigCommonBiz.getSysConfig(ApiSiteConfigResp.class));
    }
}
