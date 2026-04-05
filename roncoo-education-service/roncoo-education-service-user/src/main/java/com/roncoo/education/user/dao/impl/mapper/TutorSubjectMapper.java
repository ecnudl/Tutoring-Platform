package com.roncoo.education.user.dao.impl.mapper;

import com.roncoo.education.user.dao.impl.mapper.entity.TutorSubject;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorSubjectExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TutorSubjectMapper {
    int countByExample(TutorSubjectExample example);

    int deleteByExample(TutorSubjectExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TutorSubject record);

    int insertSelective(TutorSubject record);

    List<TutorSubject> selectByExample(TutorSubjectExample example);

    TutorSubject selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TutorSubject record, @Param("example") TutorSubjectExample example);

    int updateByExample(@Param("record") TutorSubject record, @Param("example") TutorSubjectExample example);

    int updateByPrimaryKeySelective(TutorSubject record);

    int updateByPrimaryKey(TutorSubject record);
}
