package com.roncoo.education.user.service.auth.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * API-AUTH-收藏添加
 */
@Data
@Accessors(chain = true)
@ApiModel(description = "API-AUTH-收藏添加")
public class AuthFavoriteAddReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "目标类型:1导师,2需求,3课程")
    private Integer targetType;

    @ApiModelProperty(value = "目标ID")
    private Long targetId;
}
