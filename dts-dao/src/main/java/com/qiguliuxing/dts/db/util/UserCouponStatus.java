package com.qiguliuxing.dts.db.util;

public enum UserCouponStatus {

    UN_USEED(1, "未使用"),
    USEED(2, "已使用"),
    EXPIRED(3, "已过期");

    private final int code;
    private final String description;

    UserCouponStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
