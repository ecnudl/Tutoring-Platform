package com.roncoo.education.user.dao.impl;

import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.base.page.PageUtil;
import com.roncoo.education.common.tools.IdWorker;
import com.roncoo.education.common.base.AbstractBaseJdbc;
import com.roncoo.education.user.dao.WebsiteBannerDao;
import com.roncoo.education.user.dao.impl.mapper.WebsiteBannerMapper;
import com.roncoo.education.user.dao.impl.mapper.entity.WebsiteBanner;
import com.roncoo.education.user.dao.impl.mapper.entity.WebsiteBannerExample;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import jakarta.validation.constraints.NotNull;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class WebsiteBannerDaoImpl extends AbstractBaseJdbc implements WebsiteBannerDao {
    @NotNull
    private final WebsiteBannerMapper websiteBannerMapper;

    @Override
    public int save(WebsiteBanner record) {
        if (record.getId() == null) {
            record.setId(IdWorker.getId());
        }
        return this.websiteBannerMapper.insertSelective(record);
    }

    @Override
    public int deleteById(Long id) {
        return this.websiteBannerMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateById(WebsiteBanner record) {
        record.setGmtCreate(null);
        record.setGmtModified(null);
        return this.websiteBannerMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public WebsiteBanner getById(Long id) {
        return this.websiteBannerMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<WebsiteBanner> listByCityIdAndPosition(Long cityId, String position) {
        WebsiteBannerExample example = new WebsiteBannerExample();
        WebsiteBannerExample.Criteria criteria = example.createCriteria();
        criteria.andCityIdEqualTo(cityId).andPositionEqualTo(position).andStatusIdEqualTo(1);
        example.setOrderByClause("sort asc");
        return this.websiteBannerMapper.selectByExample(example);
    }

    @Override
    public Page<WebsiteBanner> page(int pageCurrent, int pageSize, WebsiteBannerExample example) {
        int count = this.websiteBannerMapper.countByExample(example);
        pageSize = PageUtil.checkPageSize(pageSize);
        pageCurrent = PageUtil.checkPageCurrent(count, pageSize, pageCurrent);
        int totalPage = PageUtil.countTotalPage(count, pageSize);
        example.setLimitStart(PageUtil.countOffset(pageCurrent, pageSize));
        example.setPageSize(pageSize);
        return new Page<WebsiteBanner>(count, totalPage, pageCurrent, pageSize, this.websiteBannerMapper.selectByExample(example));
    }
}
