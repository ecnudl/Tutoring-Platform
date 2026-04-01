package com.roncoo.education.system.service.admin.biz;

import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.base.page.PageUtil;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.common.tools.BeanUtil;
import com.roncoo.education.common.base.BaseBiz;
import com.roncoo.education.system.dao.WebsiteArticleDao;
import com.roncoo.education.system.dao.impl.mapper.entity.WebsiteArticle;
import com.roncoo.education.system.dao.impl.mapper.entity.WebsiteArticleExample;
import com.roncoo.education.system.dao.impl.mapper.entity.WebsiteArticleExample.Criteria;
import com.roncoo.education.system.service.admin.req.AdminWebsiteArticleEditReq;
import com.roncoo.education.system.service.admin.req.AdminWebsiteArticlePageReq;
import com.roncoo.education.system.service.admin.req.AdminWebsiteArticleSaveReq;
import com.roncoo.education.system.service.admin.resp.AdminWebsiteArticlePageResp;
import com.roncoo.education.system.service.admin.resp.AdminWebsiteArticleViewResp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import jakarta.validation.constraints.NotNull;

/**
 * ADMIN-文章管理
 *
 * @author wujing
 */
@Component
@RequiredArgsConstructor
public class AdminWebsiteArticleBiz extends BaseBiz {

    @NotNull
    private final WebsiteArticleDao dao;

    /**
     * 文章管理分页
     *
     * @param req 文章管理分页查询参数
     * @return 文章管理分页查询结果
     */
    public Result<Page<AdminWebsiteArticlePageResp>> page(AdminWebsiteArticlePageReq req) {
        WebsiteArticleExample example = new WebsiteArticleExample();
        Criteria c = example.createCriteria();
        if (req.getStatusId() != null) {
            c.andStatusIdEqualTo(req.getStatusId());
        }
        if (StringUtils.hasText(req.getArticleTitle())) {
            c.andArticleTitleLike("%" + req.getArticleTitle() + "%");
        }
        example.setOrderByClause("sort asc, id desc");
        Page<WebsiteArticle> page = dao.page(req.getPageCurrent(), req.getPageSize(), example);
        Page<AdminWebsiteArticlePageResp> respPage = PageUtil.transform(page, AdminWebsiteArticlePageResp.class);
        return Result.success(respPage);
    }

    /**
     * 文章管理添加
     *
     * @param req 文章管理
     * @return 添加结果
     */
    public Result<String> save(AdminWebsiteArticleSaveReq req) {
        WebsiteArticle record = BeanUtil.copyProperties(req, WebsiteArticle.class);
        if (dao.save(record) > 0) {
            return Result.success("操作成功");
        }
        return Result.error("操作失败");
    }

    /**
     * 文章管理查看
     *
     * @param id 主键ID
     * @return 文章管理
     */
    public Result<AdminWebsiteArticleViewResp> view(Long id) {
        return Result.success(BeanUtil.copyProperties(dao.getById(id), AdminWebsiteArticleViewResp.class));
    }

    /**
     * 文章管理修改
     *
     * @param req 文章管理修改对象
     * @return 修改结果
     */
    public Result<String> edit(AdminWebsiteArticleEditReq req) {
        WebsiteArticle record = BeanUtil.copyProperties(req, WebsiteArticle.class);
        if (dao.updateById(record) > 0) {
            return Result.success("操作成功");
        }
        return Result.error("操作失败");
    }

    /**
     * 文章管理删除
     *
     * @param id ID主键
     * @return 删除结果
     */
    public Result<String> delete(Long id) {
        if (dao.deleteById(id) > 0) {
            return Result.success("操作成功");
        }
        return Result.error("操作失败");
    }
}
