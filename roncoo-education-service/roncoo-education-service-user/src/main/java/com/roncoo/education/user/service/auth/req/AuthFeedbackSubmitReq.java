package com.roncoo.education.user.service.auth.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * API-AUTH-反馈提交
 */
@Data
@Accessors(chain = true)
@ApiModel(description = "API-AUTH-反馈提交")
public class AuthFeedbackSubmitReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "反馈类型:1功能建议,2问题反馈,3投诉,4其他")
    private Integer feedbackType;

    @ApiModelProperty(value = "反馈标题")
    private String title;

    @ApiModelProperty(value = "反馈内容")
    private String content;

    @ApiModelProperty(value = "图片URL,多个逗号分隔")
    private String images;

    @ApiModelProperty(value = "联系方式")
    private String contact;
}
