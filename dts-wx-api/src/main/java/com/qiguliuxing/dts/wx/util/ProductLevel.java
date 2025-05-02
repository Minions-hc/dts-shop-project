package com.qiguliuxing.dts.wx.util;

/**
 * 产品级别枚举
 */
public enum ProductLevel {
    A_PRIZE("A赏", "A_PRIZE"),
    B_PRIZE("B赏", "B_PRIZE"),
    FINAL_PRIZE("终赏", "FINAL_PRIZE"),
    OTHER("其他", "OTHER");

    private final String chineseName;
    private final String englishCode;

    ProductLevel(String chineseName, String englishCode) {
        this.chineseName = chineseName;
        this.englishCode = englishCode;
    }

    /**
     * 获取中文名称
     */
    public String getChineseName() {
        return chineseName;
    }

    /**
     * 获取英文代码
     */
    public String getEnglishCode() {
        return englishCode;
    }

    /**
     * 根据中文名称获取枚举
     */
    public static ProductLevel fromChineseName(String chineseName) {
        for (ProductLevel level : values()) {
            if (level.chineseName.equals(chineseName)) {
                return level;
            }
        }
        return OTHER;
    }

    /**
     * 根据英文代码获取枚举
     */
    public static ProductLevel fromEnglishCode(String englishCode) {
        for (ProductLevel level : values()) {
            if (level.englishCode.equals(englishCode)) {
                return level;
            }
        }
        return OTHER;
    }

    @Override
    public String toString() {
        return chineseName;
    }
}
