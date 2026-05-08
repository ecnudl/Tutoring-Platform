package com.roncoo.education.user.dao;

import com.roncoo.education.user.dao.impl.mapper.entity.TutorSubject;

import java.util.List;

public interface TutorSubjectDao {
    int save(TutorSubject record);

    int deleteById(Long id);

    List<TutorSubject> listByTutorId(Long tutorId);

    List<TutorSubject> listBySubjectId(Long subjectId);

    /** 任一 ID 命中即返回. 用于"全科"通配 (filterId + 3000). */
    List<TutorSubject> listBySubjectIds(List<Long> subjectIds);

    int deleteByTutorId(Long tutorId);
}
