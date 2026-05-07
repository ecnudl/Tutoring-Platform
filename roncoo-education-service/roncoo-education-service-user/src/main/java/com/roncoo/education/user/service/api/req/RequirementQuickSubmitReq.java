package com.roncoo.education.user.service.api.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * API-需求快速提交
 * </p>
 *
 * @author fengyw
 */
@Data
@Accessors(chain = true)
@ApiModel(description = "API-需求快速提交")
public class RequirementQuickSubmitReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "联系人姓名", required = true)
    private String contactName;

    @ApiModelProperty(value = "联系电话", required = true)
    private String contactMobile;

    @ApiModelProperty(value = "联系微信")
    private String contactWechat;

    @ApiModelProperty(value = "需求详情")
    private String requirementDetail;

    @ApiModelProperty(value = "城市ID")
    private Long cityId;

    @ApiModelProperty(value = "科目ID列表")
    private List<Long> subjectIds;

    @ApiModelProperty(value = "年级ID")
    private Long gradeId;

    @ApiModelProperty(value = "区县ID")
    private Long districtId;

    @ApiModelProperty(value = "授课方式(1:教员上门,2:学员上门,3:线上授课)")
    private Integer teachingMethod;

    @ApiModelProperty(value = "学生情况 (必填, 自由文本: 程度/学习情况/科目年级/上课频率)", required = true)
    private String studentInfo;

    @ApiModelProperty(value = "教员要求 (必填, 自由文本: 性别/地区等偏好)", required = true)
    private String tutorRequest;

    @ApiModelProperty(value = "交通信息 (必填, 自由文本: 线上无需填; 否则具体街道/地点)", required = true)
    private String trafficInfo;
}
