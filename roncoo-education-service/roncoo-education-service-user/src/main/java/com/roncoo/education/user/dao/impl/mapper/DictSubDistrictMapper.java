package com.roncoo.education.user.dao.impl.mapper;

import com.roncoo.education.user.dao.impl.mapper.entity.DictSubDistrict;
import com.roncoo.education.user.dao.impl.mapper.entity.DictSubDistrictExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DictSubDistrictMapper {
    int countByExample(DictSubDistrictExample example);

    int deleteByExample(DictSubDistrictExample example);

    int deleteByPrimaryKey(Long id);

    int insert(DictSubDistrict record);

    int insertSelective(DictSubDistrict record);

    List<DictSubDistrict> selectByExample(DictSubDistrictExample example);

    DictSubDistrict selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") DictSubDistrict record, @Param("example") DictSubDistrictExample example);

    int updateByExample(@Param("record") DictSubDistrict record, @Param("example") DictSubDistrictExample example);

    int updateByPrimaryKeySelective(DictSubDistrict record);

    int updateByPrimaryKey(DictSubDistrict record);
}
