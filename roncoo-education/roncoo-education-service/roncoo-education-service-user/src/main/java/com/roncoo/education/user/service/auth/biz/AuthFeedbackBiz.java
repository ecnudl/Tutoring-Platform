package com.roncoo.education.user.service.auth.biz;

import com.roncoo.education.common.base.BaseBiz;
import com.roncoo.education.common.base.ThreadContext;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.user.dao.FeedbackDao;
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

    public Result<?> page(Map<String, Object> req) {
        Long userId = ThreadContext.userId();
        int pageCurrent = req.get("pageCurrent") != null ? Integer.parseInt(req.get("pageCurrent").toString()) : 1;
        int pageSize = req.get("pageSize") != null ? Integer.parseInt(req.get("pageSize").toString()) : 20;
        FeedbackExample example = new FeedbackExample();
        example.createCriteria().andUserIdEqualTo(userId);
        example.setOrderByClause("gmt_create desc");
        return Result.success(feedbackDao.page(pageCurrent, pageSize, example));
    }

    public Result<String> submit(Map<String, Object> req) {
        Long userId = ThreadContext.userId();
        String content = req.get("content") != null ? req.get("content").toString() : "";
        String contact = req.get("contact") != null ? req.get("contact").toString() : "";
        if (!StringUtils.hasText(content)) return Result.error("反馈内容不能为空");
        Feedback fb = new Feedback();
        fb.setUserId(userId);
        fb.setContent(content);
        fb.setContact(contact);
        fb.setFbStatus(0); // 待处理
        feedbackDao.save(fb);
        return Result.success("提交成功，感谢您的反馈");
    }
}
