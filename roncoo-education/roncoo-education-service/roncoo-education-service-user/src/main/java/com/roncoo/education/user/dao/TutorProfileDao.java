package com.roncoo.education.user.dao;

import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorProfile;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorProfileExample;

import java.util.List;

public interface TutorProfileDao {
    int save(TutorProfile record);

    int deleteById(Long id);

    int updateById(TutorProfile record);

    TutorProfile getById(Long id);

    Page<TutorProfile> page(int pageCurrent, int pageSize, TutorProfileExample example);

    TutorProfile getByUserId(Long userId);

    List<TutorProfile> listByAuditStatus(Integer auditStatus);
}
