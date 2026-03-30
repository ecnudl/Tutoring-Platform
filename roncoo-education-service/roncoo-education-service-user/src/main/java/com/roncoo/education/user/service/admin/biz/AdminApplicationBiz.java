package com.roncoo.education.user.service.admin.biz;

import com.roncoo.education.common.base.BaseBiz;
import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.user.dao.TutorApplicationDao;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorApplication;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorApplicationExample;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class AdminApplicationBiz extends BaseBiz {
    @NotNull
    private final TutorApplicationDao tutorApplicationDao;

    public Result<?> page(Map<String, Object> req) {
        int pageCurrent = req.get("pageCurrent") != null ? Integer.parseInt(req.get("pageCurrent").toString()) : 1;
        int pageSize = req.get("pageSize") != null ? Integer.parseInt(req.get("pageSize").toString()) : 20;
        Integer appStatus = req.get("appStatus") != null ? Integer.parseInt(req.get("appStatus").toString()) : null;

        TutorApplicationExample example = new TutorApplicationExample();
        TutorApplicationExample.Criteria c = example.createCriteria();
        if (appStatus != null) {
            c.andAppStatusEqualTo(appStatus);
        }
        if (req.get("requirementId") != null) {
            c.andRequirementIdEqualTo(Long.parseLong(req.get("requirementId").toString()));
        }
        example.setOrderByClause("gmt_create desc");
        Page<TutorApplication> page = tutorApplicationDao.page(pageCurrent, pageSize, example);
        return Result.success(page);
    }

    public Result<?> view(Long id) {
        TutorApplication app = tutorApplicationDao.getById(id);
        if (app == null) {
            return Result.error("申请不存在");
        }
        return Result.success(app);
    }
}
