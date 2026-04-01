package com.roncoo.education.system.dao.impl;

import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.base.page.PageUtil;
import com.roncoo.education.common.tools.IdWorker;
import com.roncoo.education.system.dao.WebsiteArticleDao;
import com.roncoo.education.system.dao.impl.mapper.WebsiteArticleMapper;
import com.roncoo.education.system.dao.impl.mapper.entity.WebsiteArticle;
import com.roncoo.education.system.dao.impl.mapper.entity.WebsiteArticleExample;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class WebsiteArticleDaoImpl implements WebsiteArticleDao {
    @NotNull
    private final WebsiteArticleMapper mapper;

    @Override
    public int save(WebsiteArticle record) {
        if (record.getId() == null) {
            record.setId(IdWorker.getId());
        }
        return this.mapper.insertSelective(record);
    }

    @Override
    public int deleteById(Long id) {
        return this.mapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateById(WebsiteArticle record) {
        record.setGmtCreate(null);
        record.setGmtModified(null);
        return this.mapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public WebsiteArticle getById(Long id) {
        return this.mapper.selectByPrimaryKey(id);
    }

    @Override
    public Page<WebsiteArticle> page(int pageCurrent, int pageSize, WebsiteArticleExample example) {
        int count = this.mapper.countByExample(example);
        pageSize = PageUtil.checkPageSize(pageSize);
        pageCurrent = PageUtil.checkPageCurrent(count, pageSize, pageCurrent);
        int totalPage = PageUtil.countTotalPage(count, pageSize);
        example.setLimitStart(PageUtil.countOffset(pageCurrent, pageSize));
        example.setPageSize(pageSize);
        return new Page<>(count, totalPage, pageCurrent, pageSize, this.mapper.selectByExample(example));
    }

    @Override
    public List<WebsiteArticle> listByExample(WebsiteArticleExample example) {
        return this.mapper.selectByExample(example);
    }

    @Override
    public int countByExample(WebsiteArticleExample example) {
        return this.mapper.countByExample(example);
    }
}
