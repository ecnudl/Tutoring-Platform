package com.roncoo.education.user.service.auth.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * API-AUTH-预约
 */
@Data
@Accessors(chain = true)
@ApiModel(description = "API-AUTH-预约")
public class AuthReservationResp implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "学员用户ID")
    private Long studentUserId;

    @ApiModelProperty(value = "导师用户ID")
    private Long tutorUserId;

    @ApiModelProperty(value = "导师姓名")
    private String tutorName;

    @ApiModelProperty(value = "学员姓名")
    private String studentName;

    @ApiModelProperty(value = "科目")
    private String subject;

    @ApiModelProperty(value = "授课方式:1线上,2线下")
    private Integer teachingMode;

    @ApiModelProperty(value = "预约日期")
    private String reservationDate;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    @ApiModelProperty(value = "约定时薪(元)")
    private BigDecimal agreedRate;

    @ApiModelProperty(value = "上课地址")
    private String address;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "预约状态:0待确认,1已确认,2已完成,3已取消")
    private Integer reservationStatus;

    @ApiModelProperty(value = "取消原因")
    private String cancelReason;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime gmtModified;
}
