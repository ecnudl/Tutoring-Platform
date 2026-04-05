package com.roncoo.education.user.service.api;

import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.user.service.api.biz.ApiHomepageBiz;
import com.roncoo.education.user.service.api.resp.HomepageDataResp;
import com.roncoo.education.user.service.api.resp.PriceReferenceResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * API-首页聚合
 *
 * @author fengyw
 */
@Api(tags = "api-首页聚合")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/api/homepage")
public class ApiHomepageController {

    @NotNull
    private final ApiHomepageBiz biz;

    @ApiOperation(value = "首页数据", notes = "首页聚合数据(轮播图、热门科目、热门区县、推荐教员、最新需求、配置)")
    @ApiImplicitParam(name = "cityId", value = "城市ID", dataTypeClass = Long.class, paramType = "query")
    @GetMapping(value = "/data")
    public Result<HomepageDataResp> data(@RequestParam(required = false) Long cityId) {
        return biz.data(cityId);
    }

    @ApiOperation(value = "价格参考", notes = "获取城市的价格参考表")
    @ApiImplicitParam(name = "cityId", value = "城市ID", dataTypeClass = Long.class, paramType = "query")
    @GetMapping(value = "/price-reference")
    public Result<List<PriceReferenceResp>> priceReference(@RequestParam(required = false) Long cityId) {
        return biz.priceReference(cityId);
    }

}
