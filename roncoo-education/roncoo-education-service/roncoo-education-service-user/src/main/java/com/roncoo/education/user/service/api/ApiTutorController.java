package com.roncoo.education.user.service.api;

import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.user.service.api.biz.ApiTutorBiz;
import com.roncoo.education.user.service.api.req.TutorSearchReq;
import com.roncoo.education.user.service.api.resp.TutorSearchResp;
import com.roncoo.education.user.service.api.resp.TutorViewResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.NotNull;

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

    @ApiOperation(value = "教员详情", notes = "教员详情查看")
    @ApiImplicitParam(name = "id", value = "主键ID", dataTypeClass = Long.class, paramType = "query", required = true)
    @GetMapping(value = "/view")
    public Result<?> view(@RequestParam Long id) {
        return biz.view(id);
    }

}
