package com.roncoo.education.system.service.api;

import com.roncoo.education.common.base.BaseBiz;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.common.tools.BeanUtil;
import com.roncoo.education.system.dao.SysFaqDao;
import com.roncoo.education.system.dao.impl.mapper.entity.SysFaq;
import com.roncoo.education.system.service.admin.resp.AdminFaqResp;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/system/api/faq")
public class ApiFaqController extends BaseBiz {

    @NotNull
    private final SysFaqDao dao;

    @GetMapping(value = "/list")
    public Result<List<AdminFaqResp>> list() {
        List<SysFaq> list = dao.listAllActive();
        return Result.success(BeanUtil.copyProperties(list, AdminFaqResp.class));
    }
}
