package com.roncoo.education.user.dao.impl.mapper;

import com.roncoo.education.user.dao.impl.mapper.entity.DictCity;
import com.roncoo.education.user.dao.impl.mapper.entity.DictCityExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DictCityMapper {
    int countByExample(DictCityExample example);

    int deleteByExample(DictCityExample example);

    int deleteByPrimaryKey(Long id);

    int insert(DictCity record);

    int insertSelective(DictCity record);

    List<DictCity> selectByExample(DictCityExample example);

    DictCity selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") DictCity record, @Param("example") DictCityExample example);

    int updateByExample(@Param("record") DictCity record, @Param("example") DictCityExample example);

    int updateByPrimaryKeySelective(DictCity record);

    int updateByPrimaryKey(DictCity record);
}
