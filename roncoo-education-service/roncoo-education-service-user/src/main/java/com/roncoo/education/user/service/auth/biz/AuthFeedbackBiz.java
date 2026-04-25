package com.roncoo.education.user.service.auth.biz;

import com.roncoo.education.common.base.BaseBiz;
import com.roncoo.education.common.base.ThreadContext;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.user.dao.FeedbackDao;
import com.roncoo.education.user.dao.UsersDao;
import com.roncoo.education.user.dao.impl.mapper.entity.Feedback;
import com.roncoo.education.user.dao.impl.mapper.entity.FeedbackExample;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class AuthFeedbackBiz extends BaseBiz {
    @NotNull
    private final FeedbackDao feedbackDao;
    @NotNull
    private final UsersDao usersDao;

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
     */
    public Result<String> submit(Map<String, Object> req) {
        Long userId = ThreadContext.userId();
        if (userId == null) return Result.error("请先登录");
        if (usersDao.getById(userId) == null) return Result.error("用户不存在");

        String content = req.get("content") != null ? req.get("content").toString().trim() : "";
        String contact = req.get("contact") != null ? req.get("contact").toString().trim() : "";
        if (!StringUtils.hasText(content)) return Result.error("感言内容不能为空");
        if (content.length() > 500) return Result.error("感言内容过长，请控制在 500 字以内");

        Feedback fb = new Feedback();
        fb.setUserId(userId);
        fb.setContent(content);
        fb.setContact(contact);
        fb.setFbStatus(0); // 0=待审核
        feedbackDao.save(fb);
        return Result.success("提交成功，审核通过后将展示在首页");
    }
}
