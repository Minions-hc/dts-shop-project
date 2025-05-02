package com.qiguliuxing.dts.db.util;

/**
 * 积分交易类型枚举
 */
public enum PointsTransactionType {
    SIGN_IN_REWARD(1, "签到奖励"),
    ORDER_DEDUCTION(2, "订单抵扣"),
    ORDER_CLOSED(3, "订单关闭"),
    LUCKY_LIST(4, "欧皇榜");

    private final Integer code;
    private final String description;

    PointsTransactionType(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static PointsTransactionType fromCode(Integer code) {
        for (PointsTransactionType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("无效的交易类型代码: " + code);
    }
}
