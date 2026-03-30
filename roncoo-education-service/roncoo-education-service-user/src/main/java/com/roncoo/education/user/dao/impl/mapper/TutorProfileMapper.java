package com.roncoo.education.user.dao.impl.mapper;

import com.roncoo.education.user.dao.impl.mapper.entity.TutorProfile;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorProfileExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TutorProfileMapper {
    int countByExample(TutorProfileExample example);

    int deleteByExample(TutorProfileExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TutorProfile record);

    int insertSelective(TutorProfile record);

    List<TutorProfile> selectByExampleWithBLOBs(TutorProfileExample example);

    List<TutorProfile> selectByExample(TutorProfileExample example);

    TutorProfile selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TutorProfile record, @Param("example") TutorProfileExample example);

    int updateByExampleWithBLOBs(@Param("record") TutorProfile record, @Param("example") TutorProfileExample example);

    int updateByExample(@Param("record") TutorProfile record, @Param("example") TutorProfileExample example);

    int updateByPrimaryKeySelective(TutorProfile record);

    int updateByPrimaryKeyWithBLOBs(TutorProfile record);

    int updateByPrimaryKey(TutorProfile record);
}
