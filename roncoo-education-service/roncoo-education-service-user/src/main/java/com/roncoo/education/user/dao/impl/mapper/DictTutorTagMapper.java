package com.roncoo.education.user.dao.impl.mapper;

import com.roncoo.education.user.dao.impl.mapper.entity.DictTutorTag;
import com.roncoo.education.user.dao.impl.mapper.entity.DictTutorTagExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DictTutorTagMapper {
    int countByExample(DictTutorTagExample example);

    int deleteByExample(DictTutorTagExample example);

    int deleteByPrimaryKey(Long id);

    int insert(DictTutorTag record);

    int insertSelective(DictTutorTag record);

    List<DictTutorTag> selectByExample(DictTutorTagExample example);

    DictTutorTag selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") DictTutorTag record, @Param("example") DictTutorTagExample example);

    int updateByExample(@Param("record") DictTutorTag record, @Param("example") DictTutorTagExample example);

    int updateByPrimaryKeySelective(DictTutorTag record);

    int updateByPrimaryKey(DictTutorTag record);
}
