package com.roncoo.education.user.dao.impl;

import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.base.page.PageUtil;
import com.roncoo.education.common.tools.IdWorker;
import com.roncoo.education.common.base.AbstractBaseJdbc;
import com.roncoo.education.user.dao.TutorCertificationDao;
import com.roncoo.education.user.dao.impl.mapper.TutorCertificationMapper;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorCertification;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorCertificationExample;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import jakarta.validation.constraints.NotNull;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TutorCertificationDaoImpl extends AbstractBaseJdbc implements TutorCertificationDao {
    @NotNull
    private final TutorCertificationMapper tutorCertificationMapper;

    @Override
    public int save(TutorCertification record) {
        if (record.getId() == null) {
            record.setId(IdWorker.getId());
        }
        return this.tutorCertificationMapper.insertSelective(record);
    }

    @Override
    public int deleteById(Long id) {
        return this.tutorCertificationMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateById(TutorCertification record) {
        record.setGmtCreate(null);
        record.setGmtModified(null);
        return this.tutorCertificationMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public TutorCertification getById(Long id) {
        return this.tutorCertificationMapper.selectByPrimaryKey(id);
    }

    @Override
    public Page<TutorCertification> page(int pageCurrent, int pageSize, TutorCertificationExample example) {
        int count = this.tutorCertificationMapper.countByExample(example);
        pageSize = PageUtil.checkPageSize(pageSize);
        pageCurrent = PageUtil.checkPageCurrent(count, pageSize, pageCurrent);
        int totalPage = PageUtil.countTotalPage(count, pageSize);
        example.setLimitStart(PageUtil.countOffset(pageCurrent, pageSize));
        example.setPageSize(pageSize);
        return new Page<TutorCertification>(count, totalPage, pageCurrent, pageSize, this.tutorCertificationMapper.selectByExample(example));
    }

    @Override
    public List<TutorCertification> listByTutorId(Long tutorId) {
        TutorCertificationExample example = new TutorCertificationExample();
        TutorCertificationExample.Criteria criteria = example.createCriteria();
        criteria.andTutorIdEqualTo(tutorId);
        return this.tutorCertificationMapper.selectByExample(example);
    }
}
