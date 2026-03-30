package com.roncoo.education.user.service.auth.biz;

import com.roncoo.education.common.base.BaseBiz;
import com.roncoo.education.common.base.ThreadContext;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.user.dao.FootprintDao;
import com.roncoo.education.user.dao.impl.mapper.entity.Footprint;
import com.roncoo.education.user.dao.impl.mapper.entity.FootprintExample;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AuthFootprintBiz extends BaseBiz {
    @NotNull
    private final FootprintDao footprintDao;

    public Result<?> page(Map<String, Object> req) {
        Long userId = ThreadContext.userId();
        int pageCurrent = req.get("pageCurrent") != null ? Integer.parseInt(req.get("pageCurrent").toString()) : 1;
        int pageSize = req.get("pageSize") != null ? Integer.parseInt(req.get("pageSize").toString()) : 20;
        FootprintExample example = new FootprintExample();
        example.createCriteria().andUserIdEqualTo(userId);
        example.setOrderByClause("gmt_create desc");
        return Result.success(footprintDao.page(pageCurrent, pageSize, example));
    }

    public Result<String> clear() {
        Long userId = ThreadContext.userId();
        List<Footprint> list = footprintDao.listByUserId(userId);
        for (Footprint fp : list) {
            footprintDao.deleteById(fp.getId());
        }
        return Result.success("已清空");
    }
}
