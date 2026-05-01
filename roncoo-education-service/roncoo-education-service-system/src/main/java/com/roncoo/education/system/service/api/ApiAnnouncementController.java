package com.roncoo.education.system.service.api;

import com.roncoo.education.common.base.BaseBiz;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.common.tools.BeanUtil;
import com.roncoo.education.system.dao.SysAnnouncementDao;
import com.roncoo.education.system.dao.impl.mapper.entity.SysAnnouncement;
import com.roncoo.education.system.service.admin.resp.AdminAnnouncementResp;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 公开公告接口，按 category 查询。
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/api/announcement")
public class ApiAnnouncementController extends BaseBiz {

    @NotNull
    private final SysAnnouncementDao dao;

    @GetMapping(value = "/list")
    public Result<List<AdminAnnouncementResp>> list(@RequestParam(required = false) String category,
                                                    @RequestParam(required = false, defaultValue = "20") Integer limit) {
        List<SysAnnouncement> list = dao.listByCategory(category, limit == null ? 20 : limit);
        return Result.success(BeanUtil.copyProperties(list, AdminAnnouncementResp.class));
    }

    @GetMapping(value = "/view")
    public Result<AdminAnnouncementResp> view(@RequestParam Long id) {
        SysAnnouncement a = dao.getById(id);
        if (a == null || a.getStatusId() == null || a.getStatusId() != 1) {
            return Result.error("公告不存在");
        }
        return Result.success(BeanUtil.copyProperties(a, AdminAnnouncementResp.class));
    }
}
