package com.roncoo.education.user.service.admin.biz;

import com.roncoo.education.common.base.BaseBiz;
import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.base.page.PageUtil;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.user.dao.StudentProfileDao;
import com.roncoo.education.user.dao.impl.mapper.entity.StudentProfile;
import com.roncoo.education.user.dao.impl.mapper.entity.StudentProfileExample;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class AdminStudentBiz extends BaseBiz {
    @NotNull
    private final StudentProfileDao studentProfileDao;

    public Result<?> page(Map<String, Object> req) {
        int pageCurrent = req.get("pageCurrent") != null ? Integer.parseInt(req.get("pageCurrent").toString()) : 1;
        int pageSize = req.get("pageSize") != null ? Integer.parseInt(req.get("pageSize").toString()) : 20;

        StudentProfileExample example = new StudentProfileExample();
        StudentProfileExample.Criteria c = example.createCriteria();
        if (req.get("keyword") != null && StringUtils.hasText(req.get("keyword").toString())) {
            c.andRealNameLike(PageUtil.like(req.get("keyword").toString()));
        }
        example.setOrderByClause("gmt_create desc");
        Page<StudentProfile> page = studentProfileDao.page(pageCurrent, pageSize, example);
        return Result.success(page);
    }

    public Result<?> view(Long id) {
        StudentProfile profile = studentProfileDao.getById(id);
        if (profile == null) {
            return Result.error("学员不存在");
        }
        return Result.success(profile);
    }
}
