package com.roncoo.education.user.dao.impl;

import com.roncoo.education.common.tools.IdWorker;
import com.roncoo.education.common.base.AbstractBaseJdbc;
import com.roncoo.education.user.dao.TutorTeachingAreaDao;
import com.roncoo.education.user.dao.impl.mapper.TutorTeachingAreaMapper;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorTeachingArea;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorTeachingAreaExample;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import jakarta.validation.constraints.NotNull;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TutorTeachingAreaDaoImpl extends AbstractBaseJdbc implements TutorTeachingAreaDao {
    @NotNull
    private final TutorTeachingAreaMapper tutorTeachingAreaMapper;

    @Override
    public int save(TutorTeachingArea record) {
        if (record.getId() == null) {
            record.setId(IdWorker.getId());
        }
        return this.tutorTeachingAreaMapper.insertSelective(record);
    }

    @Override
    public int deleteById(Long id) {
        return this.tutorTeachingAreaMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<TutorTeachingArea> listByTutorId(Long tutorId) {
        TutorTeachingAreaExample example = new TutorTeachingAreaExample();
        TutorTeachingAreaExample.Criteria criteria = example.createCriteria();
        criteria.andTutorIdEqualTo(tutorId);
        return this.tutorTeachingAreaMapper.selectByExample(example);
    }

    @Override
    public List<TutorTeachingArea> listByCityIdAndDistrictId(Long cityId, Long districtId) {
        TutorTeachingAreaExample example = new TutorTeachingAreaExample();
        TutorTeachingAreaExample.Criteria criteria = example.createCriteria();
        criteria.andCityIdEqualTo(cityId);
        if (districtId != null) {
            criteria.andDistrictIdEqualTo(districtId);
        }
        return this.tutorTeachingAreaMapper.selectByExample(example);
    }

    @Override
    public int deleteByTutorId(Long tutorId) {
        TutorTeachingAreaExample example = new TutorTeachingAreaExample();
        TutorTeachingAreaExample.Criteria criteria = example.createCriteria();
        criteria.andTutorIdEqualTo(tutorId);
        return this.tutorTeachingAreaMapper.deleteByExample(example);
    }
}
