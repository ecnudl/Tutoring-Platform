package com.roncoo.education.user.dao;

import com.roncoo.education.user.dao.impl.mapper.entity.DictSubDistrict;

import java.util.List;

public interface DictSubDistrictDao {
    int save(DictSubDistrict record);

    int deleteById(Long id);

    int updateById(DictSubDistrict record);

    DictSubDistrict getById(Long id);

    List<DictSubDistrict> listByDistrictId(Long districtId);
}
