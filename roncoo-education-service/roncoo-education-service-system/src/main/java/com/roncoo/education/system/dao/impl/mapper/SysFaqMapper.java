package com.roncoo.education.system.dao.impl.mapper;

import com.roncoo.education.system.dao.impl.mapper.entity.SysFaq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysFaqMapper {
    int insert(SysFaq record);

    int deleteByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysFaq record);

    SysFaq selectByPrimaryKey(Long id);

    int countForPage(@Param("category") String category,
                     @Param("statusId") Integer statusId,
                     @Param("keyword") String keyword);

    List<SysFaq> selectForPage(@Param("category") String category,
                               @Param("statusId") Integer statusId,
                               @Param("keyword") String keyword,
                               @Param("offset") int offset,
                               @Param("limit") int limit);

    List<SysFaq> listAllActive();
}
