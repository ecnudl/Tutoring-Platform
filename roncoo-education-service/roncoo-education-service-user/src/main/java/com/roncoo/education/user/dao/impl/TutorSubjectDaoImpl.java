package com.roncoo.education.user.dao.impl;

import com.roncoo.education.common.tools.IdWorker;
import com.roncoo.education.common.base.AbstractBaseJdbc;
import com.roncoo.education.user.dao.TutorSubjectDao;
import com.roncoo.education.user.dao.impl.mapper.TutorSubjectMapper;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorSubject;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorSubjectExample;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import jakarta.validation.constraints.NotNull;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TutorSubjectDaoImpl extends AbstractBaseJdbc implements TutorSubjectDao {
    @NotNull
    private final TutorSubjectMapper tutorSubjectMapper;

    @Override
    public int save(TutorSubject record) {
        if (record.getId() == null) {
            record.setId(IdWorker.getId());
        }
        return this.tutorSubjectMapper.insertSelective(record);
    }

    @Override
    public int deleteById(Long id) {
        return this.tutorSubjectMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<TutorSubject> listByTutorId(Long tutorId) {
        TutorSubjectExample example = new TutorSubjectExample();
        TutorSubjectExample.Criteria criteria = example.createCriteria();
        criteria.andTutorIdEqualTo(tutorId).andStatusIdEqualTo(1);
        return this.tutorSubjectMapper.selectByExample(example);
    }

    @Override
    public List<TutorSubject> listBySubjectId(Long subjectId) {
        TutorSubjectExample example = new TutorSubjectExample();
        TutorSubjectExample.Criteria criteria = example.createCriteria();
        criteria.andSubjectIdEqualTo(subjectId).andStatusIdEqualTo(1);
        return this.tutorSubjectMapper.selectByExample(example);
    }

    @Override
    public int deleteByTutorId(Long tutorId) {
        TutorSubjectExample example = new TutorSubjectExample();
        TutorSubjectExample.Criteria criteria = example.createCriteria();
        criteria.andTutorIdEqualTo(tutorId);
        return this.tutorSubjectMapper.deleteByExample(example);
    }
}
