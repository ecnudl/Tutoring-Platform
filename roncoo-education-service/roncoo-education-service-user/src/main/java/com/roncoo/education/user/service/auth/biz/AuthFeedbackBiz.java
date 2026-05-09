package com.roncoo.education.user.service.auth.biz;

import com.roncoo.education.common.base.BaseBiz;
import com.roncoo.education.common.base.ThreadContext;
import com.roncoo.education.common.cache.CacheRedis;
import com.roncoo.education.common.core.base.Constants;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.common.tools.IpUtil;
import com.roncoo.education.user.dao.FeedbackDao;
import com.roncoo.education.user.dao.UsersDao;
import com.roncoo.education.user.dao.impl.mapper.entity.Feedback;
import com.roncoo.education.user.dao.impl.mapper.entity.FeedbackExample;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class AuthFeedbackBiz extends BaseBiz {
    @NotNull
    private final FeedbackDao feedbackDao;
    @NotNull
    private final UsersDao usersDao;
    @NotNull
    private final CacheRedis cacheRedis;
    @NotNull
    private final HttpServletRequest request;

    /** 单用户每小时最多 3 条感言 (够正常用户用; 阻断脚本灌水) */
    private static final int USER_LIMIT_PER_HOUR = 3;
    /** 同 IP 每小时最多 10 条 (兼顾家庭/办公网共享出口, 仍能拦机器人 burst) */
    private static final int IP_LIMIT_PER_HOUR = 10;

    public Result<?> page(Map<String, Object> req) {
        Long userId = ThreadContext.userId();
        int pageCurrent = req.get("pageCurrent") != null ? Integer.parseInt(req.get("pageCurrent").toString()) : 1;
        int pageSize = req.get("pageSize") != null ? Integer.parseInt(req.get("pageSize").toString()) : 20;
        FeedbackExample example = new FeedbackExample();
        example.createCriteria().andUserIdEqualTo(userId);
        example.setOrderByClause("gmt_create desc");
        return Result.success(feedbackDao.page(pageCurrent, pageSize, example));
    }

    /**
     * 提交家教感言。教员和家长（学员）都可发布；提交后状态为待审核 (fb_status=0)。
     * 限流:
     *   - 同账号每小时最多 3 条
     *   - 同 IP 每小时最多 10 条 (双重保险, 防换号继续灌)
     * 内容校验:
     *   - 至少 10 个有效字符 (剔除空白/不可见 Unicode 后)
     *   - 长度上限 500
     */
    public Result<String> submit(Map<String, Object> req) {
        Long userId = ThreadContext.userId();
        if (userId == null) return Result.error("请先登录");
        if (usersDao.getById(userId) == null) return Result.error("用户不存在");

        String content = req.get("content") != null ? req.get("content").toString().trim() : "";
        String contact = req.get("contact") != null ? req.get("contact").toString().trim() : "";
        if (!StringUtils.hasText(content)) return Result.error("感言内容不能为空");
        if (content.length() > 500) return Result.error("感言内容过长，请控制在 500 字以内");

        // 内容质量校验: 把空白和不可见 Unicode (零宽连字符 / LRM / RLM 等控制符) 过滤后, 至少 10 个可见字符
        String visible = stripInvisible(content);
        if (visible.length() < 10) {
            return Result.error("感言内容过短或含大量不可见字符，请认真填写至少 10 字");
        }

        // 限流: 单账号 (原子 INCR + EXPIRE)
        String userKey = Constants.RedisPre.RATE_LIMIT_IP + "fb:user:" + userId;
        if (atomicBump(userKey, 3600) > USER_LIMIT_PER_HOUR) {
            return Result.error("您提交过于频繁，每小时最多 " + USER_LIMIT_PER_HOUR + " 条，请稍后再试");
        }
        // 限流: 同 IP (防同人换号)
        String ip = IpUtil.getIpAddress(request);
        if (StringUtils.hasText(ip)) {
            String ipKey = Constants.RedisPre.RATE_LIMIT_IP + "fb:ip:" + ip;
            if (atomicBump(ipKey, 3600) > IP_LIMIT_PER_HOUR) {
                return Result.error("您提交过于频繁，请稍后再试");
            }
        }

        Feedback fb = new Feedback();
        fb.setUserId(userId);
        fb.setContent(content);
        fb.setContact(contact);
        fb.setFbStatus(0); // 0=待审核
        feedbackDao.save(fb);
        return Result.success("提交成功，审核通过后将展示在首页");
    }

    // Lua: 原子 INCR + 修复 TTL (单往返, 永不留无 TTL 的 key)
    private static final org.springframework.data.redis.core.script.DefaultRedisScript<Long> INCR_WITH_TTL_SCRIPT =
            new org.springframework.data.redis.core.script.DefaultRedisScript<>(
                    "local n = redis.call('INCR', KEYS[1]); " +
                    "if redis.call('TTL', KEYS[1]) < 0 then redis.call('EXPIRE', KEYS[1], ARGV[1]) end; " +
                    "return n",
                    Long.class);

    /** 原子 INCR + 修复 TTL. */
    private long atomicBump(String key, int ttlSeconds) {
        Long n = cacheRedis.getStringRedisTemplate().execute(INCR_WITH_TTL_SCRIPT,
                java.util.Collections.singletonList(key),
                String.valueOf(ttlSeconds));
        return n != null ? n : 0L;
    }

    /** 去掉空白 + 常见不可见控制字符 (零宽 / 方向标 / BOM / NBSP 等) */
    private static String stripInvisible(String s) {
        if (s == null) return "";
        StringBuilder out = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); ) {
            int cp = s.codePointAt(i);
            i += Character.charCount(cp);
            if (Character.isWhitespace(cp)) continue;
            // 不可见 / 方向 / 零宽 系列
            if (cp == 0x00A0  // NBSP
                    || cp == 0x200B || cp == 0x200C || cp == 0x200D  // ZWSP / ZWNJ / ZWJ
                    || cp == 0x200E || cp == 0x200F  // LRM / RLM
                    || cp == 0x202A || cp == 0x202B || cp == 0x202C || cp == 0x202D || cp == 0x202E
                    || cp == 0x2060  // WORD JOINER
                    || cp == 0xFEFF  // BOM
                    || (cp >= 0x2066 && cp <= 0x2069)) continue;
            out.appendCodePoint(cp);
        }
        return out.toString();
    }
}
