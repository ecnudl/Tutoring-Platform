package com.roncoo.education.user.dao;

import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorApplication;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorApplicationExample;

import java.util.List;

public interface TutorApplicationDao {
    int save(TutorApplication record);

    int deleteById(Long id);

    int updateById(TutorApplication record);

    TutorApplication getById(Long id);

    Page<TutorApplication> page(int pageCurrent, int pageSize, TutorApplicationExample example);

    List<TutorApplication> listByRequirementId(Long requirementId);

    List<TutorApplication> listByUserId(Long userId);

    TutorApplication getByRequirementIdAndTutorId(Long requirementId, Long tutorId);
}
