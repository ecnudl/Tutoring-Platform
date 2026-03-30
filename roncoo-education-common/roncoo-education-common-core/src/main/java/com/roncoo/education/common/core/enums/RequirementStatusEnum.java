package com.roncoo.education.common.core.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum RequirementStatusEnum {

    DRAFT(0, "草稿"),

    PENDING(1, "待审核"),

    PUBLISHED(2, "已发布"),

    MATCHED(3, "已匹配"),

    COMPLETED(4, "已完成"),

    CLOSED(5, "已关闭"),

    REJECTED(6, "审核驳回");

    private final Integer code;

    private final String desc;

    public static RequirementStatusEnum byCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (RequirementStatusEnum e : RequirementStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
