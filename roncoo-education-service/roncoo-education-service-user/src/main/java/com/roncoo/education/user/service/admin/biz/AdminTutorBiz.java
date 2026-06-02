package com.roncoo.education.user.service.admin.biz;

import com.roncoo.education.common.base.BaseBiz;
import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.base.page.PageUtil;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.user.dao.TutorProfileDao;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorProfile;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorProfileExample;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AdminTutorBiz extends BaseBiz {
    @NotNull
    private final TutorProfileDao tutorProfileDao;

    public Result<?> page(Map<String, Object> req) {
        int pageCurrent = req.get("pageCurrent") != null ? Integer.parseInt(req.get("pageCurrent").toString()) : 1;
        int pageSize = req.get("pageSize") != null ? Integer.parseInt(req.get("pageSize").toString()) : 20;
        Integer auditStatus = req.get("auditStatus") != null ? Integer.parseInt(req.get("auditStatus").toString()) : null;
        Integer statusId = req.get("statusId") != null ? Integer.parseInt(req.get("statusId").toString()) : null;

        String keyword = req.get("keyword") != null ? req.get("keyword").toString().trim() : null;

        TutorProfileExample example = new TutorProfileExample();
        TutorProfileExample.Criteria c = example.createCriteria();
        if (auditStatus != null) {
            c.andAuditStatusEqualTo(auditStatus);
        }
        if (statusId != null) {
            c.andStatusIdEqualTo(statusId);
        }
        example.setOrderByClause("gmt_create desc");

        // 无关键词: 直接走数据库分页
        if (!StringUtils.hasText(keyword)) {
            return Result.success(tutorProfileDao.page(pageCurrent, pageSize, example));
        }

        // 有关键词: 取出状态过滤后的全部, 在 Java 端对 姓名/教师编号/邮箱/籍贯/工作单位 做模糊匹配, 再切片分页
        // (教员量级小, 与学员需求搜索同样的做法; 避免改生成的 Example 或拼易错的 OR 条件)
        List<TutorProfile> all = tutorProfileDao.page(1, 100000, example).getList();
        if (all == null) {
            all = new java.util.ArrayList<>();
        }
        final String kw = keyword.toLowerCase();
        // 教师编号常带 T 前缀, 去掉 T 也能匹配 (输 374657 / T374657 都行)
        final String kwNoT = kw.startsWith("t") ? kw.substring(1) : kw;
        List<TutorProfile> matched = all.stream().filter(t ->
                likeField(t.getRealName(), kw)
                        || likeField(t.getEmail(), kw)
                        || likeField(t.getHometownProvince(), kw)
                        || likeField(t.getUniversity(), kw)
                        || likeField(t.getWorkUnit(), kw)
                        || likeDisplayNo(t.getDisplayNo(), kw, kwNoT)
        ).collect(java.util.stream.Collectors.toList());

        int total = matched.size();
        int totalPage = (int) Math.ceil(total / (double) pageSize);
        int from = Math.max(0, (pageCurrent - 1) * pageSize);
        int to = Math.min(from + pageSize, total);
        List<TutorProfile> pageList = from < to ? matched.subList(from, to) : new java.util.ArrayList<>();
        return Result.success(new Page<>(total, totalPage, pageCurrent, pageSize, pageList));
    }

    /** 字段不区分大小写包含关键词 */
    private static boolean likeField(String field, String kwLower) {
        return field != null && field.toLowerCase().contains(kwLower);
    }

    /** 教师编号匹配: 原串或去掉 T 前缀的串命中即可 */
    private static boolean likeDisplayNo(String displayNo, String kw, String kwNoT) {
        if (displayNo == null) {
            return false;
        }
        String d = displayNo.toLowerCase();
        return d.contains(kw) || d.contains(kwNoT);
    }

    public Result<?> view(Long id) {
        TutorProfile profile = tutorProfileDao.getById(id);
        if (profile == null) {
            return Result.error("教员不存在");
        }
        return Result.success(profile);
    }

    public Result<String> edit(Map<String, Object> req) {
        Long id = Long.parseLong(req.get("id").toString());
        TutorProfile profile = tutorProfileDao.getById(id);
        if (profile == null) {
            return Result.error("教员不存在");
        }
        TutorProfile update = new TutorProfile();
        update.setId(id);
        if (req.containsKey("sort")) {
            update.setSort(Integer.parseInt(req.get("sort").toString()));
        }
        if (req.containsKey("statusId")) {
            update.setStatusId(Integer.parseInt(req.get("statusId").toString()));
        }
        tutorProfileDao.updateById(update);
        return Result.success("操作成功");
    }

    public Result<String> delete(Long id) {
        TutorProfile profile = tutorProfileDao.getById(id);
        if (profile == null) {
            return Result.error("教员不存在");
        }
        tutorProfileDao.deleteById(id);
        return Result.success("删除成功");
    }
}
