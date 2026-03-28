package com.roncoo.education.user.dao;

import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorRequirement;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorRequirementExample;

import java.util.List;

public interface TutorRequirementDao {
    int save(TutorRequirement record);

    int deleteById(Long id);

    int updateById(TutorRequirement record);

    TutorRequirement getById(Long id);

    Page<TutorRequirement> page(int pageCurrent, int pageSize, TutorRequirementExample example);

    List<TutorRequirement> listByUserId(Long userId);

    List<TutorRequirement> listByReqStatus(Integer reqStatus);
}
