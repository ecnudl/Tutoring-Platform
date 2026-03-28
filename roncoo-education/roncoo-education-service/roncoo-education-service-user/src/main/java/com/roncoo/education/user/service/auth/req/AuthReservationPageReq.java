package com.roncoo.education.user.service.auth.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * API-AUTH-预约分页
 */
@Data
@Accessors(chain = true)
@ApiModel(description = "API-AUTH-预约分页")
public class AuthReservationPageReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "当前页")
    private int pageCurrent = 1;

    @ApiModelProperty(value = "每页条数")
    private int pageSize = 20;

    @ApiModelProperty(value = "预约状态:0待确认,1已确认,2已完成,3已取消")
    private Integer reservationStatus;

    @ApiModelProperty(value = "角色类型:1作为学员,2作为导师")
    private Integer roleType;
}
