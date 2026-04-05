package com.roncoo.education.user.dao.impl;

import com.roncoo.education.common.tools.IdWorker;
import com.roncoo.education.common.base.AbstractBaseJdbc;
import com.roncoo.education.user.dao.PriceReferenceDao;
import com.roncoo.education.user.dao.impl.mapper.PriceReferenceMapper;
import com.roncoo.education.user.dao.impl.mapper.entity.PriceReference;
import com.roncoo.education.user.dao.impl.mapper.entity.PriceReferenceExample;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import jakarta.validation.constraints.NotNull;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PriceReferenceDaoImpl extends AbstractBaseJdbc implements PriceReferenceDao {
    @NotNull
    private final PriceReferenceMapper priceReferenceMapper;

    @Override
    public int save(PriceReference record) {
        if (record.getId() == null) {
            record.setId(IdWorker.getId());
        }
        return this.priceReferenceMapper.insertSelective(record);
    }

    @Override
    public int deleteById(Long id) {
        return this.priceReferenceMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateById(PriceReference record) {
        record.setGmtCreate(null);
        record.setGmtModified(null);
        return this.priceReferenceMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public PriceReference getById(Long id) {
        return this.priceReferenceMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<PriceReference> listByCityId(Long cityId) {
        PriceReferenceExample example = new PriceReferenceExample();
        PriceReferenceExample.Criteria criteria = example.createCriteria();
        criteria.andCityIdEqualTo(cityId).andStatusIdEqualTo(1);
        example.setOrderByClause("sort asc");
        return this.priceReferenceMapper.selectByExample(example);
    }
}
