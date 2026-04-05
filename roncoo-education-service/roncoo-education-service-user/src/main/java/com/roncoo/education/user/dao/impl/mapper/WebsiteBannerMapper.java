package com.roncoo.education.user.dao.impl.mapper;

import com.roncoo.education.user.dao.impl.mapper.entity.WebsiteBanner;
import com.roncoo.education.user.dao.impl.mapper.entity.WebsiteBannerExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WebsiteBannerMapper {
    int countByExample(WebsiteBannerExample example);

    int deleteByExample(WebsiteBannerExample example);

    int deleteByPrimaryKey(Long id);

    int insert(WebsiteBanner record);

    int insertSelective(WebsiteBanner record);

    List<WebsiteBanner> selectByExample(WebsiteBannerExample example);

    WebsiteBanner selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") WebsiteBanner record, @Param("example") WebsiteBannerExample example);

    int updateByExample(@Param("record") WebsiteBanner record, @Param("example") WebsiteBannerExample example);

    int updateByPrimaryKeySelective(WebsiteBanner record);

    int updateByPrimaryKey(WebsiteBanner record);
}
