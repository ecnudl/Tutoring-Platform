package com.roncoo.education.user.dao;

import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorAuditRecord;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorAuditRecordExample;

import java.util.List;

public interface TutorAuditRecordDao {
    int save(TutorAuditRecord record);

    int deleteById(Long id);

    int updateById(TutorAuditRecord record);

    TutorAuditRecord getById(Long id);

    Page<TutorAuditRecord> page(int pageCurrent, int pageSize, TutorAuditRecordExample example);

    List<TutorAuditRecord> listByTutorId(Long tutorId);
}
