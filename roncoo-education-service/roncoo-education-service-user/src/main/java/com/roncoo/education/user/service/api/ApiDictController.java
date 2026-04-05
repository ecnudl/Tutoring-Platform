package com.roncoo.education.user.service.api;

import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.user.service.api.biz.ApiDictBiz;
import com.roncoo.education.user.service.api.resp.CityDetailResp;
import com.roncoo.education.user.service.api.resp.DictGradeResp;
import com.roncoo.education.user.service.api.resp.DictSubjectResp;
import com.roncoo.education.user.service.api.resp.DictTutorTagResp;
import com.roncoo.education.user.service.api.resp.SubjectTreeResp;
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

    @ApiOperation(value = "城市列表", notes = "获取所有启用的城市")
    @GetMapping(value = "/city/list")
    public Result<?> cityList() {
        return biz.cityList();
    }

    @ApiOperation(value = "城市详情", notes = "根据拼音获取城市详情(含区县和高校)")
    @ApiImplicitParam(name = "pinyin", value = "城市拼音", dataTypeClass = String.class, paramType = "path", required = true)
    @GetMapping(value = "/city/{pinyin}")
    public Result<CityDetailResp> cityDetail(@PathVariable String pinyin) {
        return biz.cityDetail(pinyin);
    }

    @ApiOperation(value = "区县列表", notes = "根据城市ID获取区县列表")
    @ApiImplicitParam(name = "cityId", value = "城市ID", dataTypeClass = Long.class, paramType = "query", required = true)
    @GetMapping(value = "/district/list")
    public Result<?> districtList(@RequestParam Long cityId) {
        return biz.districtList(cityId);
    }

    @ApiOperation(value = "街道列表", notes = "根据区县ID获取街道列表")
    @ApiImplicitParam(name = "districtId", value = "区县ID", dataTypeClass = Long.class, paramType = "query", required = true)
    @GetMapping(value = "/sub-district/list")
    public Result<?> subDistrictList(@RequestParam Long districtId) {
        return biz.subDistrictList(districtId);
    }

    @ApiOperation(value = "高校列表", notes = "根据城市ID获取高校列表")
    @ApiImplicitParam(name = "cityId", value = "城市ID", dataTypeClass = Long.class, paramType = "query", required = true)
    @GetMapping(value = "/university/list")
    public Result<?> universityList(@RequestParam Long cityId) {
        return biz.universityList(cityId);
    }

    @ApiOperation(value = "科目分类树", notes = "获取完整的科目分类树")
    @GetMapping(value = "/subject/tree")
    public Result<List<SubjectTreeResp>> subjectTree() {
        return biz.subjectTree();
    }

    @ApiOperation(value = "热门科目", notes = "获取热门科目列表")
    @GetMapping(value = "/subject/hot")
    public Result<?> subjectHot() {
        return biz.subjectHot();
    }

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
