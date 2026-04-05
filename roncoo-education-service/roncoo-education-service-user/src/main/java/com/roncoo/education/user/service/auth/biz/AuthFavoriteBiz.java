package com.roncoo.education.user.service.auth.biz;

import com.roncoo.education.common.base.BaseBiz;
import com.roncoo.education.common.base.ThreadContext;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.common.tools.BeanUtil;
import com.roncoo.education.user.dao.FavoriteDao;
import com.roncoo.education.user.dao.TutorProfileDao;
import com.roncoo.education.user.dao.impl.mapper.entity.Favorite;
import com.roncoo.education.user.dao.impl.mapper.entity.FavoriteExample;
import com.roncoo.education.user.dao.impl.mapper.entity.TutorProfile;
import com.roncoo.education.user.service.auth.resp.AuthFavoriteResp;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AuthFavoriteBiz extends BaseBiz {
    @NotNull
    private final FavoriteDao favoriteDao;

    @NotNull
    private final TutorProfileDao tutorProfileDao;

    public Result<?> page(Map<String, Object> req) {
        Long userId = ThreadContext.userId();
        int pageCurrent = req.get("pageCurrent") != null ? Integer.parseInt(req.get("pageCurrent").toString()) : 1;
        int pageSize = req.get("pageSize") != null ? Integer.parseInt(req.get("pageSize").toString()) : 20;
        FavoriteExample example = new FavoriteExample();
        example.createCriteria().andUserIdEqualTo(userId);
        example.setOrderByClause("gmt_create desc");
        return Result.success(favoriteDao.page(pageCurrent, pageSize, example));
    }

    public Result<List<AuthFavoriteResp>> list(Integer targetType) {
        Long userId = ThreadContext.userId();
        FavoriteExample example = new FavoriteExample();
        FavoriteExample.Criteria c = example.createCriteria();
        c.andUserIdEqualTo(userId);
        if (targetType != null) {
            c.andTargetTypeEqualTo(targetType);
        }
        example.setOrderByClause("gmt_create desc");
        List<Favorite> favorites = favoriteDao.listByExample(example);

        List<AuthFavoriteResp> result = BeanUtil.copyProperties(favorites, AuthFavoriteResp.class);

        // 如果是教员类型，查询教员详情
        if (targetType != null && targetType == 1) {
            List<Long> tutorIds = favorites.stream()
                .map(Favorite::getTargetId)
                .collect(Collectors.toList());

            if (!tutorIds.isEmpty()) {
                List<TutorProfile> tutors = tutorProfileDao.listByIds(tutorIds);
                Map<Long, TutorProfile> tutorMap = tutors.stream()
                    .collect(Collectors.toMap(TutorProfile::getId, t -> t));

                result.forEach(resp -> {
                    TutorProfile tutor = tutorMap.get(resp.getTargetId());
                    if (tutor != null) {
                        AuthFavoriteResp.TutorInfo info = BeanUtil.copyProperties(tutor, AuthFavoriteResp.TutorInfo.class);
                        resp.setTutor(info);
                    }
                });
            }
        }

        return Result.success(result);
    }

    public Result<String> add(Integer targetType, Long targetId) {
        Long userId = ThreadContext.userId();
        if (targetType == null || targetId == null) return Result.error("参数不完整");
        Favorite existing = favoriteDao.getByUserIdAndTarget(userId, targetType, targetId);
        if (existing != null) return Result.error("已收藏");
        Favorite fav = new Favorite();
        fav.setUserId(userId);
        fav.setTargetType(targetType);
        fav.setTargetId(targetId);
        favoriteDao.save(fav);
        return Result.success("收藏成功");
    }

    public Result<String> remove(Long id) {
        Long userId = ThreadContext.userId();
        Favorite fav = favoriteDao.getById(id);
        if (fav == null || !fav.getUserId().equals(userId)) return Result.error("记录不存在");
        favoriteDao.deleteById(id);
        return Result.success("取消收藏");
    }

    public Result<Boolean> check(Integer targetType, Long targetId) {
        Long userId = ThreadContext.userId();
        Favorite existing = favoriteDao.getByUserIdAndTarget(userId, targetType, targetId);
        return Result.success(existing != null);
    }
}
