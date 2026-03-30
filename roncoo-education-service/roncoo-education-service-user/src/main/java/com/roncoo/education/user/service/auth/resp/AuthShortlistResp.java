package com.roncoo.education.user.service.auth.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * API-AUTH-候选名单
 */
@Data
@Accessors(chain = true)
@ApiModel(description = "API-AUTH-候选名单")
public class AuthShortlistResp implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "导师用户ID")
    private Long tutorUserId;

    @ApiModelProperty(value = "导师姓名")
    private String tutorName;

    @ApiModelProperty(value = "导师头像")
    private String tutorHeadImgUrl;

    @ApiModelProperty(value = "导师擅长科目")
    private String tutorSubjects;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime gmtCreate;
}
