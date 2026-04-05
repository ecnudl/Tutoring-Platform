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
}
