package com.roncoo.education.user.dao.impl;

import com.roncoo.education.common.tools.IdWorker;
import com.roncoo.education.common.base.AbstractBaseJdbc;
import com.roncoo.education.user.dao.DictSubDistrictDao;
import com.roncoo.education.user.dao.impl.mapper.DictSubDistrictMapper;
import com.roncoo.education.user.dao.impl.mapper.entity.DictSubDistrict;
import com.roncoo.education.user.dao.impl.mapper.entity.DictSubDistrictExample;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import jakarta.validation.constraints.NotNull;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DictSubDistrictDaoImpl extends AbstractBaseJdbc implements DictSubDistrictDao {
    @NotNull
    private final DictSubDistrictMapper dictSubDistrictMapper;

    @Override
    public int save(DictSubDistrict record) {
        if (record.getId() == null) {
            record.setId(IdWorker.getId());
        }
        return this.dictSubDistrictMapper.insertSelective(record);
    }

    @Override
    public int deleteById(Long id) {
        return this.dictSubDistrictMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateById(DictSubDistrict record) {
        record.setGmtCreate(null);
        record.setGmtModified(null);
        return this.dictSubDistrictMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public DictSubDistrict getById(Long id) {
        return this.dictSubDistrictMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<DictSubDistrict> listByDistrictId(Long districtId) {
        DictSubDistrictExample example = new DictSubDistrictExample();
        DictSubDistrictExample.Criteria criteria = example.createCriteria();
        criteria.andDistrictIdEqualTo(districtId).andStatusIdEqualTo(1);
        example.setOrderByClause("sort asc");
        return this.dictSubDistrictMapper.selectByExample(example);
    }
}
