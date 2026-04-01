package com.roncoo.education.system.service.api.biz;

import com.roncoo.education.common.base.page.PageUtil;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.common.core.enums.StatusIdEnum;
import com.roncoo.education.common.tools.BeanUtil;
import com.roncoo.education.common.base.BaseBiz;
import com.roncoo.education.system.dao.WebsiteArticleDao;
import com.roncoo.education.system.dao.impl.mapper.entity.WebsiteArticle;
import com.roncoo.education.system.dao.impl.mapper.entity.WebsiteArticleExample;
import com.roncoo.education.system.service.api.resp.ApiWebsiteArticleResp;
import com.roncoo.education.system.service.api.resp.ApiWebsiteArticleViewResp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * API-文章管理
 */
@Component
@RequiredArgsConstructor
public class ApiWebsiteArticleBiz extends BaseBiz {

    @NotNull
    private final WebsiteArticleDao dao;

    public Result<List<ApiWebsiteArticleResp>> list() {
        WebsiteArticleExample example = new WebsiteArticleExample();
        example.createCriteria().andStatusIdEqualTo(StatusIdEnum.YES.getCode());
        example.setOrderByClause("sort asc, id desc");
        List<WebsiteArticle> articleList = dao.listByExample(example);
        return Result.success(PageUtil.copyList(articleList, ApiWebsiteArticleResp.class));
    }

    public Result<ApiWebsiteArticleViewResp> view(Long id) {
        WebsiteArticle article = dao.getById(id);
        if (article == null) {
            return Result.error("文章不存在");
        }
        // 浏览次数+1
        WebsiteArticle record = new WebsiteArticle();
        record.setId(id);
        record.setViewCount(article.getViewCount() + 1);
        dao.updateById(record);
        return Result.success(BeanUtil.copyProperties(article, ApiWebsiteArticleViewResp.class));
    }
}
