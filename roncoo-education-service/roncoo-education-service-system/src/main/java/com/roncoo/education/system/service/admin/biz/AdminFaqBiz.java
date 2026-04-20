package com.roncoo.education.system.service.admin.biz;

import com.roncoo.education.common.base.BaseBiz;
import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.base.page.PageUtil;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.common.tools.BeanUtil;
import com.roncoo.education.system.dao.SysFaqDao;
import com.roncoo.education.system.dao.impl.mapper.entity.SysFaq;
import com.roncoo.education.system.service.admin.req.AdminFaqEditReq;
import com.roncoo.education.system.service.admin.req.AdminFaqPageReq;
import com.roncoo.education.system.service.admin.req.AdminFaqSaveReq;
import com.roncoo.education.system.service.admin.resp.AdminFaqResp;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminFaqBiz extends BaseBiz {

    @NotNull
    private final SysFaqDao dao;

    public Result<Page<AdminFaqResp>> page(AdminFaqPageReq req) {
        Page<SysFaq> page = dao.page(req.getPageCurrent(), req.getPageSize(),
                req.getCategory(), req.getStatusId(), req.getKeyword());
        return Result.success(PageUtil.transform(page, AdminFaqResp.class));
    }

    public Result<String> save(AdminFaqSaveReq req) {
        SysFaq record = BeanUtil.copyProperties(req, SysFaq.class);
        return dao.save(record) > 0 ? Result.success("操作成功") : Result.error("操作失败");
    }

    public Result<AdminFaqResp> view(Long id) {
        return Result.success(BeanUtil.copyProperties(dao.getById(id), AdminFaqResp.class));
    }

    public Result<String> edit(AdminFaqEditReq req) {
        SysFaq record = BeanUtil.copyProperties(req, SysFaq.class);
        return dao.updateById(record) > 0 ? Result.success("操作成功") : Result.error("操作失败");
    }

    public Result<String> delete(Long id) {
        return dao.deleteById(id) > 0 ? Result.success("操作成功") : Result.error("操作失败");
    }
}
