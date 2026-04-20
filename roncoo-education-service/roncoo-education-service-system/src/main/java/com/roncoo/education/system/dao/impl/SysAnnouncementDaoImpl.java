package com.roncoo.education.system.dao.impl;

import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.base.page.PageUtil;
import com.roncoo.education.common.tools.IdWorker;
import com.roncoo.education.system.dao.SysAnnouncementDao;
import com.roncoo.education.system.dao.impl.mapper.SysAnnouncementMapper;
import com.roncoo.education.system.dao.impl.mapper.entity.SysAnnouncement;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SysAnnouncementDaoImpl implements SysAnnouncementDao {
    @NotNull
    private final SysAnnouncementMapper mapper;

    @Override
    public int save(SysAnnouncement record) {
        if (record.getId() == null) {
            record.setId(IdWorker.getId());
        }
        if (record.getStatusId() == null) record.setStatusId(1);
        if (record.getSort() == null) record.setSort(0);
        if (record.getIsTop() == null) record.setIsTop(0);
        return this.mapper.insert(record);
    }

    @Override
    public int deleteById(Long id) {
        return this.mapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateById(SysAnnouncement record) {
        return this.mapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public SysAnnouncement getById(Long id) {
        return this.mapper.selectByPrimaryKey(id);
    }

    @Override
    public Page<SysAnnouncement> page(int pageCurrent, int pageSize, String category, Integer statusId, String keyword) {
        int count = this.mapper.countForPage(category, statusId, keyword);
        pageSize = PageUtil.checkPageSize(pageSize);
        pageCurrent = PageUtil.checkPageCurrent(count, pageSize, pageCurrent);
        int totalPage = PageUtil.countTotalPage(count, pageSize);
        int offset = PageUtil.countOffset(pageCurrent, pageSize);
        List<SysAnnouncement> list = this.mapper.selectForPage(category, statusId, keyword, offset, pageSize);
        return new Page<>(count, totalPage, pageCurrent, pageSize, list);
    }

    @Override
    public List<SysAnnouncement> listByCategory(String category, int limit) {
        return this.mapper.listByCategory(category, limit);
    }
}
