package com.roncoo.education.user.service.api;

import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.user.service.api.biz.ApiDictBiz;
import com.roncoo.education.user.service.api.resp.DictGradeResp;
import com.roncoo.education.user.service.api.resp.DictSubjectResp;
import com.roncoo.education.user.service.api.resp.DictTutorTagResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * API-字典数据
 *
 * @author fengyw
 */
@Api(tags = "api-字典数据")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/api/dict")
public class ApiDictController {

    @NotNull
    private final ApiDictBiz biz;

    @ApiOperation(value = "科目列表", notes = "获取所有启用的科目")
    @GetMapping(value = "/subject/list")
    public Result<List<DictSubjectResp>> subjectList() {
        return biz.subjectList();
    }

    @ApiOperation(value = "年级列表", notes = "获取所有启用的年级，可按学段筛选")
    @ApiImplicitParam(name = "gradeLevel", value = "学段(1:小学,2:初中,3:高中)", dataTypeClass = Integer.class, paramType = "query")
    @GetMapping(value = "/grade/list")
    public Result<List<DictGradeResp>> gradeList(@RequestParam(required = false) Integer gradeLevel) {
        return biz.gradeList(gradeLevel);
    }

    @ApiOperation(value = "标签列表", notes = "获取所有启用的教员标签")
    @GetMapping(value = "/tag/list")
    public Result<List<DictTutorTagResp>> tagList() {
        return biz.tagList();
    }

}
