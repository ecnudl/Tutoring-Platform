package com.roncoo.education.user.dao.impl.mapper;

import com.roncoo.education.user.dao.impl.mapper.entity.StudentProfile;
import com.roncoo.education.user.dao.impl.mapper.entity.StudentProfileExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StudentProfileMapper {
    int countByExample(StudentProfileExample example);

    int deleteByExample(StudentProfileExample example);

    int deleteByPrimaryKey(Long id);

    int insert(StudentProfile record);

    int insertSelective(StudentProfile record);

    List<StudentProfile> selectByExampleWithBLOBs(StudentProfileExample example);

    List<StudentProfile> selectByExample(StudentProfileExample example);

    StudentProfile selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") StudentProfile record, @Param("example") StudentProfileExample example);

    int updateByExampleWithBLOBs(@Param("record") StudentProfile record, @Param("example") StudentProfileExample example);

    int updateByExample(@Param("record") StudentProfile record, @Param("example") StudentProfileExample example);

    int updateByPrimaryKeySelective(StudentProfile record);

    int updateByPrimaryKeyWithBLOBs(StudentProfile record);

    int updateByPrimaryKey(StudentProfile record);
}
