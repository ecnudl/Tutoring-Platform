package com.roncoo.education.user.service.api;

import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.user.service.api.biz.ApiRequirementBiz;
import com.roncoo.education.user.service.api.req.RequirementSearchReq;
import com.roncoo.education.user.service.api.resp.RequirementSearchResp;
import com.roncoo.education.user.service.api.resp.RequirementViewResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.NotNull;

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

    @ApiOperation(value = "需求详情", notes = "需求详情查看")
    @ApiImplicitParam(name = "id", value = "主键ID", dataTypeClass = Long.class, paramType = "query", required = true)
    @GetMapping(value = "/view")
    public Result<RequirementViewResp> view(@RequestParam Long id) {
        return biz.view(id);
    }

}
