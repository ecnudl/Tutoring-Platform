package com.roncoo.education.user.dao;

import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.user.dao.impl.mapper.entity.DictUniversity;
import com.roncoo.education.user.dao.impl.mapper.entity.DictUniversityExample;

import java.util.List;

public interface DictUniversityDao {
    int save(DictUniversity record);

    int deleteById(Long id);

    int updateById(DictUniversity record);

    DictUniversity getById(Long id);

    List<DictUniversity> listByCityId(Long cityId);

    List<DictUniversity> listHotByCityId(Long cityId);

    Page<DictUniversity> page(int pageCurrent, int pageSize, DictUniversityExample example);
}
