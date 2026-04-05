package com.roncoo.education.user.service.api.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * API-教员列表
 * </p>
 *
 * @author fengyw
 */
@Data
@Accessors(chain = true)
@ApiModel(description = "API-教员列表")
public class TutorListResp implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "展示编号")
    private String displayNo;

    @ApiModelProperty(value = "姓氏")
    private String realName;

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

    @ApiModelProperty(value = "城市名称")
    private String cityName;

    @ApiModelProperty(value = "区县名称")
    private String districtName;

    @ApiModelProperty(value = "授课方式(1:教员上门,2:学员上门,3:线上授课)")
    private Integer teachingMethod;

    @ApiModelProperty(value = "最低课时费(元/小时)")
    private BigDecimal priceMin;

    @ApiModelProperty(value = "最高课时费(元/小时)")
    private BigDecimal priceMax;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "是否明星教员(0:否,1:是)")
    private Integer isStar;

    @ApiModelProperty(value = "是否实名认证(0:否,1:是)")
    private Integer isVerified;

    @ApiModelProperty(value = "可教科目名称列表")
    private List<String> subjectNames;

    @ApiModelProperty(value = "标签列表")
    private List<String> tags;

    @ApiModelProperty(value = "最近登录时间")
    private LocalDateTime lastLoginTime;
}
