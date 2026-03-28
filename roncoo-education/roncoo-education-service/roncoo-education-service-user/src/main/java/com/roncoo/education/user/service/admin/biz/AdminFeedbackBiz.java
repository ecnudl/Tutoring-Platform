package com.roncoo.education.user.service.admin.biz;

import com.roncoo.education.common.base.BaseBiz;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.user.dao.FeedbackDao;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminFeedbackBiz extends BaseBiz {
    @NotNull
    private final FeedbackDao feedbackDao;

    public Result<?> page(java.util.Map<String, Object> req) { return Result.success("操作成功"); }
    public Result<?> view(Long id) { return Result.success(feedbackDao.getById(id)); }
    public Result<String> reply(java.util.Map<String, Object> req) { return Result.success("操作成功"); }
}
