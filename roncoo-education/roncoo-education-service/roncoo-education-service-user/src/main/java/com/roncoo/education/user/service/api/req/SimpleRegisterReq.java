package com.roncoo.education.user.service.api.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 简化注册请求（开发环境，不依赖RSA加密和短信验证码）
 */
@Data
@Accessors(chain = true)
public class SimpleRegisterReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "手机号", required = true)
    private String mobile;

    @ApiModelProperty(value = "密码（明文，开发环境用）", required = true)
    private String password;

    @ApiModelProperty(value = "用户类型 1教员 2学员", required = true)
    private Integer userType;
}
