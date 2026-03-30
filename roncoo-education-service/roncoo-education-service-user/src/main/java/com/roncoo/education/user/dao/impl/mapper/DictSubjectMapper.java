package com.roncoo.education.user.dao.impl.mapper;

import com.roncoo.education.user.dao.impl.mapper.entity.DictSubject;
import com.roncoo.education.user.dao.impl.mapper.entity.DictSubjectExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DictSubjectMapper {
    int countByExample(DictSubjectExample example);

    int deleteByExample(DictSubjectExample example);

    int deleteByPrimaryKey(Long id);

    int insert(DictSubject record);

    int insertSelective(DictSubject record);

    List<DictSubject> selectByExample(DictSubjectExample example);

    DictSubject selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") DictSubject record, @Param("example") DictSubjectExample example);

    int updateByExample(@Param("record") DictSubject record, @Param("example") DictSubjectExample example);

    int updateByPrimaryKeySelective(DictSubject record);

    int updateByPrimaryKey(DictSubject record);
}
