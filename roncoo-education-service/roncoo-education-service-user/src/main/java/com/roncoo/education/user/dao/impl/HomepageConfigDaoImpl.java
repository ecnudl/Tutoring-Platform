package com.roncoo.education.user.dao.impl;

import com.roncoo.education.common.tools.IdWorker;
import com.roncoo.education.common.base.AbstractBaseJdbc;
import com.roncoo.education.user.dao.HomepageConfigDao;
import com.roncoo.education.user.dao.impl.mapper.HomepageConfigMapper;
import com.roncoo.education.user.dao.impl.mapper.entity.HomepageConfig;
import com.roncoo.education.user.dao.impl.mapper.entity.HomepageConfigExample;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import jakarta.validation.constraints.NotNull;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class HomepageConfigDaoImpl extends AbstractBaseJdbc implements HomepageConfigDao {
    @NotNull
    private final HomepageConfigMapper homepageConfigMapper;

    @Override
    public int save(HomepageConfig record) {
        if (record.getId() == null) {
            record.setId(IdWorker.getId());
        }
        return this.homepageConfigMapper.insertSelective(record);
    }

    @Override
    public int deleteById(Long id) {
        return this.homepageConfigMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateById(HomepageConfig record) {
        record.setGmtCreate(null);
        record.setGmtModified(null);
        return this.homepageConfigMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public HomepageConfig getById(Long id) {
        return this.homepageConfigMapper.selectByPrimaryKey(id);
    }

    @Override
    public HomepageConfig getByCityIdAndKey(Long cityId, String key) {
        HomepageConfigExample example = new HomepageConfigExample();
        HomepageConfigExample.Criteria criteria = example.createCriteria();
        criteria.andCityIdEqualTo(cityId).andConfigKeyEqualTo(key);
        List<HomepageConfig> list = this.homepageConfigMapper.selectByExample(example);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<HomepageConfig> listByCityId(Long cityId) {
        HomepageConfigExample example = new HomepageConfigExample();
        HomepageConfigExample.Criteria criteria = example.createCriteria();
        criteria.andCityIdEqualTo(cityId).andStatusIdEqualTo(1);
        return this.homepageConfigMapper.selectByExample(example);
    }
}
