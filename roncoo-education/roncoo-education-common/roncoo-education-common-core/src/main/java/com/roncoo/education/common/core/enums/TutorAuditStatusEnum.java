package com.roncoo.education.common.core.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum TutorAuditStatusEnum {

    DRAFT(0, "草稿"),

    PENDING(1, "待审核"),

    APPROVED(2, "已通过"),

    REJECTED(3, "已拒绝"),

    PUBLISHED(4, "已发布");

    private final Integer code;

    private final String desc;

    public static TutorAuditStatusEnum byCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (TutorAuditStatusEnum e : TutorAuditStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
