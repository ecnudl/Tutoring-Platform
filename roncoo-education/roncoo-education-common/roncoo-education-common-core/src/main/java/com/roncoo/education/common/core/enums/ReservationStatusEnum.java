package com.roncoo.education.common.core.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ReservationStatusEnum {

    PENDING(0, "待确认"),

    CONFIRMED(1, "已确认"),

    COMPLETED(2, "已完成"),

    CANCELLED(3, "已取消");

    private final Integer code;

    private final String desc;

    public static ReservationStatusEnum byCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (ReservationStatusEnum e : ReservationStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
