package com.roncoo.education.user.dao;

import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.user.dao.impl.mapper.entity.SmsLog;
import com.roncoo.education.user.dao.impl.mapper.entity.SmsLogExample;

import java.util.List;

public interface SmsLogDao {
    int save(SmsLog record);

    int deleteById(Long id);

    int updateById(SmsLog record);

    SmsLog getById(Long id);

    Page<SmsLog> page(int pageCurrent, int pageSize, SmsLogExample example);

    List<SmsLog> listByMobile(String mobile);
}
