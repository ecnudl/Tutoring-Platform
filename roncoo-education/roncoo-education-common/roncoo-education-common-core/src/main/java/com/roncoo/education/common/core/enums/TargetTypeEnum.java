package com.roncoo.education.common.core.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum TargetTypeEnum {

    TUTOR(1, "教员"),

    REQUIREMENT(2, "需求");

    private final Integer code;

    private final String desc;

    public static TargetTypeEnum byCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (TargetTypeEnum e : TargetTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
