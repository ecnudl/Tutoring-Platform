package com.roncoo.education.user.dao;

import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorCertification;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorCertificationExample;

import java.util.List;

public interface TutorCertificationDao {
    int save(TutorCertification record);

    int deleteById(Long id);

    int updateById(TutorCertification record);

    TutorCertification getById(Long id);

    Page<TutorCertification> page(int pageCurrent, int pageSize, TutorCertificationExample example);

    List<TutorCertification> listByTutorId(Long tutorId);
}
