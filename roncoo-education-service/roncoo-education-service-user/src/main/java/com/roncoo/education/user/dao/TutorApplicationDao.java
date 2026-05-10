package com.roncoo.education.user.dao;

import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorApplication;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorApplicationExample;

import java.util.List;

public interface TutorApplicationDao {
    int save(TutorApplication record);

    int deleteById(Long id);

    int updateById(TutorApplication record);

    /** 条件更新 (selective). 返回受影响行数, 用于乐观并发控制 (cancel/match 竞态). */
    int updateByExampleSelective(TutorApplication record, TutorApplicationExample example);

    TutorApplication getById(Long id);

    Page<TutorApplication> page(int pageCurrent, int pageSize, TutorApplicationExample example);

    List<TutorApplication> listByRequirementId(Long requirementId);

    List<TutorApplication> listByUserId(Long userId);

    TutorApplication getByRequirementIdAndTutorId(Long requirementId, Long tutorId);
}
