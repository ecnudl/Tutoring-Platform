package com.roncoo.education.common.core.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ApplicationStatusEnum {

    APPLIED(0, "已申请"),

    SHORTLISTED(1, "入围"),

    ACCEPTED(2, "录用"),

    REJECTED(3, "拒绝");

    private final Integer code;

    private final String desc;

    public static ApplicationStatusEnum byCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (ApplicationStatusEnum e : ApplicationStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
