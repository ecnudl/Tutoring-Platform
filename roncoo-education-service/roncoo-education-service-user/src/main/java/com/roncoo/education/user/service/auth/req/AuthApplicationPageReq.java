package com.roncoo.education.user.service.auth.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * API-AUTH-申请分页
 */
@Data
@Accessors(chain = true)
@ApiModel(description = "API-AUTH-申请分页")
public class AuthApplicationPageReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "当前页")
    private int pageCurrent = 1;

    @ApiModelProperty(value = "每页条数")
    private int pageSize = 20;

    @ApiModelProperty(value = "申请状态:0待处理,1已接受,2已拒绝,3已取消")
    private Integer applicationStatus;
}
