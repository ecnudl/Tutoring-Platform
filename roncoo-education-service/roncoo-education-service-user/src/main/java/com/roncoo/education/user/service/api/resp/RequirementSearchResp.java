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
 * API-需求搜索结果
 * </p>
 *
 * @author fengyw
 */
@Data
@Accessors(chain = true)
@ApiModel(description = "API-需求搜索结果")
public class RequirementSearchResp implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime gmtCreate;

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

    @ApiModelProperty(value = "预算下限(元/小时)")
    private BigDecimal budgetMin;

    @ApiModelProperty(value = "预算上限(元/小时)")
    private BigDecimal budgetMax;

    @ApiModelProperty(value = "教员性别要求")
    private Integer tutorGender;

    @ApiModelProperty(value = "需求状态")
    private Integer reqStatus;

    @ApiModelProperty(value = "浏览次数")
    private Integer viewCount;

    @ApiModelProperty(value = "申请人数")
    private Integer applicationCount;
}
