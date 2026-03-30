package com.roncoo.education.user.dao.impl;

import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.base.page.PageUtil;
import com.roncoo.education.common.tools.IdWorker;
import com.roncoo.education.common.base.AbstractBaseJdbc;
import com.roncoo.education.user.dao.TutorAuditRecordDao;
import com.roncoo.education.user.dao.impl.mapper.TutorAuditRecordMapper;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorAuditRecord;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorAuditRecordExample;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import jakarta.validation.constraints.NotNull;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TutorAuditRecordDaoImpl extends AbstractBaseJdbc implements TutorAuditRecordDao {
    @NotNull
    private final TutorAuditRecordMapper tutorAuditRecordMapper;

    @Override
    public int save(TutorAuditRecord record) {
        if (record.getId() == null) {
            record.setId(IdWorker.getId());
        }
        return this.tutorAuditRecordMapper.insertSelective(record);
    }

    @Override
    public int deleteById(Long id) {
        return this.tutorAuditRecordMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateById(TutorAuditRecord record) {
        record.setGmtCreate(null);
        record.setGmtModified(null);
        return this.tutorAuditRecordMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public TutorAuditRecord getById(Long id) {
        return this.tutorAuditRecordMapper.selectByPrimaryKey(id);
    }

    @Override
    public Page<TutorAuditRecord> page(int pageCurrent, int pageSize, TutorAuditRecordExample example) {
        int count = this.tutorAuditRecordMapper.countByExample(example);
        pageSize = PageUtil.checkPageSize(pageSize);
        pageCurrent = PageUtil.checkPageCurrent(count, pageSize, pageCurrent);
        int totalPage = PageUtil.countTotalPage(count, pageSize);
        example.setLimitStart(PageUtil.countOffset(pageCurrent, pageSize));
        example.setPageSize(pageSize);
        return new Page<TutorAuditRecord>(count, totalPage, pageCurrent, pageSize, this.tutorAuditRecordMapper.selectByExample(example));
    }

    @Override
    public List<TutorAuditRecord> listByTutorId(Long tutorId) {
        TutorAuditRecordExample example = new TutorAuditRecordExample();
        TutorAuditRecordExample.Criteria criteria = example.createCriteria();
        criteria.andTutorIdEqualTo(tutorId);
        return this.tutorAuditRecordMapper.selectByExample(example);
    }
}
