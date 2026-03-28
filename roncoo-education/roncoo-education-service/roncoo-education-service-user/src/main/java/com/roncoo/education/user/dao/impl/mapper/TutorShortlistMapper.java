package com.roncoo.education.user.dao.impl.mapper;

import com.roncoo.education.user.dao.impl.mapper.entity.TutorShortlist;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorShortlistExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TutorShortlistMapper {
    int countByExample(TutorShortlistExample example);

    int deleteByExample(TutorShortlistExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TutorShortlist record);

    int insertSelective(TutorShortlist record);

    List<TutorShortlist> selectByExample(TutorShortlistExample example);

    TutorShortlist selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TutorShortlist record, @Param("example") TutorShortlistExample example);

    int updateByExample(@Param("record") TutorShortlist record, @Param("example") TutorShortlistExample example);

    int updateByPrimaryKeySelective(TutorShortlist record);

    int updateByPrimaryKey(TutorShortlist record);
}
