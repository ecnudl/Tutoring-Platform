package com.roncoo.education.common.core.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum DegreeEnum {

    UNSET(0, "未设置"),

    HIGH_SCHOOL(1, "高中"),

    COLLEGE(2, "大专"),

    BACHELOR(3, "本科"),

    MASTER(4, "硕士"),

    DOCTOR(5, "博士");

    private final Integer code;

    private final String desc;

    public static DegreeEnum byCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (DegreeEnum e : DegreeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
