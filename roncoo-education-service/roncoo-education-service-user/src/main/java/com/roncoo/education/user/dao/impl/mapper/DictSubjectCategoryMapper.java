package com.roncoo.education.user.dao.impl.mapper;

import com.roncoo.education.user.dao.impl.mapper.entity.DictSubjectCategory;
import com.roncoo.education.user.dao.impl.mapper.entity.DictSubjectCategoryExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DictSubjectCategoryMapper {
    int countByExample(DictSubjectCategoryExample example);

    int deleteByExample(DictSubjectCategoryExample example);

    int deleteByPrimaryKey(Long id);

    int insert(DictSubjectCategory record);

    int insertSelective(DictSubjectCategory record);

    List<DictSubjectCategory> selectByExample(DictSubjectCategoryExample example);

    DictSubjectCategory selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") DictSubjectCategory record, @Param("example") DictSubjectCategoryExample example);

    int updateByExample(@Param("record") DictSubjectCategory record, @Param("example") DictSubjectCategoryExample example);

    int updateByPrimaryKeySelective(DictSubjectCategory record);

    int updateByPrimaryKey(DictSubjectCategory record);
}
