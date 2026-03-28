package com.roncoo.education.user.service.api.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * API-教员搜索结果
 * </p>
 *
 * @author fengyw
 */
@Data
@Accessors(chain = true)
@ApiModel(description = "API-教员搜索结果")
public class TutorSearchResp implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "性别(1:男,2:女)")
    private Integer gender;

    @ApiModelProperty(value = "教员类型(1:大学生,2:专职教师,3:海外留学生,4:专业人士)")
    private Integer tutorType;

    @ApiModelProperty(value = "学历")
    private String degree;

    @ApiModelProperty(value = "毕业/在读院校")
    private String university;

    @ApiModelProperty(value = "专业")
    private String major;

    @ApiModelProperty(value = "可教科目，逗号分隔")
    private String subjects;

    @ApiModelProperty(value = "可教年级，逗号分隔")
    private String grades;

    @ApiModelProperty(value = "标签，逗号分隔")
    private String tags;

    @ApiModelProperty(value = "最低课时费(元/小时)")
    private BigDecimal priceMin;

    @ApiModelProperty(value = "最高课时费(元/小时)")
    private BigDecimal priceMax;

    @ApiModelProperty(value = "是否支持免费试课(0:否,1:是)")
    private Integer freeTrial;

    @ApiModelProperty(value = "浏览次数")
    private Integer viewCount;

    @ApiModelProperty(value = "成功次数")
    private Integer successCount;

    @ApiModelProperty(value = "自我介绍(简短)")
    private String selfIntroduction;
}
