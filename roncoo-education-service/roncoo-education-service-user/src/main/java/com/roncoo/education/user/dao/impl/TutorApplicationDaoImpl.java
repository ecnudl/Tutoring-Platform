package com.roncoo.education.user.dao.impl;

import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.base.page.PageUtil;
import com.roncoo.education.common.tools.IdWorker;
import com.roncoo.education.common.base.AbstractBaseJdbc;
import com.roncoo.education.user.dao.TutorApplicationDao;
import com.roncoo.education.user.dao.impl.mapper.TutorApplicationMapper;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorApplication;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorApplicationExample;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TutorApplicationDaoImpl extends AbstractBaseJdbc implements TutorApplicationDao {
    @NotNull
    private final TutorApplicationMapper tutorApplicationMapper;

    @Override
    public int save(TutorApplication record) {
        if (record.getId() == null) {
            record.setId(IdWorker.getId());
        }
        return this.tutorApplicationMapper.insertSelective(record);
    }

    @Override
    public int deleteById(Long id) {
        return this.tutorApplicationMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateById(TutorApplication record) {
        record.setGmtCreate(null);
        record.setGmtModified(null);
        return this.tutorApplicationMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByExampleSelective(TutorApplication record, TutorApplicationExample example) {
        record.setGmtCreate(null);
        record.setGmtModified(null);
        return this.tutorApplicationMapper.updateByExampleSelective(record, example);
    }

    @Override
    public TutorApplication getById(Long id) {
        return this.tutorApplicationMapper.selectByPrimaryKey(id);
    }

    /**
     * 分页. selectByExample 不加载 BLOB (apply_message), 用主键再 select 一次拿全字段.
     */
    @Override
    public Page<TutorApplication> page(int pageCurrent, int pageSize, TutorApplicationExample example) {
        int count = this.tutorApplicationMapper.countByExample(example);
        pageSize = PageUtil.checkPageSize(pageSize);
        pageCurrent = PageUtil.checkPageCurrent(count, pageSize, pageCurrent);
        int totalPage = PageUtil.countTotalPage(count, pageSize);
        example.setLimitStart(PageUtil.countOffset(pageCurrent, pageSize));
        example.setPageSize(pageSize);
        return new Page<TutorApplication>(count, totalPage, pageCurrent, pageSize, hydrate(this.tutorApplicationMapper.selectByExample(example)));
    }

    @Override
    public List<TutorApplication> listByRequirementId(Long requirementId) {
        TutorApplicationExample example = new TutorApplicationExample();
        example.createCriteria().andRequirementIdEqualTo(requirementId);
        return hydrate(this.tutorApplicationMapper.selectByExample(example));
    }

    @Override
    public List<TutorApplication> listByUserId(Long userId) {
        TutorApplicationExample example = new TutorApplicationExample();
        example.createCriteria().andUserIdEqualTo(userId);
        return hydrate(this.tutorApplicationMapper.selectByExample(example));
    }

    @Override
    public TutorApplication getByRequirementIdAndTutorId(Long requirementId, Long tutorId) {
        TutorApplicationExample example = new TutorApplicationExample();
        TutorApplicationExample.Criteria criteria = example.createCriteria();
        criteria.andRequirementIdEqualTo(requirementId);
        criteria.andTutorIdEqualTo(tutorId);
        List<TutorApplication> list = hydrate(this.tutorApplicationMapper.selectByExample(example));
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    /** 用主键回查一次, 让 BLOB 列 (apply_message) 也带上. */
    private List<TutorApplication> hydrate(List<TutorApplication> shallow) {
        if (shallow == null || shallow.isEmpty()) return shallow;
        List<TutorApplication> full = new ArrayList<>(shallow.size());
        for (TutorApplication a : shallow) {
            TutorApplication withBlobs = this.tutorApplicationMapper.selectByPrimaryKey(a.getId());
            full.add(withBlobs != null ? withBlobs : a);
        }
        return full;
    }
}
