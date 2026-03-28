package com.roncoo.education.user.dao.impl;

import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.base.page.PageUtil;
import com.roncoo.education.common.tools.IdWorker;
import com.roncoo.education.common.base.AbstractBaseJdbc;
import com.roncoo.education.user.dao.FavoriteDao;
import com.roncoo.education.user.dao.impl.mapper.FavoriteMapper;
import com.roncoo.education.user.dao.impl.mapper.entity.Favorite;
import com.roncoo.education.user.dao.impl.mapper.entity.FavoriteExample;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import jakarta.validation.constraints.NotNull;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FavoriteDaoImpl extends AbstractBaseJdbc implements FavoriteDao {
    @NotNull
    private final FavoriteMapper favoriteMapper;

    @Override
    public int save(Favorite record) {
        if (record.getId() == null) {
            record.setId(IdWorker.getId());
        }
        return this.favoriteMapper.insertSelective(record);
    }

    @Override
    public int deleteById(Long id) {
        return this.favoriteMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateById(Favorite record) {
        record.setGmtCreate(null);
        record.setGmtModified(null);
        return this.favoriteMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public Favorite getById(Long id) {
        return this.favoriteMapper.selectByPrimaryKey(id);
    }

    @Override
    public Page<Favorite> page(int pageCurrent, int pageSize, FavoriteExample example) {
        int count = this.favoriteMapper.countByExample(example);
        pageSize = PageUtil.checkPageSize(pageSize);
        pageCurrent = PageUtil.checkPageCurrent(count, pageSize, pageCurrent);
        int totalPage = PageUtil.countTotalPage(count, pageSize);
        example.setLimitStart(PageUtil.countOffset(pageCurrent, pageSize));
        example.setPageSize(pageSize);
        return new Page<Favorite>(count, totalPage, pageCurrent, pageSize, this.favoriteMapper.selectByExample(example));
    }

    @Override
    public List<Favorite> listByUserId(Long userId) {
        FavoriteExample example = new FavoriteExample();
        FavoriteExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        return this.favoriteMapper.selectByExample(example);
    }

    @Override
    public Favorite getByUserIdAndTarget(Long userId, Integer targetType, Long targetId) {
        FavoriteExample example = new FavoriteExample();
        FavoriteExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        criteria.andTargetTypeEqualTo(targetType);
        criteria.andTargetIdEqualTo(targetId);
        List<Favorite> list = this.favoriteMapper.selectByExample(example);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
}
