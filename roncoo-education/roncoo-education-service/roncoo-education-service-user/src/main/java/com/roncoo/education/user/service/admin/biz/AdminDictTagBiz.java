package com.roncoo.education.user.service.admin.biz;

import com.roncoo.education.common.base.BaseBiz;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.user.dao.DictTutorTagDao;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminDictTagBiz extends BaseBiz {
    @NotNull
    private final DictTutorTagDao dictTutorTagDao;

    public Result<?> page(java.util.Map<String, Object> req) { return Result.success("操作成功"); }
    public Result<?> view(Long id) { return Result.success(dictTutorTagDao.getById(id)); }
    public Result<String> save(java.util.Map<String, Object> req) { return Result.success("操作成功"); }
    public Result<String> edit(java.util.Map<String, Object> req) { return Result.success("操作成功"); }
    public Result<String> delete(Long id) { return Result.success("操作成功"); }
}
