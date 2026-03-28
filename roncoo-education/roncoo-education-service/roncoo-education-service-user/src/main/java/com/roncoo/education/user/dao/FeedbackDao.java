package com.roncoo.education.user.dao;

import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.user.dao.impl.mapper.entity.Feedback;
import com.roncoo.education.user.dao.impl.mapper.entity.FeedbackExample;

import java.util.List;

public interface FeedbackDao {
    int save(Feedback record);

    int deleteById(Long id);

    int updateById(Feedback record);

    Feedback getById(Long id);

    Page<Feedback> page(int pageCurrent, int pageSize, FeedbackExample example);

    List<Feedback> listByUserId(Long userId);
}
