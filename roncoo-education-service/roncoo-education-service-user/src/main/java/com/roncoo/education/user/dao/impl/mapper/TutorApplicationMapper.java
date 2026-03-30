package com.roncoo.education.user.dao.impl.mapper;

import com.roncoo.education.user.dao.impl.mapper.entity.TutorApplication;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorApplicationExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TutorApplicationMapper {
    int countByExample(TutorApplicationExample example);

    int deleteByExample(TutorApplicationExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TutorApplication record);

    int insertSelective(TutorApplication record);

    List<TutorApplication> selectByExampleWithBLOBs(TutorApplicationExample example);

    List<TutorApplication> selectByExample(TutorApplicationExample example);

    TutorApplication selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TutorApplication record, @Param("example") TutorApplicationExample example);

    int updateByExampleWithBLOBs(@Param("record") TutorApplication record, @Param("example") TutorApplicationExample example);

    int updateByExample(@Param("record") TutorApplication record, @Param("example") TutorApplicationExample example);

    int updateByPrimaryKeySelective(TutorApplication record);

    int updateByPrimaryKeyWithBLOBs(TutorApplication record);

    int updateByPrimaryKey(TutorApplication record);
}
