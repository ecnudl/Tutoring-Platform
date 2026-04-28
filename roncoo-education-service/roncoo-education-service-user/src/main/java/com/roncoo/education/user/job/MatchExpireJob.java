package com.roncoo.education.user.job;

import com.roncoo.education.common.core.enums.RequirementStatusEnum;
import com.roncoo.education.common.core.enums.ReservationStatusEnum;
import com.roncoo.education.user.dao.TutorRequirementDao;
import com.roncoo.education.user.dao.TutorReservationDao;
import com.roncoo.education.user.dao.impl.mapper.entity.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 已匹配的预约/需求 1 周后软清理 (换状态, 列表层过滤掉, 不删行)
 * 每天凌晨 03:30 跑一次
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MatchExpireJob {

    @NotNull
    private final TutorReservationDao tutorReservationDao;
    @NotNull
    private final TutorRequirementDao tutorRequirementDao;

    @Scheduled(cron = "0 30 3 * * ?")
    public void run() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(7);
        log.info("[MatchExpireJob] 软清理 matched_at < {} 的匹配记录", cutoff);

        // reservation: 拿全部 CONFIRMED, 在 Java 中按 matched_at 过滤
        TutorReservationExample resEx = new TutorReservationExample();
        resEx.createCriteria().andResStatusEqualTo(ReservationStatusEnum.CONFIRMED.getCode());
        List<TutorReservation> resList = tutorReservationDao.page(1, 1000, resEx).getList();
        int resTouched = 0;
        for (TutorReservation r : resList) {
            if (r.getMatchedAt() != null && r.getMatchedAt().isBefore(cutoff)) {
                TutorReservation u = new TutorReservation();
                u.setId(r.getId());
                u.setResStatus(ReservationStatusEnum.COMPLETED.getCode());
                tutorReservationDao.updateById(u);
                resTouched++;
            }
        }
        log.info("[MatchExpireJob] reservation 软清理 {} 条", resTouched);

        // requirement: 拿全部 MATCHED, 同上
        TutorRequirementExample reqEx = new TutorRequirementExample();
        reqEx.createCriteria().andReqStatusEqualTo(RequirementStatusEnum.MATCHED.getCode());
        List<TutorRequirement> reqList = tutorRequirementDao.page(1, 1000, reqEx).getList();
        int reqTouched = 0;
        for (TutorRequirement r : reqList) {
            if (r.getMatchedAt() != null && r.getMatchedAt().isBefore(cutoff)) {
                TutorRequirement u = new TutorRequirement();
                u.setId(r.getId());
                u.setReqStatus(RequirementStatusEnum.CLOSED.getCode());
                tutorRequirementDao.updateById(u);
                reqTouched++;
            }
        }
        log.info("[MatchExpireJob] requirement 软清理 {} 条", reqTouched);
    }
}
