package com.roncoo.education.user.service.api;

import com.roncoo.education.common.base.BaseBiz;
import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.common.core.enums.UserSexEnum;
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
 *
 * 展示规则:
 *   - 教员: 姓氏 + "教员"
 *   - 家长(学员账号): 姓氏 + (女士|男士)  // 依据 users.user_sex
 *   - 无法识别姓氏: "热心用户"
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
                Users u = safeGetUser(f.getUserId());
                Display d = buildDisplay(f.getContact(), u);
                FeedbackItem it = new FeedbackItem();
                it.setId(f.getId());
                it.setContent(f.getContent());
                it.setGmtCreate(f.getGmtCreate());
                it.setAuthor(d.author);
                it.setRole(d.role);
                result.add(it);
            }
        }
        return Result.success(result);
    }

    private Users safeGetUser(Long userId) {
        if (userId == null) return null;
        try { return usersDao.getById(userId); } catch (Exception e) { return null; }
    }

    private static Display buildDisplay(String contact, Users user) {
        Integer userType = user != null ? user.getUserType() : null;
        Integer userSex  = user != null ? user.getUserSex()  : null;
        String nickname  = user != null ? user.getNickname() : null;

        String surname = firstChineseChar(contact);
        if (surname.isEmpty()) surname = firstChineseChar(nickname);

        String role;
        String suffix;
        if (UserTypeEnum.TUTOR.getCode().equals(userType)) {
            role = "教员"; suffix = "教员";
        } else if (UserTypeEnum.STUDENT.getCode().equals(userType)) {
            role = "学生家长";
            suffix = UserSexEnum.FEMALE.getCode().equals(userSex) ? "女士" : "男士";
        } else {
            role = "热心用户"; suffix = "";
        }

        String author = !surname.isEmpty() ? surname + suffix : "热心用户";
        return new Display(author, role);
    }

    /** 取字符串中第一个汉字 (CJK 统一表意), 没有则返回 "" */
    private static String firstChineseChar(String s) {
        if (s == null) return "";
        int n = s.length();
        for (int i = 0; i < n; ) {
            int cp = s.codePointAt(i);
            if (cp >= 0x4E00 && cp <= 0x9FFF) {
                return new String(Character.toChars(cp));
            }
            i += Character.charCount(cp);
        }
        return "";
    }

    private static class Display {
        final String author;
        final String role;
        Display(String a, String r) { this.author = a; this.role = r; }
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
