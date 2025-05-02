package com.qiguliuxing.dts.db.util;

/**
 * 状态枚举
 * - PENDING: 待处理
 * - LOCKED: 锁定中
 * - SHIPPED: 已提货
 */
public enum StatusType {
    PENDING("pending", "待处理", 1),
    LOCKED("locked", "锁定中", 2),
    SHIPPED("shipped", "已提货", 3);

    private final String code;    // 英文代码
    private final String name;   // 中文名称
    private final int value;     // 数值值

    StatusType(String code, String name, int value) {
        this.code = code;
        this.name = name;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    /**
     * 根据code获取枚举
     */
    public static StatusType fromCode(String code) {
        for (StatusType status : StatusType.values()) {
            if (status.getCode().equalsIgnoreCase(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("无效的状态code: " + code);
    }

    /**
     * 根据value获取枚举
     */
    public static StatusType fromValue(int value) {
        for (StatusType status : StatusType.values()) {
            if (status.getValue() == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("无效的状态value: " + value);
    }

    /**
     * 检查code是否有效
     */
    public static boolean isValidCode(String code) {
        for (StatusType status : StatusType.values()) {
            if (status.getCode().equalsIgnoreCase(code)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查value是否有效
     */
    public static boolean isValidValue(int value) {
        for (StatusType status : StatusType.values()) {
            if (status.getValue() == value) {
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
