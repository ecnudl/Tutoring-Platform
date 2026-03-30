package com.roncoo.education.user.dao.impl;

import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.base.page.PageUtil;
import com.roncoo.education.common.tools.IdWorker;
import com.roncoo.education.common.base.AbstractBaseJdbc;
import com.roncoo.education.user.dao.StudentProfileDao;
import com.roncoo.education.user.dao.impl.mapper.StudentProfileMapper;
import com.roncoo.education.user.dao.impl.mapper.entity.StudentProfile;
import com.roncoo.education.user.dao.impl.mapper.entity.StudentProfileExample;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import jakarta.validation.constraints.NotNull;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StudentProfileDaoImpl extends AbstractBaseJdbc implements StudentProfileDao {
    @NotNull
    private final StudentProfileMapper studentProfileMapper;

    @Override
    public int save(StudentProfile record) {
        if (record.getId() == null) {
            record.setId(IdWorker.getId());
        }
        return this.studentProfileMapper.insertSelective(record);
    }

    @Override
    public int deleteById(Long id) {
        return this.studentProfileMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateById(StudentProfile record) {
        record.setGmtCreate(null);
        record.setGmtModified(null);
        return this.studentProfileMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public StudentProfile getById(Long id) {
        return this.studentProfileMapper.selectByPrimaryKey(id);
    }

    @Override
    public Page<StudentProfile> page(int pageCurrent, int pageSize, StudentProfileExample example) {
        int count = this.studentProfileMapper.countByExample(example);
        pageSize = PageUtil.checkPageSize(pageSize);
        pageCurrent = PageUtil.checkPageCurrent(count, pageSize, pageCurrent);
        int totalPage = PageUtil.countTotalPage(count, pageSize);
        example.setLimitStart(PageUtil.countOffset(pageCurrent, pageSize));
        example.setPageSize(pageSize);
        return new Page<StudentProfile>(count, totalPage, pageCurrent, pageSize, this.studentProfileMapper.selectByExample(example));
    }

    @Override
    public StudentProfile getByUserId(Long userId) {
        StudentProfileExample example = new StudentProfileExample();
        StudentProfileExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        List<StudentProfile> list = this.studentProfileMapper.selectByExample(example);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
}
