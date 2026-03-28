package com.roncoo.education.user.dao;

import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.user.dao.impl.mapper.entity.Footprint;
import com.roncoo.education.user.dao.impl.mapper.entity.FootprintExample;

import java.util.List;

public interface FootprintDao {
    int save(Footprint record);

    int deleteById(Long id);

    int updateById(Footprint record);

    Footprint getById(Long id);

    Page<Footprint> page(int pageCurrent, int pageSize, FootprintExample example);

    List<Footprint> listByUserId(Long userId);
}
