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
 * API-需求列表
 * </p>
 *
 * @author fengyw
 */
@Data
@Accessors(chain = true)
@ApiModel(description = "API-需求列表")
public class RequirementListResp implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "展示编号")
    private String displayNo;

    @ApiModelProperty(value = "需求标题")
    private String title;

    @ApiModelProperty(value = "科目名称列表")
    private List<String> subjectNames;

    @ApiModelProperty(value = "年级名称")
    private String gradeName;

    @ApiModelProperty(value = "区县名称")
    private String districtName;

    @ApiModelProperty(value = "学员性别")
    private Integer studentGender;

    @ApiModelProperty(value = "教员类型偏好(1:大学生,2:专职教师,3:海外留学生,4:专业人士)")
    private Integer tutorTypePref;

    @ApiModelProperty(value = "授课方式(1:教员上门,2:学员上门,3:线上授课)")
    private Integer teachingMethod;

    @ApiModelProperty(value = "预算下限(元/小时)")
    private BigDecimal budgetMin;

    @ApiModelProperty(value = "预算上限(元/小时)")
    private BigDecimal budgetMax;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty(value = "需求状态")
    private Integer reqStatus;

    @ApiModelProperty(value = "城市 ID (源字段, 用于子域名匹配)")
    private Long cityId;

    @ApiModelProperty(value = "区域 CSV (源字段, 多区域逗号分隔)")
    private String districtNames;

    @ApiModelProperty(value = "科目 CSV (源字段, 与 subjectNames 不同)")
    private String subjectIds;

    @ApiModelProperty(value = "大致位置")
    private String address;

    @ApiModelProperty(value = "是否加急 0 否 1 是")
    private Integer isUrgent;
}
