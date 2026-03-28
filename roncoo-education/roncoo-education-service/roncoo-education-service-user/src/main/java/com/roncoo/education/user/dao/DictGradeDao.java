package com.roncoo.education.user.dao;

import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.user.dao.impl.mapper.entity.DictGrade;
import com.roncoo.education.user.dao.impl.mapper.entity.DictGradeExample;

import java.util.List;

public interface DictGradeDao {
    int save(DictGrade record);

    int deleteById(Long id);

    int updateById(DictGrade record);

    DictGrade getById(Long id);

    Page<DictGrade> page(int pageCurrent, int pageSize, DictGradeExample example);

    List<DictGrade> listAll();

    List<DictGrade> listByGradeLevel(Integer gradeLevel);
}
