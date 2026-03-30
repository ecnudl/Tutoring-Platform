package com.roncoo.education.common.core.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum FeedbackStatusEnum {

    PENDING(0, "待处理"),

    REPLIED(1, "已回复"),

    CLOSED(2, "已关闭");

    private final Integer code;

    private final String desc;

    public static FeedbackStatusEnum byCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (FeedbackStatusEnum e : FeedbackStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
