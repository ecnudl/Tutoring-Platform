package com.roncoo.education.user.service.auth.biz;

import com.roncoo.education.common.base.BaseBiz;
import com.roncoo.education.common.base.ThreadContext;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.common.core.enums.TutorAuditStatusEnum;
import com.roncoo.education.user.dao.TutorProfileDao;
import com.roncoo.education.user.dao.TutorShortlistDao;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorProfile;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorShortlist;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorShortlistExample;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class AuthShortlistBiz extends BaseBiz {
    @NotNull
    private final TutorShortlistDao tutorShortlistDao;
    @NotNull
    private final TutorProfileDao tutorProfileDao;

    public Result<?> page(Map<String, Object> req) {
        Long userId = ThreadContext.userId();
        int pageCurrent = req.get("pageCurrent") != null ? Integer.parseInt(req.get("pageCurrent").toString()) : 1;
        int pageSize = req.get("pageSize") != null ? Integer.parseInt(req.get("pageSize").toString()) : 20;
        TutorShortlistExample example = new TutorShortlistExample();
        example.createCriteria().andUserIdEqualTo(userId);
        example.setOrderByClause("gmt_create desc");
        return Result.success(tutorShortlistDao.page(pageCurrent, pageSize, example));
    }

    public Result<String> add(Map<String, Object> req) {
        Long userId = ThreadContext.userId();
        Long tutorId = req.get("tutorId") != null ? Long.parseLong(req.get("tutorId").toString()) : null;
        if (tutorId == null) {
            return Result.error("教员ID不能为空");
        }
        TutorProfile profile = tutorProfileDao.getById(tutorId);
        if (profile == null) {
            return Result.error("教员不存在");
        }
        if (!TutorAuditStatusEnum.APPROVED.getCode().equals(profile.getAuditStatus())) {
            return Result.error("该教员未通过审核");
        }
        if (profile.getUserId().equals(userId)) {
            return Result.error("不能将自己加入备选");
        }
        TutorShortlist existing = tutorShortlistDao.getByUserIdAndTutorId(userId, tutorId);
        if (existing != null) {
            return Result.error("该教员已在您的备选列表中");
        }
        TutorShortlist shortlist = new TutorShortlist();
        shortlist.setUserId(userId);
        shortlist.setTutorId(tutorId);
        shortlist.setRemark(req.get("remark") != null ? req.get("remark").toString() : "");
        tutorShortlistDao.save(shortlist);
        return Result.success("添加成功");
    }

    public Result<String> remove(Long id) {
        Long userId = ThreadContext.userId();
        TutorShortlist shortlist = tutorShortlistDao.getById(id);
        if (shortlist == null || !shortlist.getUserId().equals(userId)) {
            return Result.error("记录不存在或无权操作");
        }
        tutorShortlistDao.deleteById(id);
        return Result.success("移除成功");
    }
}
