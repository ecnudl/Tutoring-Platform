package com.roncoo.education.user.dao.impl;

import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.base.page.PageUtil;
import com.roncoo.education.common.tools.IdWorker;
import com.roncoo.education.common.base.AbstractBaseJdbc;
import com.roncoo.education.user.dao.TutorReservationDao;
import com.roncoo.education.user.dao.impl.mapper.TutorReservationMapper;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorReservation;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorReservationExample;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import jakarta.validation.constraints.NotNull;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TutorReservationDaoImpl extends AbstractBaseJdbc implements TutorReservationDao {
    @NotNull
    private final TutorReservationMapper tutorReservationMapper;

    @Override
    public int save(TutorReservation record) {
        if (record.getId() == null) {
            record.setId(IdWorker.getId());
        }
        return this.tutorReservationMapper.insertSelective(record);
    }

    @Override
    public int deleteById(Long id) {
        return this.tutorReservationMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateById(TutorReservation record) {
        record.setGmtCreate(null);
        record.setGmtModified(null);
        return this.tutorReservationMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public TutorReservation getById(Long id) {
        return this.tutorReservationMapper.selectByPrimaryKey(id);
    }

    @Override
    public Page<TutorReservation> page(int pageCurrent, int pageSize, TutorReservationExample example) {
        int count = this.tutorReservationMapper.countByExample(example);
        pageSize = PageUtil.checkPageSize(pageSize);
        pageCurrent = PageUtil.checkPageCurrent(count, pageSize, pageCurrent);
        int totalPage = PageUtil.countTotalPage(count, pageSize);
        example.setLimitStart(PageUtil.countOffset(pageCurrent, pageSize));
        example.setPageSize(pageSize);
        return new Page<TutorReservation>(count, totalPage, pageCurrent, pageSize, this.tutorReservationMapper.selectByExample(example));
    }

    @Override
    public List<TutorReservation> listByStudentUserId(Long studentUserId) {
        TutorReservationExample example = new TutorReservationExample();
        TutorReservationExample.Criteria criteria = example.createCriteria();
        criteria.andStudentUserIdEqualTo(studentUserId);
        return this.tutorReservationMapper.selectByExample(example);
    }

    @Override
    public List<TutorReservation> listByTutorUserId(Long tutorUserId) {
        TutorReservationExample example = new TutorReservationExample();
        TutorReservationExample.Criteria criteria = example.createCriteria();
        criteria.andTutorUserIdEqualTo(tutorUserId);
        return this.tutorReservationMapper.selectByExample(example);
    }
}
