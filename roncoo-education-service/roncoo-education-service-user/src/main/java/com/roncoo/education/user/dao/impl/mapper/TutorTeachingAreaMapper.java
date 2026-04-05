package com.roncoo.education.user.dao.impl.mapper;

import com.roncoo.education.user.dao.impl.mapper.entity.TutorTeachingArea;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorTeachingAreaExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TutorTeachingAreaMapper {
    int countByExample(TutorTeachingAreaExample example);

    int deleteByExample(TutorTeachingAreaExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TutorTeachingArea record);

    int insertSelective(TutorTeachingArea record);

    List<TutorTeachingArea> selectByExample(TutorTeachingAreaExample example);

    TutorTeachingArea selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TutorTeachingArea record, @Param("example") TutorTeachingAreaExample example);

    int updateByExample(@Param("record") TutorTeachingArea record, @Param("example") TutorTeachingAreaExample example);

    int updateByPrimaryKeySelective(TutorTeachingArea record);

    int updateByPrimaryKey(TutorTeachingArea record);
}
