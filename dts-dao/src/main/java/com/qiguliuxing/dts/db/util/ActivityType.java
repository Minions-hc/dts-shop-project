package com.qiguliuxing.dts.db.util;

/**
 * 活动类型枚举
 */
public enum ActivityType {
    ICHIBAN_KUJI("一番赏", 1),
    AVOID_BOMB("踩雷赏", 2),
    SOUL_POWER("魂力赏", 3),
    MARKET_EXCHANGE("集市换娃", 4),
    LUCKY_DRAW("幸运大抽奖", 5);

    private final String name;
    private final int value;

    ActivityType(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    /**
     * 根据值获取枚举
     */
    public static ActivityType fromValue(int value) {
        for (ActivityType type : ActivityType.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("无效的活动类型值: " + value);
    }

    /**
     * 根据名称获取枚举
     */
    public static ActivityType fromName(String name) {
        for (ActivityType type : ActivityType.values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        throw new IllegalArgumentException("无效的活动类型名称: " + name);
    }

    /**
     * 检查值是否有效
     */
    public static boolean isValid(int value) {
        for (ActivityType type : ActivityType.values()) {
            if (type.getValue() == value) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
