package com.roncoo.education.user.dao.impl;

import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.base.page.PageUtil;
import com.roncoo.education.common.tools.IdWorker;
import com.roncoo.education.common.base.AbstractBaseJdbc;
import com.roncoo.education.user.dao.TutorRequirementDao;
import com.roncoo.education.user.dao.impl.mapper.TutorRequirementMapper;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorRequirement;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorRequirementExample;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import jakarta.validation.constraints.NotNull;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TutorRequirementDaoImpl extends AbstractBaseJdbc implements TutorRequirementDao {
    @NotNull
    private final TutorRequirementMapper tutorRequirementMapper;

    @Override
    public int save(TutorRequirement record) {
        if (record.getId() == null) {
            record.setId(IdWorker.getId());
        }
        return this.tutorRequirementMapper.insertSelective(record);
    }

    @Override
    public int deleteById(Long id) {
        return this.tutorRequirementMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateById(TutorRequirement record) {
        record.setGmtCreate(null);
        record.setGmtModified(null);
        return this.tutorRequirementMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public TutorRequirement getById(Long id) {
        return this.tutorRequirementMapper.selectByPrimaryKey(id);
    }

    /** 分页. selectByExample 不加载 BLOB (schedule/requirement_detail), 用主键回查一次 */
    @Override
    public Page<TutorRequirement> page(int pageCurrent, int pageSize, TutorRequirementExample example) {
        int count = this.tutorRequirementMapper.countByExample(example);
        pageSize = PageUtil.checkPageSize(pageSize);
        pageCurrent = PageUtil.checkPageCurrent(count, pageSize, pageCurrent);
        int totalPage = PageUtil.countTotalPage(count, pageSize);
        example.setLimitStart(PageUtil.countOffset(pageCurrent, pageSize));
        example.setPageSize(pageSize);
        java.util.List<TutorRequirement> shallow = this.tutorRequirementMapper.selectByExample(example);
        java.util.List<TutorRequirement> full = new java.util.ArrayList<>(shallow.size());
        for (TutorRequirement r : shallow) {
            TutorRequirement withBlobs = this.tutorRequirementMapper.selectByPrimaryKey(r.getId());
            full.add(withBlobs != null ? withBlobs : r);
        }
        return new Page<TutorRequirement>(count, totalPage, pageCurrent, pageSize, full);
    }

    @Override
    public List<TutorRequirement> listByUserId(Long userId) {
        TutorRequirementExample example = new TutorRequirementExample();
        TutorRequirementExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        return this.tutorRequirementMapper.selectByExample(example);
    }

    @Override
    public List<TutorRequirement> listByReqStatus(Integer reqStatus) {
        TutorRequirementExample example = new TutorRequirementExample();
        TutorRequirementExample.Criteria criteria = example.createCriteria();
        criteria.andReqStatusEqualTo(reqStatus);
        return this.tutorRequirementMapper.selectByExample(example);
    }

    @Override
    public TutorRequirement getByDisplayNo(String displayNo) {
        TutorRequirementExample example = new TutorRequirementExample();
        TutorRequirementExample.Criteria criteria = example.createCriteria();
        criteria.andDisplayNoEqualTo(displayNo);
        // 用 WithBLOBs 把 schedule / requirement_detail / student_info /
        // tutor_request / traffic_info 一并加载, 否则公开详情页这些字段会显示空
        List<TutorRequirement> list = this.tutorRequirementMapper.selectByExampleWithBLOBs(example);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
}
