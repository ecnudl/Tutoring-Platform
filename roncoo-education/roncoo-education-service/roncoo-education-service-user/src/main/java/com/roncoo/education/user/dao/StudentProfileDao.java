package com.roncoo.education.user.dao;

import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.user.dao.impl.mapper.entity.StudentProfile;
import com.roncoo.education.user.dao.impl.mapper.entity.StudentProfileExample;

import java.util.List;

public interface StudentProfileDao {
    int save(StudentProfile record);

    int deleteById(Long id);

    int updateById(StudentProfile record);

    StudentProfile getById(Long id);

    Page<StudentProfile> page(int pageCurrent, int pageSize, StudentProfileExample example);

    StudentProfile getByUserId(Long userId);
}
