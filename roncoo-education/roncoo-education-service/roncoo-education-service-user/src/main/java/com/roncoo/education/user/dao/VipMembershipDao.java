package com.roncoo.education.user.dao;

import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.user.dao.impl.mapper.entity.VipMembership;
import com.roncoo.education.user.dao.impl.mapper.entity.VipMembershipExample;

import java.util.List;

public interface VipMembershipDao {
    int save(VipMembership record);

    int deleteById(Long id);

    int updateById(VipMembership record);

    VipMembership getById(Long id);

    Page<VipMembership> page(int pageCurrent, int pageSize, VipMembershipExample example);

    VipMembership getByUserId(Long userId);
}
