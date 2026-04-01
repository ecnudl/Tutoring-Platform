package com.roncoo.education.system.dao;

import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.system.dao.impl.mapper.entity.WebsiteArticle;
import com.roncoo.education.system.dao.impl.mapper.entity.WebsiteArticleExample;

import java.util.List;

public interface WebsiteArticleDao {
    int save(WebsiteArticle record);

    int deleteById(Long id);

    int updateById(WebsiteArticle record);

    WebsiteArticle getById(Long id);

    Page<WebsiteArticle> page(int pageCurrent, int pageSize, WebsiteArticleExample example);

    List<WebsiteArticle> listByExample(WebsiteArticleExample example);

    int countByExample(WebsiteArticleExample example);
}
