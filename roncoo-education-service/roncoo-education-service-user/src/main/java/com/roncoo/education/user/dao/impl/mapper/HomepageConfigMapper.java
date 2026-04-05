package com.roncoo.education.user.dao.impl.mapper;

import com.roncoo.education.user.dao.impl.mapper.entity.HomepageConfig;
import com.roncoo.education.user.dao.impl.mapper.entity.HomepageConfigExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HomepageConfigMapper {
    int countByExample(HomepageConfigExample example);

    int deleteByExample(HomepageConfigExample example);

    int deleteByPrimaryKey(Long id);

    int insert(HomepageConfig record);

    int insertSelective(HomepageConfig record);

    List<HomepageConfig> selectByExample(HomepageConfigExample example);

    HomepageConfig selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") HomepageConfig record, @Param("example") HomepageConfigExample example);

    int updateByExample(@Param("record") HomepageConfig record, @Param("example") HomepageConfigExample example);

    int updateByPrimaryKeySelective(HomepageConfig record);

    int updateByPrimaryKey(HomepageConfig record);
}
