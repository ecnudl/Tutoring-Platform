package com.roncoo.education.user.dao.impl;

import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.base.page.PageUtil;
import com.roncoo.education.common.tools.IdWorker;
import com.roncoo.education.common.base.AbstractBaseJdbc;
import com.roncoo.education.user.dao.DictUniversityDao;
import com.roncoo.education.user.dao.impl.mapper.DictUniversityMapper;
import com.roncoo.education.user.dao.impl.mapper.entity.DictUniversity;
import com.roncoo.education.user.dao.impl.mapper.entity.DictUniversityExample;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import jakarta.validation.constraints.NotNull;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DictUniversityDaoImpl extends AbstractBaseJdbc implements DictUniversityDao {
    @NotNull
    private final DictUniversityMapper dictUniversityMapper;

    @Override
    public int save(DictUniversity record) {
        if (record.getId() == null) {
            record.setId(IdWorker.getId());
        }
        return this.dictUniversityMapper.insertSelective(record);
    }

    @Override
    public int deleteById(Long id) {
        return this.dictUniversityMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateById(DictUniversity record) {
        record.setGmtCreate(null);
        record.setGmtModified(null);
        return this.dictUniversityMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public DictUniversity getById(Long id) {
        return this.dictUniversityMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<DictUniversity> listByCityId(Long cityId) {
        DictUniversityExample example = new DictUniversityExample();
        DictUniversityExample.Criteria criteria = example.createCriteria();
        criteria.andCityIdEqualTo(cityId).andStatusIdEqualTo(1);
        example.setOrderByClause("sort asc");
        return this.dictUniversityMapper.selectByExample(example);
    }

    @Override
    public List<DictUniversity> listHotByCityId(Long cityId) {
        DictUniversityExample example = new DictUniversityExample();
        DictUniversityExample.Criteria criteria = example.createCriteria();
        criteria.andCityIdEqualTo(cityId).andStatusIdEqualTo(1).andIsHotEqualTo(1);
        example.setOrderByClause("sort asc");
        return this.dictUniversityMapper.selectByExample(example);
    }

    @Override
    public Page<DictUniversity> page(int pageCurrent, int pageSize, DictUniversityExample example) {
        int count = this.dictUniversityMapper.countByExample(example);
        pageSize = PageUtil.checkPageSize(pageSize);
        pageCurrent = PageUtil.checkPageCurrent(count, pageSize, pageCurrent);
        int totalPage = PageUtil.countTotalPage(count, pageSize);
        example.setLimitStart(PageUtil.countOffset(pageCurrent, pageSize));
        example.setPageSize(pageSize);
        return new Page<DictUniversity>(count, totalPage, pageCurrent, pageSize, this.dictUniversityMapper.selectByExample(example));
    }
}
