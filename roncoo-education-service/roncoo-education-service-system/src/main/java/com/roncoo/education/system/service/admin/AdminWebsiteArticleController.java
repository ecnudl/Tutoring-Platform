package com.roncoo.education.system.service.admin;

import com.roncoo.education.common.log.SysLog;
import com.roncoo.education.common.log.SysLogCache;
import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.common.base.SortReq;
import com.roncoo.education.system.service.admin.biz.AdminWebsiteArticleBiz;
import com.roncoo.education.system.service.admin.req.AdminWebsiteArticleEditReq;
import com.roncoo.education.system.service.admin.req.AdminWebsiteArticlePageReq;
import com.roncoo.education.system.service.admin.req.AdminWebsiteArticleSaveReq;
import com.roncoo.education.system.service.admin.resp.AdminWebsiteArticlePageResp;
import com.roncoo.education.system.service.admin.resp.AdminWebsiteArticleViewResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * ADMIN-文章管理
 *
 * @author wujing
 */
@Api(tags = "admin-文章管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/admin/website/article")
public class AdminWebsiteArticleController {

    @NotNull
    private final AdminWebsiteArticleBiz biz;

    @ApiOperation(value = "文章管理分页", notes = "文章管理分页")
    @PostMapping(value = "/page")
    public Result<Page<AdminWebsiteArticlePageResp>> page(@RequestBody AdminWebsiteArticlePageReq req) {
        return biz.page(req);
    }

    @ApiOperation(value = "文章管理添加", notes = "文章管理添加")
    @SysLog(value = "文章管理添加")
    @PostMapping(value = "/save")
    public Result<String> save(@RequestBody @Valid AdminWebsiteArticleSaveReq req) {
        return biz.save(req);
    }

    @ApiOperation(value = "文章管理查看", notes = "文章管理查看")
    @ApiImplicitParam(name = "id", value = "主键ID", dataTypeClass = Long.class, paramType = "query", required = true)
    @SysLogCache
    @GetMapping(value = "/view")
    public Result<AdminWebsiteArticleViewResp> view(@RequestParam Long id) {
        return biz.view(id);
    }

    @ApiOperation(value = "文章管理修改", notes = "文章管理修改")
    @SysLog(value = "文章管理修改")
    @PutMapping(value = "/edit")
    public Result<String> edit(@RequestBody @Valid AdminWebsiteArticleEditReq req) {
        return biz.edit(req);
    }

    @ApiOperation(value = "文章管理删除", notes = "文章管理删除")
    @ApiImplicitParam(name = "id", value = "主键ID", dataTypeClass = Long.class, paramType = "query", required = true)
    @SysLog(value = "文章管理删除")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam Long id) {
        return biz.delete(id);
    }

    @ApiOperation(value = "排序", notes = "排序")
    @SysLog(value = "排序")
    @PutMapping(value = "/sort")
    public Result<Integer> sort(@RequestBody List<SortReq> req) {
        return Result.success(biz.sort(req, "WebsiteArticle"));
    }
}
