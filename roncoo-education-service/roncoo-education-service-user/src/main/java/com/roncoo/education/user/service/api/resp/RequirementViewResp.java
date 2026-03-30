package com.roncoo.education.user.service.api.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * API-需求详情
 * </p>
 *
 * @author fengyw
 */
@Data
@Accessors(chain = true)
@ApiModel(description = "API-需求详情")
public class RequirementViewResp implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime gmtCreate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
    private LocalDateTime gmtModified;

    @ApiModelProperty(value = "需求标题")
    private String title;

    @ApiModelProperty(value = "科目IDs(JSON)")
    private String subjectIds;

    @ApiModelProperty(value = "年级ID")
    private Long gradeId;

    @ApiModelProperty(value = "省份ID")
    private Long provinceId;

    @ApiModelProperty(value = "城市ID")
    private Long cityId;

    @ApiModelProperty(value = "区县ID")
    private Long districtId;

    @ApiModelProperty(value = "详细地址")
    private String address;

    @ApiModelProperty(value = "预算下限(元/小时)")
    private BigDecimal budgetMin;

    @ApiModelProperty(value = "预算上限(元/小时)")
    private BigDecimal budgetMax;

    @ApiModelProperty(value = "教员性别要求")
    private Integer tutorGender;

    @ApiModelProperty(value = "时间安排")
    private String schedule;

    @ApiModelProperty(value = "需求详情")
    private String requirementDetail;

    @ApiModelProperty(value = "联系人")
    private String contactName;

    @ApiModelProperty(value = "联系电话")
    private String contactMobile;

    @ApiModelProperty(value = "需求状态")
    private Integer reqStatus;

    @ApiModelProperty(value = "浏览次数")
    private Integer viewCount;

    @ApiModelProperty(value = "申请人数")
    private Integer applicationCount;
}
