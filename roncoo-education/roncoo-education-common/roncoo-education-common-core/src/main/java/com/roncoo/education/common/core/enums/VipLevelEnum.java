package com.roncoo.education.common.core.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum VipLevelEnum {

    NORMAL(0, "普通用户"),

    VIP(1, "VIP会员"),

    SVIP(2, "SVIP会员");

    private final Integer code;

    private final String desc;

    public static VipLevelEnum byCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (VipLevelEnum e : VipLevelEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
