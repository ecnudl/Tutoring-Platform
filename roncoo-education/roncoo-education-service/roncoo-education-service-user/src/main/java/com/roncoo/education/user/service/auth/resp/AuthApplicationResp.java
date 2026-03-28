package com.roncoo.education.user.service.auth.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * API-AUTH-申请
 */
@Data
@Accessors(chain = true)
@ApiModel(description = "API-AUTH-申请")
public class AuthApplicationResp implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "需求ID")
    private Long requirementId;

    @ApiModelProperty(value = "需求标题")
    private String requirementTitle;

    @ApiModelProperty(value = "导师用户ID")
    private Long tutorUserId;

    @ApiModelProperty(value = "报价时薪(元)")
    private BigDecimal quotedRate;

    @ApiModelProperty(value = "申请留言")
    private String message;

    @ApiModelProperty(value = "申请状态:0待处理,1已接受,2已拒绝,3已取消")
    private Integer applicationStatus;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime gmtModified;
}
