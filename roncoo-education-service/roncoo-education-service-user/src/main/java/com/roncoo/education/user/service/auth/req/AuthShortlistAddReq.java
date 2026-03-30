package com.roncoo.education.user.service.auth.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * API-AUTH-候选名单添加
 */
@Data
@Accessors(chain = true)
@ApiModel(description = "API-AUTH-候选名单添加")
public class AuthShortlistAddReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "导师用户ID")
    private Long tutorUserId;

    @ApiModelProperty(value = "备注")
    private String remark;
}
