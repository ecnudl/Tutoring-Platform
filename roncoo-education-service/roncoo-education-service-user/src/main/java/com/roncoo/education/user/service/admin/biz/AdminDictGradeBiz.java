package com.roncoo.education.user.service.admin.biz;

import com.roncoo.education.common.base.BaseBiz;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.user.dao.DictGradeDao;
import com.roncoo.education.user.dao.impl.mapper.entity.DictGrade;
import com.roncoo.education.user.dao.impl.mapper.entity.DictGradeExample;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AdminDictGradeBiz extends BaseBiz {
    @NotNull
    private final DictGradeDao dictGradeDao;

    public Result<?> page(Map<String, Object> req) {
        int pageCurrent = req.get("pageCurrent") != null ? Integer.parseInt(req.get("pageCurrent").toString()) : 1;
        int pageSize = req.get("pageSize") != null ? Integer.parseInt(req.get("pageSize").toString()) : 20;
        DictGradeExample example = new DictGradeExample();
        example.setOrderByClause("sort asc, id asc");
        return Result.success(dictGradeDao.page(pageCurrent, pageSize, example));
    }
    public Result<?> view(Long id) { return Result.success(dictGradeDao.getById(id)); }
    public Result<String> save(Map<String, Object> req) {
        String name = req.get("gradeName") != null ? req.get("gradeName").toString() : "";
        if (!StringUtils.hasText(name)) return Result.error("名称不能为空");
        DictGrade r = new DictGrade();
        r.setGradeName(name);
        r.setGradeLevel(req.get("gradeLevel") != null ? Integer.parseInt(req.get("gradeLevel").toString()) : 0);
        r.setSort(req.get("sort") != null ? Integer.parseInt(req.get("sort").toString()) : 0);
        dictGradeDao.save(r);
        return Result.success("添加成功");
    }
    public Result<String> edit(Map<String, Object> req) {
        Long id = Long.parseLong(req.get("id").toString());
        DictGrade r = new DictGrade();
        r.setId(id);
        if (req.get("gradeName") != null) r.setGradeName(req.get("gradeName").toString());
        if (req.get("gradeLevel") != null) r.setGradeLevel(Integer.parseInt(req.get("gradeLevel").toString()));
        if (req.get("sort") != null) r.setSort(Integer.parseInt(req.get("sort").toString()));
        dictGradeDao.updateById(r);
        return Result.success("修改成功");
    }
    public Result<String> delete(Long id) { dictGradeDao.deleteById(id); return Result.success("删除成功"); }
}
