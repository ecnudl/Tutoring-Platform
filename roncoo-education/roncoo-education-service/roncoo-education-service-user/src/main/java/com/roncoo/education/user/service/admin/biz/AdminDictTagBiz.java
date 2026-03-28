package com.roncoo.education.user.service.admin.biz;

import com.roncoo.education.common.base.BaseBiz;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.user.dao.DictTutorTagDao;
import com.roncoo.education.user.dao.impl.mapper.entity.DictTutorTag;
import com.roncoo.education.user.dao.impl.mapper.entity.DictTutorTagExample;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AdminDictTagBiz extends BaseBiz {
    @NotNull
    private final DictTutorTagDao dictTutorTagDao;

    public Result<?> page(Map<String, Object> req) {
        int pageCurrent = req.get("pageCurrent") != null ? Integer.parseInt(req.get("pageCurrent").toString()) : 1;
        int pageSize = req.get("pageSize") != null ? Integer.parseInt(req.get("pageSize").toString()) : 20;
        DictTutorTagExample example = new DictTutorTagExample();
        example.setOrderByClause("sort asc, id asc");
        return Result.success(dictTutorTagDao.page(pageCurrent, pageSize, example));
    }
    public Result<?> view(Long id) { return Result.success(dictTutorTagDao.getById(id)); }
    public Result<String> save(Map<String, Object> req) {
        String name = req.get("tagName") != null ? req.get("tagName").toString() : "";
        if (!StringUtils.hasText(name)) return Result.error("名称不能为空");
        DictTutorTag r = new DictTutorTag();
        r.setTagName(name);
        r.setSort(req.get("sort") != null ? Integer.parseInt(req.get("sort").toString()) : 0);
        dictTutorTagDao.save(r);
        return Result.success("添加成功");
    }
    public Result<String> edit(Map<String, Object> req) {
        Long id = Long.parseLong(req.get("id").toString());
        DictTutorTag r = new DictTutorTag();
        r.setId(id);
        if (req.get("tagName") != null) r.setTagName(req.get("tagName").toString());
        if (req.get("sort") != null) r.setSort(Integer.parseInt(req.get("sort").toString()));
        dictTutorTagDao.updateById(r);
        return Result.success("修改成功");
    }
    public Result<String> delete(Long id) { dictTutorTagDao.deleteById(id); return Result.success("删除成功"); }
}
