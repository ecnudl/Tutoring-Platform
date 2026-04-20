package com.roncoo.education.system.service.admin;

import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.common.log.SysLog;
import com.roncoo.education.system.service.admin.biz.AdminFaqBiz;
import com.roncoo.education.system.service.admin.req.AdminFaqEditReq;
import com.roncoo.education.system.service.admin.req.AdminFaqPageReq;
import com.roncoo.education.system.service.admin.req.AdminFaqSaveReq;
import com.roncoo.education.system.service.admin.resp.AdminFaqResp;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/system/admin/faq")
public class AdminFaqController {

    @NotNull
    private final AdminFaqBiz biz;

    @PostMapping(value = "/page")
    public Result<Page<AdminFaqResp>> page(@RequestBody AdminFaqPageReq req) {
        return biz.page(req);
    }

    @SysLog(value = "FAQ 添加")
    @PostMapping(value = "/save")
    public Result<String> save(@RequestBody AdminFaqSaveReq req) {
        return biz.save(req);
    }

    @GetMapping(value = "/view")
    public Result<AdminFaqResp> view(@RequestParam Long id) {
        return biz.view(id);
    }

    @SysLog(value = "FAQ 修改")
    @PutMapping(value = "/edit")
    public Result<String> edit(@RequestBody AdminFaqEditReq req) {
        return biz.edit(req);
    }

    @SysLog(value = "FAQ 删除")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam Long id) {
        return biz.delete(id);
    }
}
