package com.roncoo.education.user.service.api.biz;

import com.roncoo.education.common.base.BaseBiz;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.common.tools.BeanUtil;
import com.roncoo.education.user.dao.DictCityDao;
import com.roncoo.education.user.dao.DictDistrictDao;
import com.roncoo.education.user.dao.DictSubjectDao;
import com.roncoo.education.user.dao.DictUniversityDao;
import com.roncoo.education.user.dao.HomepageConfigDao;
import com.roncoo.education.user.dao.PriceReferenceDao;
import com.roncoo.education.user.dao.TutorProfileDao;
import com.roncoo.education.user.dao.TutorRequirementDao;
import com.roncoo.education.user.dao.WebsiteBannerDao;
import com.roncoo.education.user.dao.impl.mapper.entity.DictDistrict;
import com.roncoo.education.user.dao.impl.mapper.entity.DictSubject;
import com.roncoo.education.user.dao.impl.mapper.entity.DictUniversity;
import com.roncoo.education.user.dao.impl.mapper.entity.HomepageConfig;
import com.roncoo.education.user.dao.impl.mapper.entity.PriceReference;
import com.roncoo.education.user.dao.impl.mapper.entity.WebsiteBanner;
import com.roncoo.education.user.service.api.resp.CityDetailResp;
import com.roncoo.education.user.service.api.resp.HomepageDataResp;
import com.roncoo.education.user.service.api.resp.PriceReferenceResp;
import com.roncoo.education.user.service.api.resp.RequirementListResp;
import com.roncoo.education.user.service.api.resp.SubjectTreeResp;
import com.roncoo.education.user.service.api.resp.TutorListResp;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API-首页聚合
 *
 * @author fengyw
 */
@Component
@RequiredArgsConstructor
@CacheConfig(cacheNames = {"user"})
public class ApiHomepageBiz extends BaseBiz {

    @NotNull
    private final WebsiteBannerDao websiteBannerDao;

    @NotNull
    private final HomepageConfigDao homepageConfigDao;

    @NotNull
    private final PriceReferenceDao priceReferenceDao;

    @NotNull
    private final DictCityDao dictCityDao;

    @NotNull
    private final DictDistrictDao dictDistrictDao;

    @NotNull
    private final DictUniversityDao dictUniversityDao;

    @NotNull
    private final DictSubjectDao dictSubjectDao;

    @NotNull
    private final TutorProfileDao tutorProfileDao;

    @NotNull
    private final ApiTutorBiz apiTutorBiz;

    @NotNull
    private final ApiRequirementBiz apiRequirementBiz;

    /**
     * 首页聚合数据
     */
    @Cacheable
    public Result<HomepageDataResp> data(Long cityId) {
        HomepageDataResp resp = new HomepageDataResp();

        // 查询轮播图
        List<WebsiteBanner> banners = websiteBannerDao.listByCityIdAndPosition(cityId, "home");
        resp.setBanners(BeanUtil.copyProperties(banners, HomepageDataResp.BannerItem.class));

        // 查询热门科目
        List<DictSubject> hotSubjects = dictSubjectDao.listHot();
        resp.setHotSubjects(BeanUtil.copyProperties(hotSubjects, SubjectTreeResp.SubjectItem.class));

        // 查询热门区县
        if (cityId != null) {
            List<DictDistrict> districts = dictDistrictDao.listByCityId(cityId);
            resp.setHotDistricts(BeanUtil.copyProperties(districts, CityDetailResp.DistrictItem.class));

            // 查询热门高校
            List<DictUniversity> hotUnis = dictUniversityDao.listHotByCityId(cityId);
            resp.setHotUniversities(BeanUtil.copyProperties(hotUnis, HomepageDataResp.UniversityItem.class));
        } else {
            resp.setHotDistricts(Collections.emptyList());
            resp.setHotUniversities(Collections.emptyList());
        }

        // 查询推荐教员
        Result<List<TutorListResp>> tutorResult = apiTutorBiz.recommend(cityId, 18);
        resp.setRecommendedTutors(tutorResult.getData() != null ? tutorResult.getData() : Collections.emptyList());

        // 查询最新需求
        Result<List<RequirementListResp>> reqResult = apiRequirementBiz.latest(cityId, 10);
        resp.setLatestRequirements(reqResult.getData() != null ? reqResult.getData() : Collections.emptyList());

        // 查询配置
        Map<String, String> config = new HashMap<>();
        if (cityId != null) {
            List<HomepageConfig> configs = homepageConfigDao.listByCityId(cityId);
            for (HomepageConfig hc : configs) {
                config.put(hc.getConfigKey(), hc.getConfigValue());
            }
        }
        resp.setConfig(config);

        return Result.success(resp);
    }

    /**
     * 获取城市的价格参考表
     */
    @Cacheable
    public Result<List<PriceReferenceResp>> priceReference(Long cityId) {
        if (cityId == null) {
            return Result.success(Collections.emptyList());
        }
        List<PriceReference> list = priceReferenceDao.listByCityId(cityId);
        return Result.success(BeanUtil.copyProperties(list, PriceReferenceResp.class));
    }
}
