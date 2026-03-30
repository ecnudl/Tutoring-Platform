package com.roncoo.education.user.service.admin.biz;

import com.roncoo.education.common.base.BaseBiz;
import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.user.dao.TutorReservationDao;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorReservation;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorReservationExample;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class AdminReservationBiz extends BaseBiz {
    @NotNull
    private final TutorReservationDao tutorReservationDao;

    public Result<?> page(Map<String, Object> req) {
        int pageCurrent = req.get("pageCurrent") != null ? Integer.parseInt(req.get("pageCurrent").toString()) : 1;
        int pageSize = req.get("pageSize") != null ? Integer.parseInt(req.get("pageSize").toString()) : 20;
        Integer resStatus = req.get("resStatus") != null ? Integer.parseInt(req.get("resStatus").toString()) : null;

        TutorReservationExample example = new TutorReservationExample();
        TutorReservationExample.Criteria c = example.createCriteria();
        if (resStatus != null) {
            c.andResStatusEqualTo(resStatus);
        }
        example.setOrderByClause("gmt_create desc");
        Page<TutorReservation> page = tutorReservationDao.page(pageCurrent, pageSize, example);
        return Result.success(page);
    }

    public Result<?> view(Long id) {
        TutorReservation res = tutorReservationDao.getById(id);
        if (res == null) {
            return Result.error("预约不存在");
        }
        return Result.success(res);
    }
}
