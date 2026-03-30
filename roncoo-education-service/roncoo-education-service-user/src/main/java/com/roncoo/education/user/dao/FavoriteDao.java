package com.roncoo.education.user.dao;

import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.user.dao.impl.mapper.entity.Favorite;
import com.roncoo.education.user.dao.impl.mapper.entity.FavoriteExample;

import java.util.List;

public interface FavoriteDao {
    int save(Favorite record);

    int deleteById(Long id);

    int updateById(Favorite record);

    Favorite getById(Long id);

    Page<Favorite> page(int pageCurrent, int pageSize, FavoriteExample example);

    List<Favorite> listByUserId(Long userId);

    Favorite getByUserIdAndTarget(Long userId, Integer targetType, Long targetId);
}
