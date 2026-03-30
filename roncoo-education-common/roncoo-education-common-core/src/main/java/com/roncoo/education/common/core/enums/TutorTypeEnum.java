package com.roncoo.education.common.core.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum TutorTypeEnum {

    COLLEGE_STUDENT(1, "大学生"),

    PROFESSIONAL(2, "专职"),

    SCHOOL_TEACHER(3, "在职教师"),

    RETIRED(4, "退休教师"),

    OTHER(5, "其他");

    private final Integer code;

    private final String desc;

    public static TutorTypeEnum byCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (TutorTypeEnum e : TutorTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
