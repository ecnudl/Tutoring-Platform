package com.roncoo.education.user.dao.impl.mapper;

import com.roncoo.education.user.dao.impl.mapper.entity.TutorAuditRecord;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorAuditRecordExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TutorAuditRecordMapper {
    int countByExample(TutorAuditRecordExample example);

    int deleteByExample(TutorAuditRecordExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TutorAuditRecord record);

    int insertSelective(TutorAuditRecord record);

    List<TutorAuditRecord> selectByExample(TutorAuditRecordExample example);

    TutorAuditRecord selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TutorAuditRecord record, @Param("example") TutorAuditRecordExample example);

    int updateByExample(@Param("record") TutorAuditRecord record, @Param("example") TutorAuditRecordExample example);

    int updateByPrimaryKeySelective(TutorAuditRecord record);

    int updateByPrimaryKey(TutorAuditRecord record);
}
