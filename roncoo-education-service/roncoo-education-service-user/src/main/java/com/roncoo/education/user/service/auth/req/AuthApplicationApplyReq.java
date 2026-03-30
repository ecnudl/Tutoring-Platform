package com.roncoo.education.user.service.auth.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * API-AUTH-申请
 */
@Data
@Accessors(chain = true)
@ApiModel(description = "API-AUTH-申请")
public class AuthApplicationApplyReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "需求ID")
    private Long requirementId;

    @ApiModelProperty(value = "报价时薪(元)")
    private BigDecimal quotedRate;

    @ApiModelProperty(value = "申请留言")
    private String message;
}
