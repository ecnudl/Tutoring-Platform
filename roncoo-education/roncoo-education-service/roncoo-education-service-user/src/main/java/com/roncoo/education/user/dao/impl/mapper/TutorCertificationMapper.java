package com.roncoo.education.user.dao.impl.mapper;

import com.roncoo.education.user.dao.impl.mapper.entity.TutorCertification;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorCertificationExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TutorCertificationMapper {
    int countByExample(TutorCertificationExample example);

    int deleteByExample(TutorCertificationExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TutorCertification record);

    int insertSelective(TutorCertification record);

    List<TutorCertification> selectByExample(TutorCertificationExample example);

    TutorCertification selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TutorCertification record, @Param("example") TutorCertificationExample example);

    int updateByExample(@Param("record") TutorCertification record, @Param("example") TutorCertificationExample example);

    int updateByPrimaryKeySelective(TutorCertification record);

    int updateByPrimaryKey(TutorCertification record);
}
