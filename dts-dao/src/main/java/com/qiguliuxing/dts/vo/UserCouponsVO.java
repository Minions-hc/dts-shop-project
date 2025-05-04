package com.qiguliuxing.dts.vo;

import java.math.BigDecimal;
import java.util.Date;

public class UserCouponsVO {
    private Integer couponId;
    private String userId;
    private String couponName;
    private BigDecimal couponAmount;
    private Date expireTime;
    private Integer status;
    private String orderId;
    private Date useTime;
    private Integer couponType; // 1-无门槛券, 2-满减券
    private BigDecimal minOrderAmount; // 仅满减券有效
    private Date createTime;
    private Date updateTime;

    // Getters and Setters
    public Integer getCouponId() {
        return couponId;
    }

    public void setCouponId(Integer couponId) {
        this.couponId = couponId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public BigDecimal getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(BigDecimal couponAmount) {
        this.couponAmount = couponAmount;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Date getUseTime() {
        return useTime;
    }

    public void setUseTime(Date useTime) {
        this.useTime = useTime;
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

    // toString()方法（可选）
    @Override
    public String toString() {
        return "UserCouponsVO{" +
                "couponId=" + couponId +
                ", userId='" + userId + '\'' +
                ", couponName='" + couponName + '\'' +
                ", couponAmount=" + couponAmount +
                ", expireTime=" + expireTime +
                ", status=" + status +
                ", orderId='" + orderId + '\'' +
                ", useTime=" + useTime +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }

    public Integer getCouponType() {
        return couponType;
    }

    public void setCouponType(Integer couponType) {
        this.couponType = couponType;
    }

    public BigDecimal getMinOrderAmount() {
        return minOrderAmount;
    }

    public void setMinOrderAmount(BigDecimal minOrderAmount) {
        this.minOrderAmount = minOrderAmount;
    }
}
