package com.roncoo.education.common.core.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum UserTypeEnum {

    UNSET(0, "未设置"),

    TUTOR(1, "教员"),

    STUDENT(2, "学员");

    private final Integer code;

    private final String desc;

    public static UserTypeEnum byCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (UserTypeEnum e : UserTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
