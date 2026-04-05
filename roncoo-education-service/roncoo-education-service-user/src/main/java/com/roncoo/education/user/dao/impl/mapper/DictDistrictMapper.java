package com.roncoo.education.user.dao.impl.mapper;

import com.roncoo.education.user.dao.impl.mapper.entity.DictDistrict;
import com.roncoo.education.user.dao.impl.mapper.entity.DictDistrictExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DictDistrictMapper {
    int countByExample(DictDistrictExample example);

    int deleteByExample(DictDistrictExample example);

    int deleteByPrimaryKey(Long id);

    int insert(DictDistrict record);

    int insertSelective(DictDistrict record);

    List<DictDistrict> selectByExample(DictDistrictExample example);

    DictDistrict selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") DictDistrict record, @Param("example") DictDistrictExample example);

    int updateByExample(@Param("record") DictDistrict record, @Param("example") DictDistrictExample example);

    int updateByPrimaryKeySelective(DictDistrict record);

    int updateByPrimaryKey(DictDistrict record);
}
