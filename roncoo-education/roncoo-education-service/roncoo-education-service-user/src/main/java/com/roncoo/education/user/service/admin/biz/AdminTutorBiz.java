package com.roncoo.education.user.service.admin.biz;

import com.roncoo.education.common.base.BaseBiz;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.user.dao.TutorProfileDao;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminTutorBiz extends BaseBiz {
    @NotNull
    private final TutorProfileDao tutorProfileDao;

    public Result<?> page(java.util.Map<String, Object> req) { return Result.success("操作成功"); }
    public Result<?> view(Long id) { return Result.success(tutorProfileDao.getById(id)); }
    public Result<String> edit(java.util.Map<String, Object> req) { return Result.success("操作成功"); }
    public Result<String> delete(Long id) { return Result.success("操作成功"); }
}
