package com.roncoo.education.user.service.api.biz;

import com.roncoo.education.common.cache.CacheRedis;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.common.sms.SmsFace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class ApiSmsCodeBiz {

    private static final String SMS_CODE_PREFIX = "sms:code:";
    private static final int CODE_EXPIRE_MINUTES = 5;

    private final CacheRedis cacheRedis;
    private final SmsFace smsFace;

    public ApiSmsCodeBiz(CacheRedis cacheRedis, @Qualifier("mockSms") SmsFace smsFace) {
        this.cacheRedis = cacheRedis;
        this.smsFace = smsFace;
    }

    public Result<String> sendRegisterCode(String mobile) {
        if (mobile == null || !mobile.matches("^1[3-9]\\d{9}$")) {
            return Result.error("手机号格式不正确");
        }
        return doSendCode(mobile, "register");
    }

    public Result<String> sendLoginCode(String mobile) {
        if (mobile == null || !mobile.matches("^1[3-9]\\d{9}$")) {
            return Result.error("手机号格式不正确");
        }
        return doSendCode(mobile, "login");
    }

    public Result<String> sendResetCode(String mobile) {
        if (mobile == null || !mobile.matches("^1[3-9]\\d{9}$")) {
            return Result.error("手机号格式不正确");
        }
        return doSendCode(mobile, "reset");
    }

    private Result<String> doSendCode(String mobile, String scene) {
        String key = SMS_CODE_PREFIX + scene + ":" + mobile;
        // 防频繁发送
        String existing = cacheRedis.get(key);
        if (existing != null) {
            return Result.error("验证码已发送，请稍后再试");
        }
        String code = generateCode();
        // 存入Redis，5分钟过期
        cacheRedis.set(key, code, CODE_EXPIRE_MINUTES, TimeUnit.MINUTES);
        // 通过SmsFace接口发送（当前为Mock实现，后续切换真实短信服务）
        boolean sent = smsFace.sendVerCode(mobile, code, null);
        if (sent) {
            log.info("验证码已发送: mobile={}, scene={}, code={}", mobile, scene, code);
            return Result.success("验证码已发送");
        }
        cacheRedis.delete(key);
        return Result.error("发送失败，请稍后重试");
    }

    /**
     * 校验验证码
     */
    public boolean verifyCode(String mobile, String code, String scene) {
        if (mobile == null || code == null) {
            return false;
        }
        String key = SMS_CODE_PREFIX + scene + ":" + mobile;
        String cached = cacheRedis.get(key);
        if (cached != null && cached.equals(code)) {
            cacheRedis.delete(key);
            return true;
        }
        return false;
    }

    private String generateCode() {
        return String.format("%06d", new Random().nextInt(1000000));
    }
}
