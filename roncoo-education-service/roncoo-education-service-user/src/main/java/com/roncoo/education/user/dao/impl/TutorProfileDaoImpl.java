package com.roncoo.education.user.dao.impl;

import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.base.page.PageUtil;
import com.roncoo.education.common.tools.IdWorker;
import com.roncoo.education.common.base.AbstractBaseJdbc;
import com.roncoo.education.user.dao.TutorProfileDao;
import com.roncoo.education.user.dao.impl.mapper.TutorProfileMapper;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorProfile;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorProfileExample;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import jakarta.validation.constraints.NotNull;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TutorProfileDaoImpl extends AbstractBaseJdbc implements TutorProfileDao {
    @NotNull
    private final TutorProfileMapper tutorProfileMapper;

    @Override
    public int save(TutorProfile record) {
        if (record.getId() == null) {
            record.setId(IdWorker.getId());
        }
        return this.tutorProfileMapper.insertSelective(record);
    }

    @Override
    public int deleteById(Long id) {
        return this.tutorProfileMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateById(TutorProfile record) {
        record.setGmtCreate(null);
        record.setGmtModified(null);
        return this.tutorProfileMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public TutorProfile getById(Long id) {
        return this.tutorProfileMapper.selectByPrimaryKey(id);
    }

    @Override
    public Page<TutorProfile> page(int pageCurrent, int pageSize, TutorProfileExample example) {
        int count = this.tutorProfileMapper.countByExample(example);
        pageSize = PageUtil.checkPageSize(pageSize);
        pageCurrent = PageUtil.checkPageCurrent(count, pageSize, pageCurrent);
        int totalPage = PageUtil.countTotalPage(count, pageSize);
        example.setLimitStart(PageUtil.countOffset(pageCurrent, pageSize));
        example.setPageSize(pageSize);
        return new Page<TutorProfile>(count, totalPage, pageCurrent, pageSize, this.tutorProfileMapper.selectByExample(example));
    }

    @Override
    public TutorProfile getByUserId(Long userId) {
        TutorProfileExample example = new TutorProfileExample();
        TutorProfileExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        List<TutorProfile> list = this.tutorProfileMapper.selectByExample(example);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<TutorProfile> listByAuditStatus(Integer auditStatus) {
        TutorProfileExample example = new TutorProfileExample();
        TutorProfileExample.Criteria criteria = example.createCriteria();
        criteria.andAuditStatusEqualTo(auditStatus);
        return this.tutorProfileMapper.selectByExample(example);
    }
}
