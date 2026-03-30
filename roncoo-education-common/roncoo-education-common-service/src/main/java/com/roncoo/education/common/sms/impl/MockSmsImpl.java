package com.roncoo.education.common.sms.impl;

import com.roncoo.education.common.sms.Sms;
import com.roncoo.education.common.sms.SmsFace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("mockSms")
public class MockSmsImpl implements SmsFace {

    @Override
    public Boolean sendVerCode(String mobile, String code, Sms sms) {
        log.warn("【模拟短信】发送验证码 -> 手机号: {}, 验证码: {}", mobile, code);
        return true;
    }

    @Override
    public Boolean sendPurchaseSuccess(String mobile, String courseName, Long orderNo, Sms sms) {
        log.warn("【模拟短信】购买成功通知 -> 手机号: {}, 课程: {}, 订单号: {}", mobile, courseName, orderNo);
        return true;
    }
}
