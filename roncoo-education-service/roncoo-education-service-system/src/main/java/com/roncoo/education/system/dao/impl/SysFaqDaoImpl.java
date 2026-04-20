package com.roncoo.education.system.dao.impl;

import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.base.page.PageUtil;
import com.roncoo.education.common.tools.IdWorker;
import com.roncoo.education.system.dao.SysFaqDao;
import com.roncoo.education.system.dao.impl.mapper.SysFaqMapper;
import com.roncoo.education.system.dao.impl.mapper.entity.SysFaq;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SysFaqDaoImpl implements SysFaqDao {
    @NotNull
    private final SysFaqMapper mapper;

    @Override
    public int save(SysFaq record) {
        if (record.getId() == null) {
            record.setId(IdWorker.getId());
        }
        if (record.getStatusId() == null) record.setStatusId(1);
        if (record.getSort() == null) record.setSort(0);
        return this.mapper.insert(record);
    }

    @Override
    public int deleteById(Long id) {
        return this.mapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateById(SysFaq record) {
        return this.mapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public SysFaq getById(Long id) {
        return this.mapper.selectByPrimaryKey(id);
    }

    @Override
    public Page<SysFaq> page(int pageCurrent, int pageSize, String category, Integer statusId, String keyword) {
        int count = this.mapper.countForPage(category, statusId, keyword);
        pageSize = PageUtil.checkPageSize(pageSize);
        pageCurrent = PageUtil.checkPageCurrent(count, pageSize, pageCurrent);
        int totalPage = PageUtil.countTotalPage(count, pageSize);
        int offset = PageUtil.countOffset(pageCurrent, pageSize);
        List<SysFaq> list = this.mapper.selectForPage(category, statusId, keyword, offset, pageSize);
        return new Page<>(count, totalPage, pageCurrent, pageSize, list);
    }

    @Override
    public List<SysFaq> listAllActive() {
        return this.mapper.listAllActive();
    }
}
