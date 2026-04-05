package com.roncoo.education.user.dao;

import com.roncoo.education.user.dao.impl.mapper.entity.TutorTeachingArea;

import java.util.List;

public interface TutorTeachingAreaDao {
    int save(TutorTeachingArea record);

    int deleteById(Long id);

    List<TutorTeachingArea> listByTutorId(Long tutorId);

    List<TutorTeachingArea> listByCityIdAndDistrictId(Long cityId, Long districtId);

    int deleteByTutorId(Long tutorId);
}
