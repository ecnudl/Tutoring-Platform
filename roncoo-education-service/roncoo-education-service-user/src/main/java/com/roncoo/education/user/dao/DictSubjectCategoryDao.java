package com.roncoo.education.user.dao;

import com.roncoo.education.user.dao.impl.mapper.entity.DictSubjectCategory;

import java.util.List;

public interface DictSubjectCategoryDao {
    int save(DictSubjectCategory record);

    int deleteById(Long id);

    int updateById(DictSubjectCategory record);

    DictSubjectCategory getById(Long id);

    List<DictSubjectCategory> listByParentId(Long parentId);

    List<DictSubjectCategory> listAll();
}
