package com.roncoo.education.user.service.auth.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 个人中心 "我的订单" 卡片 — 双方脱敏一致格式.
 */
@Data
@Accessors(chain = true)
@ApiModel(description = "我的订单 (脱敏)")
public class AuthOrderItemResp implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "预约 id")
    private Long id;

    @ApiModelProperty(value = "状态码 (0待匹配 1已撮合 2已完成 3已取消)")
    private Integer resStatus;

    @ApiModelProperty(value = "状态文案")
    private String statusText;

    @ApiModelProperty(value = "年级")
    private String grade;

    @ApiModelProperty(value = "科目 (CSV)")
    private String subjects;

    @ApiModelProperty(value = "区+大致位置")
    private String location;

    @ApiModelProperty(value = "需求摘要")
    private String detail;

    @ApiModelProperty(value = "日期 yyyy-MM-dd")
    private String date;

    @ApiModelProperty(value = "取消原因 (仅 res_status=3 时有值)")
    private String cancelReason;
}
