package com.roncoo.education.user.dao;

import com.roncoo.education.user.dao.impl.mapper.entity.HomepageConfig;

import java.util.List;

public interface HomepageConfigDao {
    int save(HomepageConfig record);

    int deleteById(Long id);

    int updateById(HomepageConfig record);

    HomepageConfig getById(Long id);

    HomepageConfig getByCityIdAndKey(Long cityId, String key);

    List<HomepageConfig> listByCityId(Long cityId);
}
