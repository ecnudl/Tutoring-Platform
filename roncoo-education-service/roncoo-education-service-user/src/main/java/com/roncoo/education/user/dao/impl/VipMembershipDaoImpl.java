package com.roncoo.education.user.dao.impl;

import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.base.page.PageUtil;
import com.roncoo.education.common.tools.IdWorker;
import com.roncoo.education.common.base.AbstractBaseJdbc;
import com.roncoo.education.user.dao.VipMembershipDao;
import com.roncoo.education.user.dao.impl.mapper.VipMembershipMapper;
import com.roncoo.education.user.dao.impl.mapper.entity.VipMembership;
import com.roncoo.education.user.dao.impl.mapper.entity.VipMembershipExample;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import jakarta.validation.constraints.NotNull;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class VipMembershipDaoImpl extends AbstractBaseJdbc implements VipMembershipDao {
    @NotNull
    private final VipMembershipMapper vipMembershipMapper;

    @Override
    public int save(VipMembership record) {
        if (record.getId() == null) {
            record.setId(IdWorker.getId());
        }
        return this.vipMembershipMapper.insertSelective(record);
    }

    @Override
    public int deleteById(Long id) {
        return this.vipMembershipMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateById(VipMembership record) {
        record.setGmtCreate(null);
        record.setGmtModified(null);
        return this.vipMembershipMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public VipMembership getById(Long id) {
        return this.vipMembershipMapper.selectByPrimaryKey(id);
    }

    @Override
    public Page<VipMembership> page(int pageCurrent, int pageSize, VipMembershipExample example) {
        int count = this.vipMembershipMapper.countByExample(example);
        pageSize = PageUtil.checkPageSize(pageSize);
        pageCurrent = PageUtil.checkPageCurrent(count, pageSize, pageCurrent);
        int totalPage = PageUtil.countTotalPage(count, pageSize);
        example.setLimitStart(PageUtil.countOffset(pageCurrent, pageSize));
        example.setPageSize(pageSize);
        return new Page<VipMembership>(count, totalPage, pageCurrent, pageSize, this.vipMembershipMapper.selectByExample(example));
    }

    @Override
    public VipMembership getByUserId(Long userId) {
        VipMembershipExample example = new VipMembershipExample();
        VipMembershipExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        List<VipMembership> list = this.vipMembershipMapper.selectByExample(example);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
}
