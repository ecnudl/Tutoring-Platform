package com.roncoo.education.system.dao;

import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.system.dao.impl.mapper.entity.SysAnnouncement;

import java.util.List;

public interface SysAnnouncementDao {
    int save(SysAnnouncement record);

    int deleteById(Long id);

    int updateById(SysAnnouncement record);

    SysAnnouncement getById(Long id);

    Page<SysAnnouncement> page(int pageCurrent, int pageSize, String category, Integer statusId, String keyword);

    List<SysAnnouncement> listByCategory(String category, int limit);
}
