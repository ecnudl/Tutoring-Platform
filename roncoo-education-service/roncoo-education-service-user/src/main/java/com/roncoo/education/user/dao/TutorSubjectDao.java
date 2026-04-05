package com.roncoo.education.user.dao;

import com.roncoo.education.user.dao.impl.mapper.entity.TutorSubject;

import java.util.List;

public interface TutorSubjectDao {
    int save(TutorSubject record);

    int deleteById(Long id);

    List<TutorSubject> listByTutorId(Long tutorId);

    List<TutorSubject> listBySubjectId(Long subjectId);

    int deleteByTutorId(Long tutorId);
}
