package com.roncoo.education.user.dao.impl;

import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.base.page.PageUtil;
import com.roncoo.education.common.tools.IdWorker;
import com.roncoo.education.common.base.AbstractBaseJdbc;
import com.roncoo.education.user.dao.TutorRequirementAuditDao;
import com.roncoo.education.user.dao.impl.mapper.TutorRequirementAuditMapper;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorRequirementAudit;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorRequirementAuditExample;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import jakarta.validation.constraints.NotNull;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TutorRequirementAuditDaoImpl extends AbstractBaseJdbc implements TutorRequirementAuditDao {
    @NotNull
    private final TutorRequirementAuditMapper tutorRequirementAuditMapper;

    @Override
    public int save(TutorRequirementAudit record) {
        if (record.getId() == null) {
            record.setId(IdWorker.getId());
        }
        return this.tutorRequirementAuditMapper.insertSelective(record);
    }

    @Override
    public int deleteById(Long id) {
        return this.tutorRequirementAuditMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateById(TutorRequirementAudit record) {
        record.setGmtCreate(null);
        record.setGmtModified(null);
        return this.tutorRequirementAuditMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public TutorRequirementAudit getById(Long id) {
        return this.tutorRequirementAuditMapper.selectByPrimaryKey(id);
    }

    @Override
    public Page<TutorRequirementAudit> page(int pageCurrent, int pageSize, TutorRequirementAuditExample example) {
        int count = this.tutorRequirementAuditMapper.countByExample(example);
        pageSize = PageUtil.checkPageSize(pageSize);
        pageCurrent = PageUtil.checkPageCurrent(count, pageSize, pageCurrent);
        int totalPage = PageUtil.countTotalPage(count, pageSize);
        example.setLimitStart(PageUtil.countOffset(pageCurrent, pageSize));
        example.setPageSize(pageSize);
        return new Page<TutorRequirementAudit>(count, totalPage, pageCurrent, pageSize, this.tutorRequirementAuditMapper.selectByExample(example));
    }

    @Override
    public List<TutorRequirementAudit> listByRequirementId(Long requirementId) {
        TutorRequirementAuditExample example = new TutorRequirementAuditExample();
        TutorRequirementAuditExample.Criteria criteria = example.createCriteria();
        criteria.andRequirementIdEqualTo(requirementId);
        return this.tutorRequirementAuditMapper.selectByExample(example);
    }
}
