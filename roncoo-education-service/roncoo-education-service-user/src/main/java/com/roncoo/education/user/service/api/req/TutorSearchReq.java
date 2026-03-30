package com.roncoo.education.user.service.api.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * API-教员搜索
 * </p>
 *
 * @author fengyw
 */
@Data
@Accessors(chain = true)
@ApiModel(description = "API-教员搜索")
public class TutorSearchReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "当前页")
    private int pageCurrent = 1;

    @ApiModelProperty(value = "每页条数")
    private int pageSize = 20;

    @ApiModelProperty(value = "科目ID")
    private Long subjectId;

    @ApiModelProperty(value = "年级ID")
    private Long gradeId;

    @ApiModelProperty(value = "省份ID")
    private Long provinceId;

    @ApiModelProperty(value = "城市ID")
    private Long cityId;

    @ApiModelProperty(value = "区县ID")
    private Long districtId;

    @ApiModelProperty(value = "教员类型(1:大学生,2:专职教师,3:海外留学生,4:专业人士)")
    private Integer tutorType;

    @ApiModelProperty(value = "关键词")
    private String keyword;

    @ApiModelProperty(value = "最低价格")
    private BigDecimal priceMin;

    @ApiModelProperty(value = "最高价格")
    private BigDecimal priceMax;

    @ApiModelProperty(value = "性别(1:男,2:女)")
    private Integer gender;

    @ApiModelProperty(value = "排序字段(priceMin,viewCount,successCount)")
    private String sortField;

    @ApiModelProperty(value = "排序方向(asc,desc)")
    private String sortOrder;
}
