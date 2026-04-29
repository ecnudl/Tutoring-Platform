package com.roncoo.education.user.service.api;

import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.user.service.api.biz.ApiRequirementBiz;
import com.roncoo.education.user.service.api.req.RequirementQuickSubmitReq;
import com.roncoo.education.user.service.api.req.RequirementSearchReq;
import com.roncoo.education.user.service.api.resp.RequirementDetailResp;
import com.roncoo.education.user.service.api.resp.RequirementListResp;
import com.roncoo.education.user.service.api.resp.RequirementSearchResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * API-需求列表
 *
 * @author fengyw
 */
@Api(tags = "api-需求列表")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/api/requirement")
public class ApiRequirementController {

    @NotNull
    private final ApiRequirementBiz biz;

    @ApiOperation(value = "需求搜索", notes = "需求搜索（分页+多条件）")
    @PostMapping(value = "/search")
    public Result<Page<RequirementSearchResp>> search(@RequestBody RequirementSearchReq req) {
        return biz.search(req);
    }

    @ApiOperation(value = "需求详情(按展示编号)", notes = "根据展示编号查看需求详情")
    @ApiImplicitParam(name = "displayNo", value = "展示编号", dataTypeClass = String.class, paramType = "query", required = true)
    @GetMapping(value = "/view")
    public Result<RequirementDetailResp> viewByDisplayNo(@RequestParam String displayNo) {
        return biz.viewByDisplayNo(displayNo);
    }

    @ApiOperation(value = "最新需求", notes = "首页最新需求列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cityId", value = "城市ID", dataTypeClass = Long.class, paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "数量限制", dataTypeClass = Integer.class, paramType = "query")
    })
    @GetMapping(value = "/latest")
    public Result<List<RequirementListResp>> latest(@RequestParam(required = false) Long cityId,
                                                    @RequestParam(required = false, defaultValue = "10") Integer limit) {
        return biz.latest(cityId, limit);
    }

    @ApiOperation(value = "快速提交需求", notes = "游客快速提交家教需求(无需登录)")
    @PostMapping(value = "/quick-submit")
    public Result<String> quickSubmit(@RequestBody RequirementQuickSubmitReq req) {
        return biz.quickSubmit(req);
    }

    @ApiOperation(value = "相关订单", notes = "订单详情侧栏 - 同区域近期 5 条")
    @GetMapping(value = "/related")
    public Result<List<RequirementListResp>> related(@RequestParam String displayNo,
                                                     @RequestParam(required = false, defaultValue = "5") Integer limit) {
        return biz.related(displayNo, limit);
    }

}
