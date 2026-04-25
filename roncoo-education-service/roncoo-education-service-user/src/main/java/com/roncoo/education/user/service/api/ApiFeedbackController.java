package com.roncoo.education.user.service.api;

import com.roncoo.education.common.base.BaseBiz;
import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.common.core.enums.UserTypeEnum;
import com.roncoo.education.user.dao.FeedbackDao;
import com.roncoo.education.user.dao.UsersDao;
import com.roncoo.education.user.dao.impl.mapper.entity.Feedback;
import com.roncoo.education.user.dao.impl.mapper.entity.FeedbackExample;
import com.roncoo.education.user.dao.impl.mapper.entity.Users;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
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
 * 仅返回审核通过 (fb_status=1) 的感言。
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/api/feedback")
public class ApiFeedbackController extends BaseBiz {

    @NotNull
    private final FeedbackDao feedbackDao;

    @NotNull
    private final UsersDao usersDao;

    @GetMapping(value = "/latest")
    public Result<List<FeedbackItem>> latest(@RequestParam(required = false, defaultValue = "4") Integer limit) {
        if (limit == null || limit <= 0) limit = 4;
        FeedbackExample example = new FeedbackExample();
        FeedbackExample.Criteria c = example.createCriteria();
        c.andStatusIdEqualTo(1);
        c.andFbStatusEqualTo(1);
        example.setOrderByClause("id desc");
        Page<Feedback> page = feedbackDao.page(1, limit, example);
        List<FeedbackItem> result = new ArrayList<>();
        if (page.getList() != null) {
            for (Feedback f : page.getList()) {
                FeedbackItem it = new FeedbackItem();
                it.setId(f.getId());
                it.setContent(f.getContent());
                it.setGmtCreate(f.getGmtCreate());
                it.setAuthor(maskAuthor(f.getContact()));
                it.setRole(resolveRole(f.getUserId()));
                result.add(it);
            }
        }
        return Result.success(result);
    }

    private String resolveRole(Long userId) {
        if (userId == null) return "热心用户";
        try {
            Users u = usersDao.getById(userId);
            if (u == null) return "热心用户";
            if (UserTypeEnum.TUTOR.getCode().equals(u.getUserType())) return "教员";
            if (UserTypeEnum.STUDENT.getCode().equals(u.getUserType())) return "学生家长";
        } catch (Exception ignored) {}
        return "热心用户";
    }

    private static String maskAuthor(String contact) {
        if (contact == null || contact.isEmpty()) return "热心用户";
        String digits = contact.replaceAll("\\D", "");
        if (digits.length() >= 11) {
            return digits.substring(0, 3) + "****" + digits.substring(digits.length() - 4);
        }
        if (contact.length() <= 1) return contact;
        if (contact.length() <= 3) return contact.charAt(0) + "**";
        return contact.substring(0, 1) + "**" + contact.substring(contact.length() - 1);
    }

    @Data
    public static class FeedbackItem implements Serializable {
        private static final long serialVersionUID = 1L;
        private Long id;
        private String author;
        private String role;
        private String content;
        private LocalDateTime gmtCreate;
    }
}
