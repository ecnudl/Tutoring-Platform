package com.roncoo.education.user.service.admin.biz;

import com.roncoo.education.common.base.BaseBiz;
import com.roncoo.education.common.base.page.PageUtil;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.user.dao.DictSubjectDao;
import com.roncoo.education.user.dao.impl.mapper.entity.DictSubject;
import com.roncoo.education.user.dao.impl.mapper.entity.DictSubjectExample;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AdminDictSubjectBiz extends BaseBiz {
    @NotNull
    private final DictSubjectDao dictSubjectDao;

    public Result<?> page(Map<String, Object> req) {
        int pageCurrent = req.get("pageCurrent") != null ? Integer.parseInt(req.get("pageCurrent").toString()) : 1;
        int pageSize = req.get("pageSize") != null ? Integer.parseInt(req.get("pageSize").toString()) : 20;
        DictSubjectExample example = new DictSubjectExample();
        example.setOrderByClause("sort asc, id asc");
        return Result.success(dictSubjectDao.page(pageCurrent, pageSize, example));
    }
    public Result<?> view(Long id) { return Result.success(dictSubjectDao.getById(id)); }
    public Result<String> save(Map<String, Object> req) {
        String name = req.get("subjectName") != null ? req.get("subjectName").toString() : "";
        if (!StringUtils.hasText(name)) return Result.error("名称不能为空");
        DictSubject r = new DictSubject();
        r.setSubjectName(name);
        r.setSort(req.get("sort") != null ? Integer.parseInt(req.get("sort").toString()) : 0);
        dictSubjectDao.save(r);
        return Result.success("添加成功");
    }
    public Result<String> edit(Map<String, Object> req) {
        Long id = Long.parseLong(req.get("id").toString());
        DictSubject r = new DictSubject();
        r.setId(id);
        if (req.get("subjectName") != null) r.setSubjectName(req.get("subjectName").toString());
        if (req.get("sort") != null) r.setSort(Integer.parseInt(req.get("sort").toString()));
        dictSubjectDao.updateById(r);
        return Result.success("修改成功");
    }
    public Result<String> delete(Long id) { dictSubjectDao.deleteById(id); return Result.success("删除成功"); }
}
