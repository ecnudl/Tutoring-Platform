package com.roncoo.education.user.dao.impl;

import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.base.page.PageUtil;
import com.roncoo.education.common.tools.IdWorker;
import com.roncoo.education.common.base.AbstractBaseJdbc;
import com.roncoo.education.user.dao.DictCityDao;
import com.roncoo.education.user.dao.impl.mapper.DictCityMapper;
import com.roncoo.education.user.dao.impl.mapper.entity.DictCity;
import com.roncoo.education.user.dao.impl.mapper.entity.DictCityExample;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import jakarta.validation.constraints.NotNull;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DictCityDaoImpl extends AbstractBaseJdbc implements DictCityDao {
    @NotNull
    private final DictCityMapper dictCityMapper;

    @Override
    public int save(DictCity record) {
        if (record.getId() == null) {
            record.setId(IdWorker.getId());
        }
        return this.dictCityMapper.insertSelective(record);
    }

    @Override
    public int deleteById(Long id) {
        return this.dictCityMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateById(DictCity record) {
        record.setGmtCreate(null);
        record.setGmtModified(null);
        return this.dictCityMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public DictCity getById(Long id) {
        return this.dictCityMapper.selectByPrimaryKey(id);
    }

    @Override
    public DictCity getByPinyin(String pinyin) {
        DictCityExample example = new DictCityExample();
        DictCityExample.Criteria criteria = example.createCriteria();
        criteria.andCityPinyinEqualTo(pinyin);
        List<DictCity> list = this.dictCityMapper.selectByExample(example);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<DictCity> listByStatusId(Integer statusId) {
        DictCityExample example = new DictCityExample();
        DictCityExample.Criteria criteria = example.createCriteria();
        criteria.andStatusIdEqualTo(statusId);
        example.setOrderByClause("sort asc");
        return this.dictCityMapper.selectByExample(example);
    }

    @Override
    public Page<DictCity> page(int pageCurrent, int pageSize, DictCityExample example) {
        int count = this.dictCityMapper.countByExample(example);
        pageSize = PageUtil.checkPageSize(pageSize);
        pageCurrent = PageUtil.checkPageCurrent(count, pageSize, pageCurrent);
        int totalPage = PageUtil.countTotalPage(count, pageSize);
        example.setLimitStart(PageUtil.countOffset(pageCurrent, pageSize));
        example.setPageSize(pageSize);
        return new Page<DictCity>(count, totalPage, pageCurrent, pageSize, this.dictCityMapper.selectByExample(example));
    }
}
