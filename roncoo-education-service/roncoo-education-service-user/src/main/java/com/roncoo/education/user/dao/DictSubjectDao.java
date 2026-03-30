package com.roncoo.education.user.dao;

import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.user.dao.impl.mapper.entity.DictSubject;
import com.roncoo.education.user.dao.impl.mapper.entity.DictSubjectExample;

import java.util.List;

public interface DictSubjectDao {
    int save(DictSubject record);

    int deleteById(Long id);

    int updateById(DictSubject record);

    DictSubject getById(Long id);

    Page<DictSubject> page(int pageCurrent, int pageSize, DictSubjectExample example);

    List<DictSubject> listAll();
}
