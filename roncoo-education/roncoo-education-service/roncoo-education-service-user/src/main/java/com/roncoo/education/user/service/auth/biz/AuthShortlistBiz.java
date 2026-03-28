package com.roncoo.education.user.service.auth.biz;

import com.roncoo.education.common.base.BaseBiz;
import com.roncoo.education.common.base.ThreadContext;
import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.base.page.PageUtil;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.common.tools.BeanUtil;
import com.roncoo.education.user.dao.TutorShortlistDao;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AuthShortlistBiz extends BaseBiz {
    @NotNull
    private final TutorShortlistDao tutorShortlistDao;

    public Result<?> page(Map<String, Object> req) {
        Long userId = ThreadContext.userId();
        return Result.success("操作成功");
    }

    public Result<String> action(Map<String, Object> req) {
        return Result.success("操作成功");
    }

    public Result<String> add(Map<String, Object> req) { return Result.success("操作成功"); }
    public Result<String> remove(Long id) { return Result.success("操作成功"); }
}
