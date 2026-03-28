package com.roncoo.education.user.service.auth.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * API-AUTH-反馈
 */
@Data
@Accessors(chain = true)
@ApiModel(description = "API-AUTH-反馈")
public class AuthFeedbackResp implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

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

    @ApiModelProperty(value = "处理状态:0待处理,1处理中,2已处理")
    private Integer handleStatus;

    @ApiModelProperty(value = "处理回复")
    private String handleReply;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime gmtModified;
}
