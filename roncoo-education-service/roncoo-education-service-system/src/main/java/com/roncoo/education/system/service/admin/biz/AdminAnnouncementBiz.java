package com.roncoo.education.system.service.admin.biz;

import com.roncoo.education.common.base.BaseBiz;
import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.base.page.PageUtil;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.common.tools.BeanUtil;
import com.roncoo.education.system.dao.SysAnnouncementDao;
import com.roncoo.education.system.dao.impl.mapper.entity.SysAnnouncement;
import com.roncoo.education.system.service.admin.req.AdminAnnouncementEditReq;
import com.roncoo.education.system.service.admin.req.AdminAnnouncementPageReq;
import com.roncoo.education.system.service.admin.req.AdminAnnouncementSaveReq;
import com.roncoo.education.system.service.admin.resp.AdminAnnouncementResp;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminAnnouncementBiz extends BaseBiz {

    @NotNull
    private final SysAnnouncementDao dao;

    public Result<Page<AdminAnnouncementResp>> page(AdminAnnouncementPageReq req) {
        Page<SysAnnouncement> page = dao.page(req.getPageCurrent(), req.getPageSize(),
                req.getCategory(), req.getStatusId(), req.getKeyword());
        return Result.success(PageUtil.transform(page, AdminAnnouncementResp.class));
    }

    public Result<String> save(AdminAnnouncementSaveReq req) {
        SysAnnouncement record = BeanUtil.copyProperties(req, SysAnnouncement.class);
        return dao.save(record) > 0 ? Result.success("操作成功") : Result.error("操作失败");
    }

    public Result<AdminAnnouncementResp> view(Long id) {
        return Result.success(BeanUtil.copyProperties(dao.getById(id), AdminAnnouncementResp.class));
    }

    public Result<String> edit(AdminAnnouncementEditReq req) {
        SysAnnouncement record = BeanUtil.copyProperties(req, SysAnnouncement.class);
        return dao.updateById(record) > 0 ? Result.success("操作成功") : Result.error("操作失败");
    }

    public Result<String> delete(Long id) {
        return dao.deleteById(id) > 0 ? Result.success("操作成功") : Result.error("操作失败");
    }
}
