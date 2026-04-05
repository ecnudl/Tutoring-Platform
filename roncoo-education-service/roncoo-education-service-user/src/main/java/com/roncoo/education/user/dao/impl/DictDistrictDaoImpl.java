package com.roncoo.education.user.dao.impl;

import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.base.page.PageUtil;
import com.roncoo.education.common.tools.IdWorker;
import com.roncoo.education.common.base.AbstractBaseJdbc;
import com.roncoo.education.user.dao.DictDistrictDao;
import com.roncoo.education.user.dao.impl.mapper.DictDistrictMapper;
import com.roncoo.education.user.dao.impl.mapper.entity.DictDistrict;
import com.roncoo.education.user.dao.impl.mapper.entity.DictDistrictExample;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import jakarta.validation.constraints.NotNull;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DictDistrictDaoImpl extends AbstractBaseJdbc implements DictDistrictDao {
    @NotNull
    private final DictDistrictMapper dictDistrictMapper;

    @Override
    public int save(DictDistrict record) {
        if (record.getId() == null) {
            record.setId(IdWorker.getId());
        }
        return this.dictDistrictMapper.insertSelective(record);
    }

    @Override
    public int deleteById(Long id) {
        return this.dictDistrictMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateById(DictDistrict record) {
        record.setGmtCreate(null);
        record.setGmtModified(null);
        return this.dictDistrictMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public DictDistrict getById(Long id) {
        return this.dictDistrictMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<DictDistrict> listByCityId(Long cityId) {
        DictDistrictExample example = new DictDistrictExample();
        DictDistrictExample.Criteria criteria = example.createCriteria();
        criteria.andCityIdEqualTo(cityId).andStatusIdEqualTo(1);
        example.setOrderByClause("sort asc");
        return this.dictDistrictMapper.selectByExample(example);
    }

    @Override
    public Page<DictDistrict> page(int pageCurrent, int pageSize, DictDistrictExample example) {
        int count = this.dictDistrictMapper.countByExample(example);
        pageSize = PageUtil.checkPageSize(pageSize);
        pageCurrent = PageUtil.checkPageCurrent(count, pageSize, pageCurrent);
        int totalPage = PageUtil.countTotalPage(count, pageSize);
        example.setLimitStart(PageUtil.countOffset(pageCurrent, pageSize));
        example.setPageSize(pageSize);
        return new Page<DictDistrict>(count, totalPage, pageCurrent, pageSize, this.dictDistrictMapper.selectByExample(example));
    }
}
