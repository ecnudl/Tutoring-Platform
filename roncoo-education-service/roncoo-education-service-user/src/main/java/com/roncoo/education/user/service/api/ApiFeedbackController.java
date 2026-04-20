package com.roncoo.education.user.service.api;

import com.roncoo.education.common.base.BaseBiz;
import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.common.tools.BeanUtil;
import com.roncoo.education.user.dao.FeedbackDao;
import com.roncoo.education.user.dao.impl.mapper.entity.Feedback;
import com.roncoo.education.user.dao.impl.mapper.entity.FeedbackExample;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 公开 feedback 最新列表（首页家教感言）。
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/api/feedback")
public class ApiFeedbackController extends BaseBiz {

    @NotNull
    private final FeedbackDao feedbackDao;

    @GetMapping(value = "/latest")
    public Result<List<FeedbackItem>> latest(@RequestParam(required = false, defaultValue = "4") Integer limit) {
        if (limit == null || limit <= 0) limit = 4;
        FeedbackExample example = new FeedbackExample();
        FeedbackExample.Criteria c = example.createCriteria();
        c.andStatusIdEqualTo(1);
        example.setOrderByClause("id desc");
        Page<Feedback> page = feedbackDao.page(1, limit, example);
        List<FeedbackItem> result = new ArrayList<>();
        if (page.getList() != null) {
            for (Feedback f : page.getList()) {
                FeedbackItem it = BeanUtil.copyProperties(f, FeedbackItem.class);
                it.setAuthor(maskAuthor(f.getContact()));
                result.add(it);
            }
        }
        return Result.success(result);
    }

    private static String maskAuthor(String contact) {
        if (contact == null || contact.isEmpty()) return "热心用户";
        // 纯数字手机号 -> 1XX****XXXX 前 3 位 + **** + 后 4 位；否则保留前两字符
        String digits = contact.replaceAll("\\D", "");
        if (digits.length() >= 11) {
            return digits.substring(0, 3) + "****" + digits.substring(digits.length() - 4);
        }
        return contact.length() > 2 ? contact.substring(0, 2) + "**" : contact;
    }

    @Data
    @Accessors(chain = true)
    public static class FeedbackItem implements Serializable {
        private static final long serialVersionUID = 1L;
        private Long id;
        private String author;
        private String content;
        private LocalDateTime gmtCreate;
    }
}
