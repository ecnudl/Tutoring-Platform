package com.roncoo.education.user.dao.impl.mapper;

import com.roncoo.education.user.dao.impl.mapper.entity.DictUniversity;
import com.roncoo.education.user.dao.impl.mapper.entity.DictUniversityExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DictUniversityMapper {
    int countByExample(DictUniversityExample example);

    int deleteByExample(DictUniversityExample example);

    int deleteByPrimaryKey(Long id);

    int insert(DictUniversity record);

    int insertSelective(DictUniversity record);

    List<DictUniversity> selectByExample(DictUniversityExample example);

    DictUniversity selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") DictUniversity record, @Param("example") DictUniversityExample example);

    int updateByExample(@Param("record") DictUniversity record, @Param("example") DictUniversityExample example);

    int updateByPrimaryKeySelective(DictUniversity record);

    int updateByPrimaryKey(DictUniversity record);
}
