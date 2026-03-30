package com.roncoo.education.user.service.auth.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * API-AUTH-浏览足迹
 */
@Data
@Accessors(chain = true)
@ApiModel(description = "API-AUTH-浏览足迹")
public class AuthFootprintResp implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "目标类型:1导师,2需求,3课程")
    private Integer targetType;

    @ApiModelProperty(value = "目标ID")
    private Long targetId;

    @ApiModelProperty(value = "目标名称")
    private String targetName;

    @ApiModelProperty(value = "目标图片")
    private String targetImgUrl;

    @ApiModelProperty(value = "浏览时间")
    private LocalDateTime gmtCreate;
}
