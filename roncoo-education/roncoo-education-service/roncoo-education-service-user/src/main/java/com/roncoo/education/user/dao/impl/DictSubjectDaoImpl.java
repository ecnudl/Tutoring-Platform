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
        DictSubjectExample example = new DictSubjectExample();
        return this.dictSubjectMapper.selectByExample(example);
    }
}
