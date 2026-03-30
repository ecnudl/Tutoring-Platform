package com.roncoo.education.common.core.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum CertTypeEnum {

    ID_CARD(1, "身份证"),

    STUDENT_CARD(2, "学生证"),

    TEACHER_CERT(3, "教师资格证"),

    DEGREE_CERT(4, "学历证"),

    OTHER(5, "其他");

    private final Integer code;

    private final String desc;

    public static CertTypeEnum byCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (CertTypeEnum e : CertTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
