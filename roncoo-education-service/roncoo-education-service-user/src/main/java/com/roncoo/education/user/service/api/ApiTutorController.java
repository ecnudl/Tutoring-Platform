package com.roncoo.education.user.service.api;

import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.user.service.api.biz.ApiTutorBiz;
import com.roncoo.education.user.service.api.req.TutorSearchReq;
import com.roncoo.education.user.service.api.resp.TutorDetailResp;
import com.roncoo.education.user.service.api.resp.TutorListResp;
import com.roncoo.education.user.service.api.resp.TutorSearchResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * API-教员库
 *
 * @author fengyw
 */
@Api(tags = "api-教员库")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/api/tutor")
public class ApiTutorController {

    @NotNull
    private final ApiTutorBiz biz;

    @ApiOperation(value = "教员搜索", notes = "教员搜索（分页+多条件）")
    @PostMapping(value = "/search")
    public Result<Page<TutorSearchResp>> search(@RequestBody TutorSearchReq req) {
        return biz.search(req);
    }

    @ApiOperation(value = "教员详情(按展示编号)", notes = "根据展示编号查看教员详情")
    @ApiImplicitParam(name = "displayNo", value = "展示编号", dataTypeClass = String.class, paramType = "query", required = true)
    @GetMapping(value = "/view")
    public Result<TutorDetailResp> viewByDisplayNo(@RequestParam String displayNo) {
        return biz.viewByDisplayNo(displayNo);
    }

    @ApiOperation(value = "推荐教员", notes = "首页推荐教员列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cityId", value = "城市ID", dataTypeClass = Long.class, paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "数量限制", dataTypeClass = Integer.class, paramType = "query")
    })
    @GetMapping(value = "/recommend")
    public Result<List<TutorListResp>> recommend(@RequestParam(required = false) Long cityId,
                                                 @RequestParam(required = false, defaultValue = "10") Integer limit) {
        return biz.recommend(cityId, limit);
    }

}
