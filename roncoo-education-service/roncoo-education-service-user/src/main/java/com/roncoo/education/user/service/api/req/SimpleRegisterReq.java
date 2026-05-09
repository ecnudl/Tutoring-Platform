package com.roncoo.education.user.service.api.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 简化注册请求（不依赖RSA加密，需短信验证码）
 */
@Data
@Accessors(chain = true)
public class SimpleRegisterReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "手机号", required = true)
    private String mobile;

    @ApiModelProperty(value = "密码（明文）", required = true)
    private String password;

    @ApiModelProperty(value = "短信验证码 (旧字段, 当前未启用 SMS, 保留兼容)")
    private String code;

    @ApiModelProperty(value = "图形验证码 token (来自 GET /system/api/common/code)", required = true)
    private String verToken;

    @ApiModelProperty(value = "图形验证码 (用户输入)", required = true)
    private String verCode;

    @ApiModelProperty(value = "蜜罐字段 (隐藏, 真人不应该填)")
    private String honeypot;

    @ApiModelProperty(value = "用户类型 1教员 2学员", required = true)
    private Integer userType;

    @ApiModelProperty(value = "真实姓名/尊称")
    private String realName;

    @ApiModelProperty(value = "安全问题(找回密码用)")
    private String securityQuestion;

    @ApiModelProperty(value = "安全答案(明文,存储时SHA1+salt哈希)")
    private String securityAnswer;
}
