package com.roncoo.education.user.service.api.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
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
 * API-需求详情(完整)
 * </p>
 *
 * @author fengyw
 */
@Data
@Accessors(chain = true)
@ApiModel(description = "API-需求详情(完整)")
public class RequirementDetailResp implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "展示编号")
    private String displayNo;

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

    @ApiModelProperty(value = "科目名称列表")
    private List<String> subjectNames;

    @ApiModelProperty(value = "年级ID")
    private Long gradeId;

    @ApiModelProperty(value = "年级名称")
    private String gradeName;

    @ApiModelProperty(value = "省份ID")
    private Long provinceId;

    @ApiModelProperty(value = "城市ID")
    private Long cityId;

    @ApiModelProperty(value = "城市名称")
    private String cityName;

    @ApiModelProperty(value = "区县ID")
    private Long districtId;

    @ApiModelProperty(value = "区县名称")
    private String districtName;

    @ApiModelProperty(value = "详细地址")
    private String address;

    @ApiModelProperty(value = "预算下限(元/小时)")
    private BigDecimal budgetMin;

    @ApiModelProperty(value = "预算上限(元/小时)")
    private BigDecimal budgetMax;

    @ApiModelProperty(value = "学员性别")
    private Integer studentGender;

    @ApiModelProperty(value = "教员性别要求")
    private Integer tutorGender;

    @ApiModelProperty(value = "教员类型偏好(1:大学生,2:专职教师,3:海外留学生,4:专业人士)")
    private Integer tutorTypePref;

    @ApiModelProperty(value = "授课方式(1:教员上门,2:学员上门,3:线上授课)")
    private Integer teachingMethod;

    @ApiModelProperty(value = "时间安排")
    private String schedule;

    @ApiModelProperty(value = "需求详情")
    private String requirementDetail;

    @ApiModelProperty(value = "联系人")
    private String contactName;

    @ApiModelProperty(value = "联系电话(脱敏)")
    private String contactMobile;

    @ApiModelProperty(value = "需求状态")
    private Integer reqStatus;

    @ApiModelProperty(value = "浏览次数")
    private Integer viewCount;

    @ApiModelProperty(value = "申请人数")
    private Integer applicationCount;
}
