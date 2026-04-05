package com.roncoo.education.user.dao;

import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.user.dao.impl.mapper.entity.DictDistrict;
import com.roncoo.education.user.dao.impl.mapper.entity.DictDistrictExample;

import java.util.List;

public interface DictDistrictDao {
    int save(DictDistrict record);

    int deleteById(Long id);

    int updateById(DictDistrict record);

    DictDistrict getById(Long id);

    List<DictDistrict> listByCityId(Long cityId);

    Page<DictDistrict> page(int pageCurrent, int pageSize, DictDistrictExample example);
}
