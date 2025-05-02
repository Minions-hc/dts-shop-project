package com.qiguliuxing.dts.db.util;

/**
 * 优惠券状态枚举
 */
public enum CouponStatus {
    VALID(1, "有效"),
    INVALID(0, "无效"),
    EXPIRED(2, "过期");

    private final int code;
    private final String description;

    CouponStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 根据code获取枚举值
     */
    public static CouponStatus getByCode(int code) {
        for (CouponStatus status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        return null;
    }

    /**
     * 判断是否是有效状态
     */
    public static boolean isValid(int code) {
        return code == VALID.code;
    }

    /**
     * 判断是否是无效状态
     */
    public static boolean isInvalid(int code) {
        return code == INVALID.code;
    }

    /**
     * 判断是否是过期状态
     */
    public static boolean isExpired(int code) {
        return code == EXPIRED.code;
    }
}
