package com.roncoo.education.user.dao;

import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.user.dao.impl.mapper.entity.DictTutorTag;
import com.roncoo.education.user.dao.impl.mapper.entity.DictTutorTagExample;

import java.util.List;

public interface DictTutorTagDao {
    int save(DictTutorTag record);

    int deleteById(Long id);

    int updateById(DictTutorTag record);

    DictTutorTag getById(Long id);

    Page<DictTutorTag> page(int pageCurrent, int pageSize, DictTutorTagExample example);

    List<DictTutorTag> listAll();
}
