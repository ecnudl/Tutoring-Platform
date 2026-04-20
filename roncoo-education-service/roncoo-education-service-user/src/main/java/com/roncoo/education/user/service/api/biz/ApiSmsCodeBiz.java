package com.roncoo.education.user.service.api.biz;

import cn.hutool.core.util.ObjectUtil;
import com.roncoo.education.common.cache.CacheRedis;
import com.roncoo.education.common.core.base.Constants;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.common.core.enums.SmsPlatformEnum;
import com.roncoo.education.common.sms.Sms;
import com.roncoo.education.common.sms.SmsFace;
import com.roncoo.education.system.feign.interfaces.IFeignSysConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiSmsCodeBiz {

    private static final int CODE_EXPIRE_MINUTES = 5;

    private final CacheRedis cacheRedis;
    private final Map<String, SmsFace> smsFaceMap;
    private final IFeignSysConfig feignSysConfig;

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
        // 5 分钟内最多 2 次，防刷
        if (!sendCodeCheck(mobile)) {
            return Result.error("操作频繁，请稍后再试");
        }

        Sms sms = feignSysConfig.getSms();
        if (sms == null || sms.getSmsPlatform() == null) {
            return Result.error("短信平台未配置");
        }
        SmsPlatformEnum platformEnum = SmsPlatformEnum.byCode(sms.getSmsPlatform());
        if (platformEnum == null) {
            return Result.error("短信平台配置不合法");
        }
        SmsFace smsFace = smsFaceMap.get(platformEnum.getMode());
        if (ObjectUtil.isEmpty(smsFace)) {
            return Result.error("未找到短信服务实现");
        }

        String code = generateCode();
        boolean sent = smsFace.sendVerCode(mobile, code, sms);
        if (!sent) {
            return Result.error("发送失败，请稍后重试");
        }
        cacheRedis.set(Constants.RedisPre.CODE + mobile, code, CODE_EXPIRE_MINUTES, TimeUnit.MINUTES);
        log.info("验证码已发送: mobile={}, scene={}, platform={}", mobile, scene, platformEnum.getDesc());
        return Result.success("验证码已发送");
    }

    /**
     * 5 分钟内，同一个手机号不能超过 2 次发送验证码
     */
    private Boolean sendCodeCheck(String mobile) {
        String count = cacheRedis.get(Constants.RedisPre.CODE_STAT + mobile);
        if (count != null && !count.isEmpty()) {
            int num = cacheRedis.set(Constants.RedisPre.CODE_STAT + mobile, Integer.parseInt(count) + 1, CODE_EXPIRE_MINUTES, TimeUnit.MINUTES);
            return num < 2;
        }
        cacheRedis.set(Constants.RedisPre.CODE_STAT + mobile, 1, CODE_EXPIRE_MINUTES, TimeUnit.MINUTES);
        return Boolean.TRUE;
    }

    /**
     * 校验验证码。校验通过后删除，避免重放。
     */
    public boolean verifyCode(String mobile, String code) {
        if (mobile == null || code == null) {
            return false;
        }
        String cached = cacheRedis.get(Constants.RedisPre.CODE + mobile);
        if (cached != null && cached.equals(code)) {
            cacheRedis.delete(Constants.RedisPre.CODE + mobile);
            return true;
        }
        return false;
    }

    private String generateCode() {
        return String.format("%06d", new Random().nextInt(1000000));
    }
}
