package com.qiguliuxing.dts.db.util;

/**
 * 兑换码类型枚举
 */
public enum RedemptionCodeType {
    COUPON(1, "优惠券"),
    PRIZE(2, "奖品"),
    UNASSIGNED(0, "未分配");

    private final Integer value;
    private final String description;

    RedemptionCodeType(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    public Integer getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 根据值获取枚举
     */
    public static RedemptionCodeType fromValue(Integer value) {
        for (RedemptionCodeType type : RedemptionCodeType.values()) {
            if (type.value == null && value == null) {
                return type;
            }
            if (type.value != null && type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("无效的兑换码类型值: " + value);
    }

    /**
     * 检查值是否有效
     */
    public static boolean isValid(Integer value) {
        for (RedemptionCodeType type : RedemptionCodeType.values()) {
            if (type.value == null && value == null) {
                return true;
            }
            if (type.value != null && type.value.equals(value)) {
                return true;
            }
        }
        return false;
    }
}
