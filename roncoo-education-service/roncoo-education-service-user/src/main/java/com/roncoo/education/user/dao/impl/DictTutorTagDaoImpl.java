package com.roncoo.education.user.dao.impl;

import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.base.page.PageUtil;
import com.roncoo.education.common.tools.IdWorker;
import com.roncoo.education.common.base.AbstractBaseJdbc;
import com.roncoo.education.user.dao.DictTutorTagDao;
import com.roncoo.education.user.dao.impl.mapper.DictTutorTagMapper;
import com.roncoo.education.user.dao.impl.mapper.entity.DictTutorTag;
import com.roncoo.education.user.dao.impl.mapper.entity.DictTutorTagExample;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import jakarta.validation.constraints.NotNull;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DictTutorTagDaoImpl extends AbstractBaseJdbc implements DictTutorTagDao {
    @NotNull
    private final DictTutorTagMapper dictTutorTagMapper;

    @Override
    public int save(DictTutorTag record) {
        if (record.getId() == null) {
            record.setId(IdWorker.getId());
        }
        return this.dictTutorTagMapper.insertSelective(record);
    }

    @Override
    public int deleteById(Long id) {
        return this.dictTutorTagMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateById(DictTutorTag record) {
        record.setGmtCreate(null);
        record.setGmtModified(null);
        return this.dictTutorTagMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public DictTutorTag getById(Long id) {
        return this.dictTutorTagMapper.selectByPrimaryKey(id);
    }

    @Override
    public Page<DictTutorTag> page(int pageCurrent, int pageSize, DictTutorTagExample example) {
        int count = this.dictTutorTagMapper.countByExample(example);
        pageSize = PageUtil.checkPageSize(pageSize);
        pageCurrent = PageUtil.checkPageCurrent(count, pageSize, pageCurrent);
        int totalPage = PageUtil.countTotalPage(count, pageSize);
        example.setLimitStart(PageUtil.countOffset(pageCurrent, pageSize));
        example.setPageSize(pageSize);
        return new Page<DictTutorTag>(count, totalPage, pageCurrent, pageSize, this.dictTutorTagMapper.selectByExample(example));
    }

    @Override
    public List<DictTutorTag> listAll() {
        DictTutorTagExample example = new DictTutorTagExample();
        return this.dictTutorTagMapper.selectByExample(example);
    }
}
