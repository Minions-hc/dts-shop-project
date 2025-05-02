package com.qiguliuxing.dts.vo;

import java.util.Date;

public class RedemptionCodeVo {
    private String code;             // 兑换码(主键)
    private Integer codeType;        // 兑换码类型(1:优惠券;2:奖品;null:未分配)
    private Boolean available;       // 是否可用
    private Date createTime;         // 创建时间
    private Date updateTime;         // 更新时间

    // 兑换码类型常量
    public static final int TYPE_COUPON = 1;  // 优惠券
    public static final int TYPE_PRIZE = 2;   // 奖品

    // getters and setters
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getCodeType() {
        return codeType;
    }

    public void setCodeType(Integer codeType) {
        this.codeType = codeType;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "RedemptionCodeVo{" +
                "code='" + code + '\'' +
                ", codeType=" + codeType +
                ", available=" + available +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
