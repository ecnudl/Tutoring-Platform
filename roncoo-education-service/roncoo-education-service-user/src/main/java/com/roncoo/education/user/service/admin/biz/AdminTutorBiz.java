package com.roncoo.education.user.service.admin.biz;

import com.roncoo.education.common.base.BaseBiz;
import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.base.page.PageUtil;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.user.dao.TutorProfileDao;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorProfile;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorProfileExample;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class AdminTutorBiz extends BaseBiz {
    @NotNull
    private final TutorProfileDao tutorProfileDao;

    public Result<?> page(Map<String, Object> req) {
        int pageCurrent = req.get("pageCurrent") != null ? Integer.parseInt(req.get("pageCurrent").toString()) : 1;
        int pageSize = req.get("pageSize") != null ? Integer.parseInt(req.get("pageSize").toString()) : 20;
        Integer auditStatus = req.get("auditStatus") != null ? Integer.parseInt(req.get("auditStatus").toString()) : null;

        TutorProfileExample example = new TutorProfileExample();
        TutorProfileExample.Criteria c = example.createCriteria();
        if (auditStatus != null) {
            c.andAuditStatusEqualTo(auditStatus);
        }
        if (req.get("keyword") != null && StringUtils.hasText(req.get("keyword").toString())) {
            c.andRealNameLike(PageUtil.like(req.get("keyword").toString()));
        }
        example.setOrderByClause("gmt_create desc");
        Page<TutorProfile> page = tutorProfileDao.page(pageCurrent, pageSize, example);
        return Result.success(page);
    }

    public Result<?> view(Long id) {
        TutorProfile profile = tutorProfileDao.getById(id);
        if (profile == null) {
            return Result.error("教员不存在");
        }
        return Result.success(profile);
    }

    public Result<String> edit(Map<String, Object> req) {
        Long id = Long.parseLong(req.get("id").toString());
        TutorProfile profile = tutorProfileDao.getById(id);
        if (profile == null) {
            return Result.error("教员不存在");
        }
        TutorProfile update = new TutorProfile();
        update.setId(id);
        if (req.containsKey("sort")) {
            update.setSort(Integer.parseInt(req.get("sort").toString()));
        }
        if (req.containsKey("statusId")) {
            update.setStatusId(Integer.parseInt(req.get("statusId").toString()));
        }
        tutorProfileDao.updateById(update);
        return Result.success("操作成功");
    }

    public Result<String> delete(Long id) {
        TutorProfile profile = tutorProfileDao.getById(id);
        if (profile == null) {
            return Result.error("教员不存在");
        }
        tutorProfileDao.deleteById(id);
        return Result.success("删除成功");
    }
}
