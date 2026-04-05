package com.roncoo.education.user.service.api.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * API-首页聚合数据
 * </p>
 *
 * @author fengyw
 */
@Data
@Accessors(chain = true)
@ApiModel(description = "API-首页聚合数据")
public class HomepageDataResp implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "轮播图列表")
    private List<BannerItem> banners;

    @ApiModelProperty(value = "热门科目列表")
    private List<SubjectTreeResp.SubjectItem> hotSubjects;

    @ApiModelProperty(value = "热门区县列表")
    private List<CityDetailResp.DistrictItem> hotDistricts;

    @ApiModelProperty(value = "热门高校列表")
    private List<UniversityItem> hotUniversities;

    @ApiModelProperty(value = "推荐教员列表")
    private List<TutorListResp> recommendedTutors;

    @ApiModelProperty(value = "最新需求列表")
    private List<RequirementListResp> latestRequirements;

    @ApiModelProperty(value = "配置信息")
    private Map<String, String> config;

    @Data
    @Accessors(chain = true)
    @ApiModel(description = "轮播图")
    public static class BannerItem implements Serializable {

        private static final long serialVersionUID = 1L;

        @ApiModelProperty(value = "主键")
        private Long id;

        @ApiModelProperty(value = "图片URL")
        private String imageUrl;

        @ApiModelProperty(value = "跳转链接")
        private String linkUrl;

        @ApiModelProperty(value = "标题")
        private String title;

        @ApiModelProperty(value = "排序")
        private Integer sort;
    }

    @Data
    @Accessors(chain = true)
    @ApiModel(description = "高校信息")
    public static class UniversityItem implements Serializable {

        private static final long serialVersionUID = 1L;

        @ApiModelProperty(value = "主键")
        private Long id;

        @ApiModelProperty(value = "高校名称")
        private String uniName;

        @ApiModelProperty(value = "简称")
        private String uniShort;
    }
}
