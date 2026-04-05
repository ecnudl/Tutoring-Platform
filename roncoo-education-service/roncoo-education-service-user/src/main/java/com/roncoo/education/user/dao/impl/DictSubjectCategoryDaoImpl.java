package com.roncoo.education.user.dao.impl;

import com.roncoo.education.common.tools.IdWorker;
import com.roncoo.education.common.base.AbstractBaseJdbc;
import com.roncoo.education.user.dao.DictSubjectCategoryDao;
import com.roncoo.education.user.dao.impl.mapper.DictSubjectCategoryMapper;
import com.roncoo.education.user.dao.impl.mapper.entity.DictSubjectCategory;
import com.roncoo.education.user.dao.impl.mapper.entity.DictSubjectCategoryExample;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import jakarta.validation.constraints.NotNull;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DictSubjectCategoryDaoImpl extends AbstractBaseJdbc implements DictSubjectCategoryDao {
    @NotNull
    private final DictSubjectCategoryMapper dictSubjectCategoryMapper;

    @Override
    public int save(DictSubjectCategory record) {
        if (record.getId() == null) {
            record.setId(IdWorker.getId());
        }
        return this.dictSubjectCategoryMapper.insertSelective(record);
    }

    @Override
    public int deleteById(Long id) {
        return this.dictSubjectCategoryMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateById(DictSubjectCategory record) {
        record.setGmtCreate(null);
        record.setGmtModified(null);
        return this.dictSubjectCategoryMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public DictSubjectCategory getById(Long id) {
        return this.dictSubjectCategoryMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<DictSubjectCategory> listByParentId(Long parentId) {
        DictSubjectCategoryExample example = new DictSubjectCategoryExample();
        DictSubjectCategoryExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId).andStatusIdEqualTo(1);
        example.setOrderByClause("sort asc");
        return this.dictSubjectCategoryMapper.selectByExample(example);
    }

    @Override
    public List<DictSubjectCategory> listAll() {
        DictSubjectCategoryExample example = new DictSubjectCategoryExample();
        example.setOrderByClause("sort asc");
        return this.dictSubjectCategoryMapper.selectByExample(example);
    }
}
