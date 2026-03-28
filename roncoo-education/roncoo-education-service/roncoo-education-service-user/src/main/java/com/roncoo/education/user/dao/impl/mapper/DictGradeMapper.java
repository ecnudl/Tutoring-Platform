package com.roncoo.education.user.dao.impl.mapper;

import com.roncoo.education.user.dao.impl.mapper.entity.DictGrade;
import com.roncoo.education.user.dao.impl.mapper.entity.DictGradeExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DictGradeMapper {
    int countByExample(DictGradeExample example);

    int deleteByExample(DictGradeExample example);

    int deleteByPrimaryKey(Long id);

    int insert(DictGrade record);

    int insertSelective(DictGrade record);

    List<DictGrade> selectByExample(DictGradeExample example);

    DictGrade selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") DictGrade record, @Param("example") DictGradeExample example);

    int updateByExample(@Param("record") DictGrade record, @Param("example") DictGradeExample example);

    int updateByPrimaryKeySelective(DictGrade record);

    int updateByPrimaryKey(DictGrade record);
}
