package com.roncoo.education.system.service.api;

import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.system.service.api.biz.ApiWebsiteArticleBiz;
import com.roncoo.education.system.service.api.resp.ApiWebsiteArticleResp;
import com.roncoo.education.system.service.api.resp.ApiWebsiteArticleViewResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * API-文章管理
 *
 * @author wujing
 */
@Api(tags = "api-文章管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/api/website/article")
public class ApiWebsiteArticleController {

    @NotNull
    private final ApiWebsiteArticleBiz biz;

    @ApiOperation(value = "文章列表接口", notes = "文章列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result<List<ApiWebsiteArticleResp>> list() {
        return biz.list();
    }

    @ApiOperation(value = "文章详情接口", notes = "文章详情")
    @ApiImplicitParam(name = "id", value = "文章ID", dataTypeClass = Long.class, paramType = "query", required = true)
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public Result<ApiWebsiteArticleViewResp> view(@RequestParam Long id) {
        return biz.view(id);
    }
}
