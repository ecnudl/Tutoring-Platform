package com.roncoo.education.user.dao.impl;

import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.base.page.PageUtil;
import com.roncoo.education.common.tools.IdWorker;
import com.roncoo.education.common.base.AbstractBaseJdbc;
import com.roncoo.education.user.dao.DictGradeDao;
import com.roncoo.education.user.dao.impl.mapper.DictGradeMapper;
import com.roncoo.education.user.dao.impl.mapper.entity.DictGrade;
import com.roncoo.education.user.dao.impl.mapper.entity.DictGradeExample;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import jakarta.validation.constraints.NotNull;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DictGradeDaoImpl extends AbstractBaseJdbc implements DictGradeDao {
    @NotNull
    private final DictGradeMapper dictGradeMapper;

    @Override
    public int save(DictGrade record) {
        if (record.getId() == null) {
            record.setId(IdWorker.getId());
        }
        return this.dictGradeMapper.insertSelective(record);
    }

    @Override
    public int deleteById(Long id) {
        return this.dictGradeMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateById(DictGrade record) {
        record.setGmtCreate(null);
        record.setGmtModified(null);
        return this.dictGradeMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public DictGrade getById(Long id) {
        return this.dictGradeMapper.selectByPrimaryKey(id);
    }

    @Override
    public Page<DictGrade> page(int pageCurrent, int pageSize, DictGradeExample example) {
        int count = this.dictGradeMapper.countByExample(example);
        pageSize = PageUtil.checkPageSize(pageSize);
        pageCurrent = PageUtil.checkPageCurrent(count, pageSize, pageCurrent);
        int totalPage = PageUtil.countTotalPage(count, pageSize);
        example.setLimitStart(PageUtil.countOffset(pageCurrent, pageSize));
        example.setPageSize(pageSize);
        return new Page<DictGrade>(count, totalPage, pageCurrent, pageSize, this.dictGradeMapper.selectByExample(example));
    }

    @Override
    public List<DictGrade> listAll() {
        DictGradeExample example = new DictGradeExample();
        return this.dictGradeMapper.selectByExample(example);
    }

    @Override
    public List<DictGrade> listByGradeLevel(Integer gradeLevel) {
        DictGradeExample example = new DictGradeExample();
        DictGradeExample.Criteria criteria = example.createCriteria();
        criteria.andGradeLevelEqualTo(gradeLevel);
        return this.dictGradeMapper.selectByExample(example);
    }
}
