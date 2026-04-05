package com.roncoo.education.user.service.auth.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * API-AUTH-收藏
 */
@Data
@Accessors(chain = true)
@ApiModel(description = "API-AUTH-收藏")
public class AuthFavoriteResp implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "目标类型:1教员,2需求")
    private Integer targetType;

    @ApiModelProperty(value = "目标ID")
    private Long targetId;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty(value = "教员信息")
    private TutorInfo tutor;

    @Data
    public static class TutorInfo implements Serializable {
        private Long id;
        private String realName;
        private String avatar;
        private String university;
        private String major;
        private BigDecimal priceMin;
        private BigDecimal priceMax;
        private Integer tutorType;
        private String displayNo;
    }
}
