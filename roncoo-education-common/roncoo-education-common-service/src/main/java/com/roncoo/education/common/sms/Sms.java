package com.roncoo.education.common.sms;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author wujing
 */
@Data
@Accessors(chain = true)
public class Sms implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer smsPlatform;

    // Lkyun短信签名
    private String lkyunSmsSignName;
    private String lkyunSmsAccessKeyId;
    private String lkyunSmsAccessKeySecret;
    private String lkyunSmsAuthCode;
    private String lkyunSmsPurchaseCode;

    // Aliyun短信签名
    private String aliyunSmsSignName;
    private String aliyunSmsAccessKeyId;
    private String aliyunSmsAccessKeySecret;
    private String aliyunSmsAuthCode;
    private String aliyunSmsPurchaseCode;

    // Tencent短信签名
    private String tencentSmsSignName;
    private String tencentSmsSdkAppId;
    private String tencentSmsAccessKeyId;
    private String tencentSmsAccessKeySecret;
    private String tencentSmsAuthCode;
    private String tencentSmsPurchaseCode;
    private String tencentSmsRegion;

    // 厂商中性短信配置：各 Impl 优先读取这些通用字段，vendor-prefixed 字段作 fallback
    // 迁移到新短信服务商时，只需在 sys_config 里用通用 key 填写凭证，无需加字段/代码
    private String smsSignName;
    private String smsSecretId;    // access key id
    private String smsSecretKey;   // access key secret
    private String smsSdkAppId;    // 应用ID（部分厂商需要，如腾讯）
    private String smsAuthCode;    // 验证码模板ID
    private String smsPurchaseCode; // 订单通知模板ID
    private String smsRegion;      // 区域
}

