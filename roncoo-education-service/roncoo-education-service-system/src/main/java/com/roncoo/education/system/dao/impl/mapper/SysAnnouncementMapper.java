package com.roncoo.education.system.dao.impl.mapper;

import com.roncoo.education.system.dao.impl.mapper.entity.SysAnnouncement;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysAnnouncementMapper {
    int insert(SysAnnouncement record);

    int deleteByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysAnnouncement record);

    SysAnnouncement selectByPrimaryKey(Long id);

    int countForPage(@Param("category") String category,
                     @Param("statusId") Integer statusId,
                     @Param("keyword") String keyword);

    List<SysAnnouncement> selectForPage(@Param("category") String category,
                                        @Param("statusId") Integer statusId,
                                        @Param("keyword") String keyword,
                                        @Param("offset") int offset,
                                        @Param("limit") int limit);

    List<SysAnnouncement> listByCategory(@Param("category") String category,
                                         @Param("limit") int limit);
}
