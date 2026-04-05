package com.roncoo.education.user.dao.impl.mapper;

import com.roncoo.education.user.dao.impl.mapper.entity.PriceReference;
import com.roncoo.education.user.dao.impl.mapper.entity.PriceReferenceExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PriceReferenceMapper {
    int countByExample(PriceReferenceExample example);

    int deleteByExample(PriceReferenceExample example);

    int deleteByPrimaryKey(Long id);

    int insert(PriceReference record);

    int insertSelective(PriceReference record);

    List<PriceReference> selectByExample(PriceReferenceExample example);

    PriceReference selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") PriceReference record, @Param("example") PriceReferenceExample example);

    int updateByExample(@Param("record") PriceReference record, @Param("example") PriceReferenceExample example);

    int updateByPrimaryKeySelective(PriceReference record);

    int updateByPrimaryKey(PriceReference record);
}
