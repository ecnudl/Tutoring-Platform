package com.roncoo.education.user.dao.impl;

import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.base.page.PageUtil;
import com.roncoo.education.common.tools.IdWorker;
import com.roncoo.education.common.base.AbstractBaseJdbc;
import com.roncoo.education.user.dao.FootprintDao;
import com.roncoo.education.user.dao.impl.mapper.FootprintMapper;
import com.roncoo.education.user.dao.impl.mapper.entity.Footprint;
import com.roncoo.education.user.dao.impl.mapper.entity.FootprintExample;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import jakarta.validation.constraints.NotNull;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FootprintDaoImpl extends AbstractBaseJdbc implements FootprintDao {
    @NotNull
    private final FootprintMapper footprintMapper;

    @Override
    public int save(Footprint record) {
        if (record.getId() == null) {
            record.setId(IdWorker.getId());
        }
        return this.footprintMapper.insertSelective(record);
    }

    @Override
    public int deleteById(Long id) {
        return this.footprintMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateById(Footprint record) {
        record.setGmtCreate(null);
        record.setGmtModified(null);
        return this.footprintMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public Footprint getById(Long id) {
        return this.footprintMapper.selectByPrimaryKey(id);
    }

    @Override
    public Page<Footprint> page(int pageCurrent, int pageSize, FootprintExample example) {
        int count = this.footprintMapper.countByExample(example);
        pageSize = PageUtil.checkPageSize(pageSize);
        pageCurrent = PageUtil.checkPageCurrent(count, pageSize, pageCurrent);
        int totalPage = PageUtil.countTotalPage(count, pageSize);
        example.setLimitStart(PageUtil.countOffset(pageCurrent, pageSize));
        example.setPageSize(pageSize);
        return new Page<Footprint>(count, totalPage, pageCurrent, pageSize, this.footprintMapper.selectByExample(example));
    }

    @Override
    public List<Footprint> listByUserId(Long userId) {
        FootprintExample example = new FootprintExample();
        FootprintExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        return this.footprintMapper.selectByExample(example);
    }
}
