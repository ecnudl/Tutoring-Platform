package com.roncoo.education.user.dao;

import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorShortlist;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorShortlistExample;

import java.util.List;

public interface TutorShortlistDao {
    int save(TutorShortlist record);

    int deleteById(Long id);

    int updateById(TutorShortlist record);

    TutorShortlist getById(Long id);

    Page<TutorShortlist> page(int pageCurrent, int pageSize, TutorShortlistExample example);

    List<TutorShortlist> listByUserId(Long userId);

    TutorShortlist getByUserIdAndTutorId(Long userId, Long tutorId);
}
