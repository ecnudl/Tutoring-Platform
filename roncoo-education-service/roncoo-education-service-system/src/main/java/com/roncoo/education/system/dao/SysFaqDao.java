package com.roncoo.education.system.dao;

import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.system.dao.impl.mapper.entity.SysFaq;

import java.util.List;

public interface SysFaqDao {
    int save(SysFaq record);

    int deleteById(Long id);

    int updateById(SysFaq record);

    SysFaq getById(Long id);

    Page<SysFaq> page(int pageCurrent, int pageSize, String category, Integer statusId, String keyword);

    List<SysFaq> listAllActive();
}
