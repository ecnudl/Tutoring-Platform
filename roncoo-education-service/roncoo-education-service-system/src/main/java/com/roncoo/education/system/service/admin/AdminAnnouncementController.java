package com.roncoo.education.system.service.admin;

import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.common.log.SysLog;
import com.roncoo.education.system.service.admin.biz.AdminAnnouncementBiz;
import com.roncoo.education.system.service.admin.req.AdminAnnouncementEditReq;
import com.roncoo.education.system.service.admin.req.AdminAnnouncementPageReq;
import com.roncoo.education.system.service.admin.req.AdminAnnouncementSaveReq;
import com.roncoo.education.system.service.admin.resp.AdminAnnouncementResp;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/system/admin/announcement")
public class AdminAnnouncementController {

    @NotNull
    private final AdminAnnouncementBiz biz;

    @PostMapping(value = "/page")
    public Result<Page<AdminAnnouncementResp>> page(@RequestBody AdminAnnouncementPageReq req) {
        return biz.page(req);
    }

    @SysLog(value = "公告添加")
    @PostMapping(value = "/save")
    public Result<String> save(@RequestBody AdminAnnouncementSaveReq req) {
        return biz.save(req);
    }

    @GetMapping(value = "/view")
    public Result<AdminAnnouncementResp> view(@RequestParam Long id) {
        return biz.view(id);
    }

    @SysLog(value = "公告修改")
    @PutMapping(value = "/edit")
    public Result<String> edit(@RequestBody AdminAnnouncementEditReq req) {
        return biz.edit(req);
    }

    @SysLog(value = "公告删除")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam Long id) {
        return biz.delete(id);
    }
}
