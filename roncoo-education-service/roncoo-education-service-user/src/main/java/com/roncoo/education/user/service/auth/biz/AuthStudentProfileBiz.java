package com.roncoo.education.user.service.auth.biz;

import com.roncoo.education.common.base.BaseBiz;
import com.roncoo.education.common.base.ThreadContext;
import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.base.page.PageUtil;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.common.tools.BeanUtil;
import com.roncoo.education.user.dao.StudentProfileDao;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AuthStudentProfileBiz extends BaseBiz {
    @NotNull
    private final StudentProfileDao studentProfileDao;

    public Result<?> page(Map<String, Object> req) {
        Long userId = ThreadContext.userId();
        return Result.success("操作成功");
    }

    public Result<String> action(Map<String, Object> req) {
        return Result.success("操作成功");
    }

    public Result<?> view() { return Result.success(studentProfileDao.getByUserId(ThreadContext.userId())); }
    public Result<String> save(com.roncoo.education.user.service.auth.req.AuthStudentProfileSaveReq req) {
        Long userId = ThreadContext.userId();
        com.roncoo.education.user.dao.impl.mapper.entity.StudentProfile profile = studentProfileDao.getByUserId(userId);
        if (profile == null) {
            return Result.error("学员资料不存在");
        }
        com.roncoo.education.user.dao.impl.mapper.entity.StudentProfile update = BeanUtil.copyProperties(req, com.roncoo.education.user.dao.impl.mapper.entity.StudentProfile.class);
        update.setId(profile.getId());
        studentProfileDao.updateById(update);
        return Result.success("保存成功");
    }
}
