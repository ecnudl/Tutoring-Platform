package com.roncoo.education.user.dao.impl;

import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.base.page.PageUtil;
import com.roncoo.education.common.tools.IdWorker;
import com.roncoo.education.common.base.AbstractBaseJdbc;
import com.roncoo.education.user.dao.DictSubjectDao;
import com.roncoo.education.user.dao.impl.mapper.DictSubjectMapper;
import com.roncoo.education.user.dao.impl.mapper.entity.DictSubject;
import com.roncoo.education.user.dao.impl.mapper.entity.DictSubjectExample;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import jakarta.validation.constraints.NotNull;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DictSubjectDaoImpl extends AbstractBaseJdbc implements DictSubjectDao {
    @NotNull
    private final DictSubjectMapper dictSubjectMapper;

    @Override
    public int save(DictSubject record) {
        if (record.getId() == null) {
            record.setId(IdWorker.getId());
        }
        return this.dictSubjectMapper.insertSelective(record);
    }

    @Override
    public int deleteById(Long id) {
        return this.dictSubjectMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateById(DictSubject record) {
        record.setGmtCreate(null);
        record.setGmtModified(null);
        return this.dictSubjectMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public DictSubject getById(Long id) {
        return this.dictSubjectMapper.selectByPrimaryKey(id);
    }

    @Override
    public Page<DictSubject> page(int pageCurrent, int pageSize, DictSubjectExample example) {
        int count = this.dictSubjectMapper.countByExample(example);
        pageSize = PageUtil.checkPageSize(pageSize);
        pageCurrent = PageUtil.checkPageCurrent(count, pageSize, pageCurrent);
        int totalPage = PageUtil.countTotalPage(count, pageSize);
        example.setLimitStart(PageUtil.countOffset(pageCurrent, pageSize));
        example.setPageSize(pageSize);
        return new Page<DictSubject>(count, totalPage, pageCurrent, pageSize, this.dictSubjectMapper.selectByExample(example));
    }

    @Override
    public List<DictSubject> listAll() {
        // 仅启用项 (status_id=1). 软删的旧条目 status_id=0 不返回, 否则前端字典里会同时出现新/旧科目.
        DictSubjectExample example = new DictSubjectExample();
        example.createCriteria().andStatusIdEqualTo(1);
        example.setOrderByClause("id asc, sort asc");
        return this.dictSubjectMapper.selectByExample(example);
    }

    @Override
    public List<DictSubject> listByCategoryId(Long categoryId) {
        DictSubjectExample example = new DictSubjectExample();
        DictSubjectExample.Criteria criteria = example.createCriteria();
        criteria.andStatusIdEqualTo(1);
        criteria.andCategoryIdEqualTo(categoryId);
        example.setOrderByClause("sort asc");
        return this.dictSubjectMapper.selectByExample(example);
    }

    @Override
    public List<DictSubject> listHot() {
        DictSubjectExample example = new DictSubjectExample();
        DictSubjectExample.Criteria criteria = example.createCriteria();
        criteria.andStatusIdEqualTo(1);
        criteria.andIsHotEqualTo(1);
        example.setOrderByClause("sort asc");
        return this.dictSubjectMapper.selectByExample(example);
    }
}
