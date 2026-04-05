package com.roncoo.education.user.service.api.biz;

import com.roncoo.education.common.base.BaseBiz;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.common.core.enums.StatusIdEnum;
import com.roncoo.education.common.tools.BeanUtil;
import com.roncoo.education.user.dao.DictCityDao;
import com.roncoo.education.user.dao.DictDistrictDao;
import com.roncoo.education.user.dao.DictGradeDao;
import com.roncoo.education.user.dao.DictSubDistrictDao;
import com.roncoo.education.user.dao.DictSubjectCategoryDao;
import com.roncoo.education.user.dao.DictSubjectDao;
import com.roncoo.education.user.dao.DictTutorTagDao;
import com.roncoo.education.user.dao.DictUniversityDao;
import com.roncoo.education.user.dao.impl.mapper.entity.DictCity;
import com.roncoo.education.user.dao.impl.mapper.entity.DictDistrict;
import com.roncoo.education.user.dao.impl.mapper.entity.DictSubDistrict;
import com.roncoo.education.user.dao.impl.mapper.entity.DictSubjectCategory;
import com.roncoo.education.user.dao.impl.mapper.entity.DictUniversity;
import com.roncoo.education.user.service.api.resp.CityDetailResp;
import com.roncoo.education.user.service.api.resp.DictGradeResp;
import com.roncoo.education.user.service.api.resp.DictSubjectResp;
import com.roncoo.education.user.service.api.resp.DictTutorTagResp;
import com.roncoo.education.user.service.api.resp.SubjectTreeResp;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * API-字典数据
 *
 * @author fengyw
 */
@Component
@RequiredArgsConstructor
@CacheConfig(cacheNames = {"user"})
public class ApiDictBiz extends BaseBiz {

    @NotNull
    private final DictCityDao dictCityDao;

    @NotNull
    private final DictDistrictDao dictDistrictDao;

    @NotNull
    private final DictSubDistrictDao dictSubDistrictDao;

    @NotNull
    private final DictUniversityDao dictUniversityDao;

    @NotNull
    private final DictSubjectCategoryDao dictSubjectCategoryDao;

    @NotNull
    private final DictSubjectDao dictSubjectDao;

    @NotNull
    private final DictGradeDao dictGradeDao;

    @NotNull
    private final DictTutorTagDao dictTutorTagDao;

    /**
     * 获取所有启用的城市
     */
    @Cacheable
    public Result<?> cityList() {
        // TODO 实际实现: 查询所有启用城市，按拼音排序
        List<DictCity> list = dictCityDao.listByStatusId(StatusIdEnum.YES.getCode());
        return Result.success(list);
    }

    /**
     * 根据拼音获取城市详情(含区县和高校)
     */
    @Cacheable
    public Result<CityDetailResp> cityDetail(String pinyin) {
        DictCity city = dictCityDao.getByPinyin(pinyin);
        if (city == null) {
            return Result.error("城市不存在");
        }
        CityDetailResp resp = new CityDetailResp();
        resp.setId(city.getId());
        resp.setCityName(city.getCityName());
        resp.setCityPinyin(city.getCityPinyin());
        resp.setProvinceName(city.getProvinceName());

        // 查询区县列表
        List<DictDistrict> districts = dictDistrictDao.listByCityId(city.getId());
        resp.setDistricts(BeanUtil.copyProperties(districts, CityDetailResp.DistrictItem.class));

        // 查询高校列表
        List<DictUniversity> universities = dictUniversityDao.listByCityId(city.getId());
        resp.setUniversities(BeanUtil.copyProperties(universities, CityDetailResp.UniversityItem.class));

        return Result.success(resp);
    }

    /**
     * 根据城市ID获取区县列表
     */
    @Cacheable
    public Result<?> districtList(Long cityId) {
        List<DictDistrict> list = dictDistrictDao.listByCityId(cityId);
        return Result.success(list);
    }

    /**
     * 根据区县ID获取街道列表
     */
    @Cacheable
    public Result<?> subDistrictList(Long districtId) {
        List<DictSubDistrict> list = dictSubDistrictDao.listByDistrictId(districtId);
        return Result.success(list);
    }

    /**
     * 根据城市ID获取高校列表
     */
    @Cacheable
    public Result<?> universityList(Long cityId) {
        List<DictUniversity> list = dictUniversityDao.listByCityId(cityId);
        return Result.success(list);
    }

    /**
     * 获取完整的科目分类树
     */
    @Cacheable
    public Result<List<SubjectTreeResp>> subjectTree() {
        List<DictSubjectCategory> categories = dictSubjectCategoryDao.listAll();
        List<SubjectTreeResp> tree = new ArrayList<>();
        for (DictSubjectCategory cat : categories) {
            if (cat.getParentId() == null || cat.getParentId() == 0) {
                SubjectTreeResp node = new SubjectTreeResp();
                node.setId(cat.getId());
                node.setCategoryName(cat.getCategoryName());
                // 查询该分类下的科目
                List<com.roncoo.education.user.dao.impl.mapper.entity.DictSubject> subjects = dictSubjectDao.listByCategoryId(cat.getId());
                List<SubjectTreeResp.SubjectItem> items = new ArrayList<>();
                for (com.roncoo.education.user.dao.impl.mapper.entity.DictSubject s : subjects) {
                    SubjectTreeResp.SubjectItem item = new SubjectTreeResp.SubjectItem();
                    item.setId(s.getId());
                    item.setSubjectName(s.getSubjectName());
                    item.setIsHot(s.getIsHot());
                    items.add(item);
                }
                node.setSubjects(items);
                tree.add(node);
            }
        }
        return Result.success(tree);
    }

    /**
     * 获取热门科目
     */
    @Cacheable
    public Result<?> subjectHot() {
        List<com.roncoo.education.user.dao.impl.mapper.entity.DictSubject> list = dictSubjectDao.listHot();
        return Result.success(BeanUtil.copyProperties(list, DictSubjectResp.class));
    }

    /**
     * 获取所有启用的科目
     */
    @Cacheable
    public Result<List<DictSubjectResp>> subjectList() {
        return Result.success(BeanUtil.copyProperties(dictSubjectDao.listAll(), DictSubjectResp.class));
    }

    /**
     * 获取所有启用的年级，可按学段筛选
     */
    @Cacheable
    public Result<List<DictGradeResp>> gradeList(Integer gradeLevel) {
        if (gradeLevel != null) {
            return Result.success(BeanUtil.copyProperties(dictGradeDao.listByGradeLevel(gradeLevel), DictGradeResp.class));
        }
        return Result.success(BeanUtil.copyProperties(dictGradeDao.listAll(), DictGradeResp.class));
    }

    /**
     * 获取所有启用的教员标签
     */
    @Cacheable
    public Result<List<DictTutorTagResp>> tagList() {
        return Result.success(BeanUtil.copyProperties(dictTutorTagDao.listAll(), DictTutorTagResp.class));
    }
}
