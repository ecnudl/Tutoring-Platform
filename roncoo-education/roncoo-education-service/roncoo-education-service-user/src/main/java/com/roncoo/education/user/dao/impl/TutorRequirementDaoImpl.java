package com.roncoo.education.user.dao.impl;

import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.base.page.PageUtil;
import com.roncoo.education.common.tools.IdWorker;
import com.roncoo.education.common.base.AbstractBaseJdbc;
import com.roncoo.education.user.dao.TutorRequirementDao;
import com.roncoo.education.user.dao.impl.mapper.TutorRequirementMapper;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorRequirement;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorRequirementExample;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import jakarta.validation.constraints.NotNull;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TutorRequirementDaoImpl extends AbstractBaseJdbc implements TutorRequirementDao {
    @NotNull
    private final TutorRequirementMapper tutorRequirementMapper;

    @Override
    public int save(TutorRequirement record) {
        if (record.getId() == null) {
            record.setId(IdWorker.getId());
        }
        return this.tutorRequirementMapper.insertSelective(record);
    }

    @Override
    public int deleteById(Long id) {
        return this.tutorRequirementMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateById(TutorRequirement record) {
        record.setGmtCreate(null);
        record.setGmtModified(null);
        return this.tutorRequirementMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public TutorRequirement getById(Long id) {
        return this.tutorRequirementMapper.selectByPrimaryKey(id);
    }

    @Override
    public Page<TutorRequirement> page(int pageCurrent, int pageSize, TutorRequirementExample example) {
        int count = this.tutorRequirementMapper.countByExample(example);
        pageSize = PageUtil.checkPageSize(pageSize);
        pageCurrent = PageUtil.checkPageCurrent(count, pageSize, pageCurrent);
        int totalPage = PageUtil.countTotalPage(count, pageSize);
        example.setLimitStart(PageUtil.countOffset(pageCurrent, pageSize));
        example.setPageSize(pageSize);
        return new Page<TutorRequirement>(count, totalPage, pageCurrent, pageSize, this.tutorRequirementMapper.selectByExample(example));
    }

    @Override
    public List<TutorRequirement> listByUserId(Long userId) {
        TutorRequirementExample example = new TutorRequirementExample();
        TutorRequirementExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        return this.tutorRequirementMapper.selectByExample(example);
    }

    @Override
    public List<TutorRequirement> listByReqStatus(Integer reqStatus) {
        TutorRequirementExample example = new TutorRequirementExample();
        TutorRequirementExample.Criteria criteria = example.createCriteria();
        criteria.andReqStatusEqualTo(reqStatus);
        return this.tutorRequirementMapper.selectByExample(example);
    }
}
