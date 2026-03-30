package com.roncoo.education.user.service.admin.biz;

import com.roncoo.education.common.base.BaseBiz;
import com.roncoo.education.common.base.ThreadContext;
import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.user.dao.FeedbackDao;
import com.roncoo.education.user.dao.impl.mapper.entity.Feedback;
import com.roncoo.education.user.dao.impl.mapper.entity.FeedbackExample;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AdminFeedbackBiz extends BaseBiz {
    @NotNull
    private final FeedbackDao feedbackDao;

    public Result<?> page(Map<String, Object> req) {
        int pageCurrent = req.get("pageCurrent") != null ? Integer.parseInt(req.get("pageCurrent").toString()) : 1;
        int pageSize = req.get("pageSize") != null ? Integer.parseInt(req.get("pageSize").toString()) : 20;
        Integer fbStatus = req.get("fbStatus") != null ? Integer.parseInt(req.get("fbStatus").toString()) : null;

        FeedbackExample example = new FeedbackExample();
        FeedbackExample.Criteria c = example.createCriteria();
        if (fbStatus != null) {
            c.andFbStatusEqualTo(fbStatus);
        }
        example.setOrderByClause("gmt_create desc");
        Page<Feedback> page = feedbackDao.page(pageCurrent, pageSize, example);
        return Result.success(page);
    }

    public Result<?> view(Long id) {
        Feedback fb = feedbackDao.getById(id);
        if (fb == null) {
            return Result.error("反馈不存在");
        }
        return Result.success(fb);
    }

    public Result<String> reply(Map<String, Object> req) {
        Long id = Long.parseLong(req.get("id").toString());
        String replyText = req.get("reply") != null ? req.get("reply").toString() : "";
        if (!StringUtils.hasText(replyText)) {
            return Result.error("回复内容不能为空");
        }

        Feedback fb = feedbackDao.getById(id);
        if (fb == null) {
            return Result.error("反馈不存在");
        }

        Feedback update = new Feedback();
        update.setId(id);
        update.setReply(replyText);
        update.setFbStatus(1); // 已回复
        update.setReplyTime(LocalDateTime.now());
        try {
            update.setReplierId(ThreadContext.userId());
        } catch (Exception e) {
            update.setReplierId(0L);
        }
        feedbackDao.updateById(update);
        return Result.success("回复成功");
    }
}
