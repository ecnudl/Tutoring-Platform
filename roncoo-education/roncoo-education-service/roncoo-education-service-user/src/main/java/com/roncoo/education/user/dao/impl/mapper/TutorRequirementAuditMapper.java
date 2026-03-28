package com.roncoo.education.user.dao.impl.mapper;

import com.roncoo.education.user.dao.impl.mapper.entity.TutorRequirementAudit;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorRequirementAuditExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TutorRequirementAuditMapper {
    int countByExample(TutorRequirementAuditExample example);

    int deleteByExample(TutorRequirementAuditExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TutorRequirementAudit record);

    int insertSelective(TutorRequirementAudit record);

    List<TutorRequirementAudit> selectByExample(TutorRequirementAuditExample example);

    TutorRequirementAudit selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TutorRequirementAudit record, @Param("example") TutorRequirementAuditExample example);

    int updateByExample(@Param("record") TutorRequirementAudit record, @Param("example") TutorRequirementAuditExample example);

    int updateByPrimaryKeySelective(TutorRequirementAudit record);

    int updateByPrimaryKey(TutorRequirementAudit record);
}
