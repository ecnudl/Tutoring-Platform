package com.roncoo.education.user.dao.impl.mapper;

import com.roncoo.education.user.dao.impl.mapper.entity.VipMembership;
import com.roncoo.education.user.dao.impl.mapper.entity.VipMembershipExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface VipMembershipMapper {
    int countByExample(VipMembershipExample example);

    int deleteByExample(VipMembershipExample example);

    int deleteByPrimaryKey(Long id);

    int insert(VipMembership record);

    int insertSelective(VipMembership record);

    List<VipMembership> selectByExample(VipMembershipExample example);

    VipMembership selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") VipMembership record, @Param("example") VipMembershipExample example);

    int updateByExample(@Param("record") VipMembership record, @Param("example") VipMembershipExample example);

    int updateByPrimaryKeySelective(VipMembership record);

    int updateByPrimaryKey(VipMembership record);
}
