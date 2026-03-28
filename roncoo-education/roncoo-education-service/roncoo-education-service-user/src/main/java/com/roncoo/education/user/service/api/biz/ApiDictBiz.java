package com.roncoo.education.user.service.api.biz;

import com.roncoo.education.common.base.BaseBiz;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.common.tools.BeanUtil;
import com.roncoo.education.user.dao.DictSubjectDao;
import com.roncoo.education.user.dao.DictGradeDao;
import com.roncoo.education.user.dao.DictTutorTagDao;
import com.roncoo.education.user.service.api.resp.DictSubjectResp;
import com.roncoo.education.user.service.api.resp.DictGradeResp;
import com.roncoo.education.user.service.api.resp.DictTutorTagResp;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ApiDictBiz extends BaseBiz {
    @NotNull
    private final DictSubjectDao dictSubjectDao;
    @NotNull
    private final DictGradeDao dictGradeDao;
    @NotNull
    private final DictTutorTagDao dictTutorTagDao;

    public Result<List<DictSubjectResp>> subjectList() {
        return Result.success(BeanUtil.copyProperties(dictSubjectDao.listAll(), DictSubjectResp.class));
    }
    public Result<List<DictGradeResp>> gradeList(Integer gradeLevel) {
        if (gradeLevel != null) {
            return Result.success(BeanUtil.copyProperties(dictGradeDao.listByGradeLevel(gradeLevel), DictGradeResp.class));
        }
        return Result.success(BeanUtil.copyProperties(dictGradeDao.listAll(), DictGradeResp.class));
    }
    public Result<List<DictTutorTagResp>> tagList() {
        return Result.success(BeanUtil.copyProperties(dictTutorTagDao.listAll(), DictTutorTagResp.class));
    }
}
