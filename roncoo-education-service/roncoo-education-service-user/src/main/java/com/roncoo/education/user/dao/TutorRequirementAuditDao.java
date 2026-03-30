package com.roncoo.education.user.dao;

import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorRequirementAudit;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorRequirementAuditExample;

import java.util.List;

public interface TutorRequirementAuditDao {
    int save(TutorRequirementAudit record);

    int deleteById(Long id);

    int updateById(TutorRequirementAudit record);

    TutorRequirementAudit getById(Long id);

    Page<TutorRequirementAudit> page(int pageCurrent, int pageSize, TutorRequirementAuditExample example);

    List<TutorRequirementAudit> listByRequirementId(Long requirementId);
}
