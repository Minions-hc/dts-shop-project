package com.qiguliuxing.dts.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 盒柜订单支付信息VO
 */
public class BoxOrderVO {

    private Integer id;
    private Integer recordId; // 关联盒柜商品ID
    private Integer orderId; // 业务订单ID
    private BigDecimal pointDeduction; // 积分减免金额
    private BigDecimal couponDeduction; // 优惠券减免金额
    private BigDecimal orderAmount; // 订单金额
    private BigDecimal paymentAmount; // 实际支付金额
    private Date paymentTime; // 支付时间
    private BigDecimal shippingFee; // 快递费用
    private Date createdTime; // 创建时间
    private Date updatedTime; // 更新时间

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getPointDeduction() {
        return pointDeduction;
    }

    public void setPointDeduction(BigDecimal pointDeduction) {
        this.pointDeduction = pointDeduction;
    }

    public BigDecimal getCouponDeduction() {
        return couponDeduction;
    }

    public void setCouponDeduction(BigDecimal couponDeduction) {
        this.couponDeduction = couponDeduction;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    public BigDecimal getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(BigDecimal shippingFee) {
        this.shippingFee = shippingFee;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }
}
