package com.roncoo.education.user.service.api.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * API-城市详情
 * </p>
 *
 * @author fengyw
 */
@Data
@Accessors(chain = true)
@ApiModel(description = "API-城市详情")
public class CityDetailResp implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "城市名称")
    private String cityName;

    @ApiModelProperty(value = "城市拼音")
    private String cityPinyin;

    @ApiModelProperty(value = "省份名称")
    private String provinceName;

    @ApiModelProperty(value = "区县列表")
    private List<DistrictItem> districts;

    @ApiModelProperty(value = "高校列表")
    private List<UniversityItem> universities;

    @Data
    @Accessors(chain = true)
    @ApiModel(description = "区县信息")
    public static class DistrictItem implements Serializable {

        private static final long serialVersionUID = 1L;

        @ApiModelProperty(value = "主键")
        private Long id;

        @ApiModelProperty(value = "区县名称")
        private String districtName;
    }

    @Data
    @Accessors(chain = true)
    @ApiModel(description = "高校信息")
    public static class UniversityItem implements Serializable {

        private static final long serialVersionUID = 1L;

        @ApiModelProperty(value = "主键")
        private Long id;

        @ApiModelProperty(value = "高校名称")
        private String universityName;
    }
}
