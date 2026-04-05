package com.roncoo.education.user.service.api.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * API-价格参考
 * </p>
 *
 * @author fengyw
 */
@Data
@Accessors(chain = true)
@ApiModel(description = "API-价格参考")
public class PriceReferenceResp implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "教员类型(1:大学生,2:专职教师,3:海外留学生,4:专业人士)")
    private Integer tutorType;

    @ApiModelProperty(value = "学段(1:小学,2:初中,3:高中)")
    private Integer gradeLevel;

    @ApiModelProperty(value = "参考最低价(元/小时)")
    private BigDecimal priceMin;

    @ApiModelProperty(value = "参考最高价(元/小时)")
    private BigDecimal priceMax;

    @ApiModelProperty(value = "备注")
    private String remark;
}
