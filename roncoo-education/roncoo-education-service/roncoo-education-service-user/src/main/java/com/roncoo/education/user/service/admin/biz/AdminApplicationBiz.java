package com.roncoo.education.user.service.admin.biz;

import com.roncoo.education.common.base.BaseBiz;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.user.dao.TutorApplicationDao;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminApplicationBiz extends BaseBiz {
    @NotNull
    private final TutorApplicationDao tutorApplicationDao;

    public Result<?> page(java.util.Map<String, Object> req) { return Result.success("操作成功"); }
    public Result<?> view(Long id) { return Result.success(tutorApplicationDao.getById(id)); }
}
