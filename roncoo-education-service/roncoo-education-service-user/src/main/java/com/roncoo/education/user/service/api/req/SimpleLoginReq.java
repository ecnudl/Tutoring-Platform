package com.roncoo.education.user.service.api.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 简化登录请求（开发环境，不依赖RSA加密和图形验证码）
 */
@Data
@Accessors(chain = true)
public class SimpleLoginReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "手机号", required = true)
    private String mobile;

    @ApiModelProperty(value = "密码（明文，开发环境用）", required = true)
    private String password;

    @ApiModelProperty(value = "期望登录的用户类型：1=教员，2=家长；不传则不校验")
    private Integer userType;
}
