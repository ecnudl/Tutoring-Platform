package com.roncoo.education.user.service.auth.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * API-AUTH-预约创建
 */
@Data
@Accessors(chain = true)
@ApiModel(description = "API-AUTH-预约创建")
public class AuthReservationCreateReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "导师用户ID")
    private Long tutorUserId;

    @ApiModelProperty(value = "科目")
    private String subject;

    @ApiModelProperty(value = "授课方式:1线上,2线下")
    private Integer teachingMode;

    @ApiModelProperty(value = "预约日期(yyyy-MM-dd)")
    private String reservationDate;

    @ApiModelProperty(value = "开始时间(HH:mm)")
    private String startTime;

    @ApiModelProperty(value = "结束时间(HH:mm)")
    private String endTime;

    @ApiModelProperty(value = "约定时薪(元)")
    private BigDecimal agreedRate;

    @ApiModelProperty(value = "上课地址(线下时填)")
    private String address;

    @ApiModelProperty(value = "备注")
    private String remark;
}
