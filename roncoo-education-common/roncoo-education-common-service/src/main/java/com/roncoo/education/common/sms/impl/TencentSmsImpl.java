package com.roncoo.education.common.sms.impl;

import cn.hutool.core.util.StrUtil;
import com.roncoo.education.common.sms.Sms;
import com.roncoo.education.common.sms.SmsFace;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import com.tencentcloudapi.sms.v20210111.models.SendStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 腾讯云短信
 *
 * @author dulin
 */
@Slf4j
@Component(value = "tencentSms")
public class TencentSmsImpl implements SmsFace {

    private static final String DEFAULT_REGION = "ap-guangzhou";
    private static final String ENDPOINT = "sms.tencentcloudapi.com";

    @Override
    public Boolean sendVerCode(String mobile, String code, Sms sms) {
        return tencent(mobile, new String[]{code, "5"}, sms.getTencentSmsSignName(), sms.getTencentSmsAuthCode(),
                sms.getTencentSmsSdkAppId(), sms.getTencentSmsAccessKeyId(), sms.getTencentSmsAccessKeySecret(),
                sms.getTencentSmsRegion());
    }

    @Override
    public Boolean sendPurchaseSuccess(String mobile, String courseName, Long orderNo, Sms sms) {
        return tencent(mobile, new String[]{courseName, String.valueOf(orderNo)}, sms.getTencentSmsSignName(),
                sms.getTencentSmsPurchaseCode(), sms.getTencentSmsSdkAppId(), sms.getTencentSmsAccessKeyId(),
                sms.getTencentSmsAccessKeySecret(), sms.getTencentSmsRegion());
    }

    private static Boolean tencent(String phone, String[] templateParams, String signName, String templateId,
                                   String sdkAppId, String secretId, String secretKey, String region) {
        if (StrUtil.hasBlank(secretId, secretKey, sdkAppId, signName, templateId)) {
            log.error("腾讯云短信参数缺失，phone={}", phone);
            return false;
        }
        try {
            Credential cred = new Credential(secretId, secretKey);
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint(ENDPOINT);
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            SmsClient client = new SmsClient(cred, StrUtil.blankToDefault(region, DEFAULT_REGION), clientProfile);

            SendSmsRequest req = new SendSmsRequest();
            req.setSmsSdkAppId(sdkAppId);
            req.setSignName(signName);
            req.setTemplateId(templateId);
            req.setTemplateParamSet(templateParams);
            // 腾讯云要求手机号带 +86 前缀（中国大陆）
            req.setPhoneNumberSet(new String[]{normalizePhone(phone)});

            SendSmsResponse resp = client.SendSms(req);
            if (resp.getSendStatusSet() == null || resp.getSendStatusSet().length == 0) {
                log.error("腾讯云短信返回为空，phone={}, requestId={}", phone, resp.getRequestId());
                return false;
            }
            SendStatus status = resp.getSendStatusSet()[0];
            if (!"Ok".equals(status.getCode())) {
                log.error("腾讯云短信发送失败，phone={}, code={}, message={}, requestId={}",
                        phone, status.getCode(), status.getMessage(), resp.getRequestId());
                return false;
            }
            return true;
        } catch (Exception e) {
            log.error("腾讯云短信发送异常，phone={}", phone, e);
            return false;
        }
    }

    private static String normalizePhone(String phone) {
        if (phone == null) {
            return null;
        }
        if (phone.startsWith("+")) {
            return phone;
        }
        return "+86" + phone;
    }
}
