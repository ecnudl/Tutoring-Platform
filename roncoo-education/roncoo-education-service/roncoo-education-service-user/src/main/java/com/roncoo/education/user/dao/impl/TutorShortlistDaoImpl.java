package com.roncoo.education.user.dao.impl;

import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.base.page.PageUtil;
import com.roncoo.education.common.tools.IdWorker;
import com.roncoo.education.common.base.AbstractBaseJdbc;
import com.roncoo.education.user.dao.TutorShortlistDao;
import com.roncoo.education.user.dao.impl.mapper.TutorShortlistMapper;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorShortlist;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorShortlistExample;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import jakarta.validation.constraints.NotNull;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TutorShortlistDaoImpl extends AbstractBaseJdbc implements TutorShortlistDao {
    @NotNull
    private final TutorShortlistMapper tutorShortlistMapper;

    @Override
    public int save(TutorShortlist record) {
        if (record.getId() == null) {
            record.setId(IdWorker.getId());
        }
        return this.tutorShortlistMapper.insertSelective(record);
    }

    @Override
    public int deleteById(Long id) {
        return this.tutorShortlistMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateById(TutorShortlist record) {
        record.setGmtCreate(null);
        record.setGmtModified(null);
        return this.tutorShortlistMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public TutorShortlist getById(Long id) {
        return this.tutorShortlistMapper.selectByPrimaryKey(id);
    }

    @Override
    public Page<TutorShortlist> page(int pageCurrent, int pageSize, TutorShortlistExample example) {
        int count = this.tutorShortlistMapper.countByExample(example);
        pageSize = PageUtil.checkPageSize(pageSize);
        pageCurrent = PageUtil.checkPageCurrent(count, pageSize, pageCurrent);
        int totalPage = PageUtil.countTotalPage(count, pageSize);
        example.setLimitStart(PageUtil.countOffset(pageCurrent, pageSize));
        example.setPageSize(pageSize);
        return new Page<TutorShortlist>(count, totalPage, pageCurrent, pageSize, this.tutorShortlistMapper.selectByExample(example));
    }

    @Override
    public List<TutorShortlist> listByUserId(Long userId) {
        TutorShortlistExample example = new TutorShortlistExample();
        TutorShortlistExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        return this.tutorShortlistMapper.selectByExample(example);
    }

    @Override
    public TutorShortlist getByUserIdAndTutorId(Long userId, Long tutorId) {
        TutorShortlistExample example = new TutorShortlistExample();
        TutorShortlistExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        criteria.andTutorIdEqualTo(tutorId);
        List<TutorShortlist> list = this.tutorShortlistMapper.selectByExample(example);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
}
