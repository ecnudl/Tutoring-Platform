package com.roncoo.education.user.dao;

import com.roncoo.education.user.dao.impl.mapper.entity.PriceReference;

import java.util.List;

public interface PriceReferenceDao {
    int save(PriceReference record);

    int deleteById(Long id);

    int updateById(PriceReference record);

    PriceReference getById(Long id);

    List<PriceReference> listByCityId(Long cityId);
}
