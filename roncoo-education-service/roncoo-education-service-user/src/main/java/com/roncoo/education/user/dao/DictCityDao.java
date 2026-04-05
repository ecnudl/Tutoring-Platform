package com.roncoo.education.user.dao;

import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.user.dao.impl.mapper.entity.DictCity;
import com.roncoo.education.user.dao.impl.mapper.entity.DictCityExample;

import java.util.List;

public interface DictCityDao {
    int save(DictCity record);

    int deleteById(Long id);

    int updateById(DictCity record);

    DictCity getById(Long id);

    DictCity getByPinyin(String pinyin);

    List<DictCity> listByStatusId(Integer statusId);

    Page<DictCity> page(int pageCurrent, int pageSize, DictCityExample example);
}
