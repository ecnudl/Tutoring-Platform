package com.roncoo.education.user.dao.impl;

import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.base.page.PageUtil;
import com.roncoo.education.common.tools.IdWorker;
import com.roncoo.education.common.base.AbstractBaseJdbc;
import com.roncoo.education.user.dao.FeedbackDao;
import com.roncoo.education.user.dao.impl.mapper.FeedbackMapper;
import com.roncoo.education.user.dao.impl.mapper.entity.Feedback;
import com.roncoo.education.user.dao.impl.mapper.entity.FeedbackExample;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import jakarta.validation.constraints.NotNull;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FeedbackDaoImpl extends AbstractBaseJdbc implements FeedbackDao {
    @NotNull
    private final FeedbackMapper feedbackMapper;

    @Override
    public int save(Feedback record) {
        if (record.getId() == null) {
            record.setId(IdWorker.getId());
        }
        return this.feedbackMapper.insertSelective(record);
    }

    @Override
    public int deleteById(Long id) {
        return this.feedbackMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateById(Feedback record) {
        record.setGmtCreate(null);
        record.setGmtModified(null);
        return this.feedbackMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public Feedback getById(Long id) {
        return this.feedbackMapper.selectByPrimaryKey(id);
    }

    @Override
    public Page<Feedback> page(int pageCurrent, int pageSize, FeedbackExample example) {
        int count = this.feedbackMapper.countByExample(example);
        pageSize = PageUtil.checkPageSize(pageSize);
        pageCurrent = PageUtil.checkPageCurrent(count, pageSize, pageCurrent);
        int totalPage = PageUtil.countTotalPage(count, pageSize);
        example.setLimitStart(PageUtil.countOffset(pageCurrent, pageSize));
        example.setPageSize(pageSize);
        return new Page<Feedback>(count, totalPage, pageCurrent, pageSize, this.feedbackMapper.selectByExample(example));
    }

    @Override
    public List<Feedback> listByUserId(Long userId) {
        FeedbackExample example = new FeedbackExample();
        FeedbackExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        return this.feedbackMapper.selectByExample(example);
    }
}
