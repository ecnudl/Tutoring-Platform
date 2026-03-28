package com.roncoo.education.user.service.admin.biz;

import com.roncoo.education.common.base.BaseBiz;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.user.dao.DictSubjectDao;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminDictSubjectBiz extends BaseBiz {
    @NotNull
    private final DictSubjectDao dictSubjectDao;

    public Result<?> page(java.util.Map<String, Object> req) { return Result.success("操作成功"); }
    public Result<?> view(Long id) { return Result.success(dictSubjectDao.getById(id)); }
    public Result<String> save(java.util.Map<String, Object> req) { return Result.success("操作成功"); }
    public Result<String> edit(java.util.Map<String, Object> req) { return Result.success("操作成功"); }
    public Result<String> delete(Long id) { return Result.success("操作成功"); }
}
