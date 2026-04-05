package com.roncoo.education.user.dao;

import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.user.dao.impl.mapper.entity.WebsiteBanner;
import com.roncoo.education.user.dao.impl.mapper.entity.WebsiteBannerExample;

import java.util.List;

public interface WebsiteBannerDao {
    int save(WebsiteBanner record);

    int deleteById(Long id);

    int updateById(WebsiteBanner record);

    WebsiteBanner getById(Long id);

    List<WebsiteBanner> listByCityIdAndPosition(Long cityId, String position);

    Page<WebsiteBanner> page(int pageCurrent, int pageSize, WebsiteBannerExample example);
}
