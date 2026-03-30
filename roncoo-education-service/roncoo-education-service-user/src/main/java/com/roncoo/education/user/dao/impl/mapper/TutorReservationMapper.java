package com.roncoo.education.user.dao.impl.mapper;

import com.roncoo.education.user.dao.impl.mapper.entity.TutorReservation;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorReservationExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TutorReservationMapper {
    int countByExample(TutorReservationExample example);

    int deleteByExample(TutorReservationExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TutorReservation record);

    int insertSelective(TutorReservation record);

    List<TutorReservation> selectByExampleWithBLOBs(TutorReservationExample example);

    List<TutorReservation> selectByExample(TutorReservationExample example);

    TutorReservation selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TutorReservation record, @Param("example") TutorReservationExample example);

    int updateByExampleWithBLOBs(@Param("record") TutorReservation record, @Param("example") TutorReservationExample example);

    int updateByExample(@Param("record") TutorReservation record, @Param("example") TutorReservationExample example);

    int updateByPrimaryKeySelective(TutorReservation record);

    int updateByPrimaryKeyWithBLOBs(TutorReservation record);

    int updateByPrimaryKey(TutorReservation record);
}
