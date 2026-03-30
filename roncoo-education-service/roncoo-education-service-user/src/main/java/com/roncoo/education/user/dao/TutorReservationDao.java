package com.roncoo.education.user.dao;

import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorReservation;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorReservationExample;

import java.util.List;

public interface TutorReservationDao {
    int save(TutorReservation record);

    int deleteById(Long id);

    int updateById(TutorReservation record);

    TutorReservation getById(Long id);

    Page<TutorReservation> page(int pageCurrent, int pageSize, TutorReservationExample example);

    List<TutorReservation> listByStudentUserId(Long studentUserId);

    List<TutorReservation> listByTutorUserId(Long tutorUserId);
}
