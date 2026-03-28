package com.roncoo.education.user.dao.impl;

import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.base.page.PageUtil;
import com.roncoo.education.common.tools.IdWorker;
import com.roncoo.education.common.base.AbstractBaseJdbc;
import com.roncoo.education.user.dao.SmsLogDao;
import com.roncoo.education.user.dao.impl.mapper.SmsLogMapper;
import com.roncoo.education.user.dao.impl.mapper.entity.SmsLog;
import com.roncoo.education.user.dao.impl.mapper.entity.SmsLogExample;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import jakarta.validation.constraints.NotNull;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SmsLogDaoImpl extends AbstractBaseJdbc implements SmsLogDao {
    @NotNull
    private final SmsLogMapper smsLogMapper;

    @Override
    public int save(SmsLog record) {
        if (record.getId() == null) {
            record.setId(IdWorker.getId());
        }
        return this.smsLogMapper.insertSelective(record);
    }

    @Override
    public int deleteById(Long id) {
        return this.smsLogMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateById(SmsLog record) {
        record.setGmtCreate(null);
        record.setGmtModified(null);
        return this.smsLogMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public SmsLog getById(Long id) {
        return this.smsLogMapper.selectByPrimaryKey(id);
    }

    @Override
    public Page<SmsLog> page(int pageCurrent, int pageSize, SmsLogExample example) {
        int count = this.smsLogMapper.countByExample(example);
        pageSize = PageUtil.checkPageSize(pageSize);
        pageCurrent = PageUtil.checkPageCurrent(count, pageSize, pageCurrent);
        int totalPage = PageUtil.countTotalPage(count, pageSize);
        example.setLimitStart(PageUtil.countOffset(pageCurrent, pageSize));
        example.setPageSize(pageSize);
        return new Page<SmsLog>(count, totalPage, pageCurrent, pageSize, this.smsLogMapper.selectByExample(example));
    }

    @Override
    public List<SmsLog> listByMobile(String mobile) {
        SmsLogExample example = new SmsLogExample();
        SmsLogExample.Criteria criteria = example.createCriteria();
        criteria.andMobileEqualTo(mobile);
        return this.smsLogMapper.selectByExample(example);
    }
}
