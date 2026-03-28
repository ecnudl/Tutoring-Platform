package com.roncoo.education.user.dao.impl.mapper;

import com.roncoo.education.user.dao.impl.mapper.entity.TutorRequirement;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorRequirementExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TutorRequirementMapper {
    int countByExample(TutorRequirementExample example);

    int deleteByExample(TutorRequirementExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TutorRequirement record);

    int insertSelective(TutorRequirement record);

    List<TutorRequirement> selectByExampleWithBLOBs(TutorRequirementExample example);

    List<TutorRequirement> selectByExample(TutorRequirementExample example);

    TutorRequirement selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TutorRequirement record, @Param("example") TutorRequirementExample example);

    int updateByExampleWithBLOBs(@Param("record") TutorRequirement record, @Param("example") TutorRequirementExample example);

    int updateByExample(@Param("record") TutorRequirement record, @Param("example") TutorRequirementExample example);

    int updateByPrimaryKeySelective(TutorRequirement record);

    int updateByPrimaryKeyWithBLOBs(TutorRequirement record);

    int updateByPrimaryKey(TutorRequirement record);
}
