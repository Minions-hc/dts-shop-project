package com.qiguliuxing.dts.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * 盲盒抽取请求VO
 */
public class BlindBoxDrawRequestVO {

    private String userId;
    private List<Integer> numbers; // 抽取的编号数组
    private String boxNumber;
    private Integer seriesId;
    private Integer spiritPower;

    /**
     * 盲盒类型（踩雷赏/一番赏/魂力赏）
     */
    private String activityType;

    /**
     * 积分减免金额
     */
    private BigDecimal pointDeduction;

    /**
     * 优惠券减免金额
     */
    private BigDecimal couponDeduction;

    /**
     * 订单金额
     */
    private BigDecimal orderAmount;

    /**
     * 实际支付金额
     */
    private BigDecimal paymentAmount;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Integer> getNumbers() {
        return numbers;
    }

    public void setNumbers(List<Integer> numbers) {
        this.numbers = numbers;
    }

    public String getBoxNumber() {
        return boxNumber;
    }

    public void setBoxNumber(String boxNumber) {
        this.boxNumber = boxNumber;
    }

    public Integer getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(Integer seriesId) {
        this.seriesId = seriesId;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
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

    public Integer getSpiritPower() {
        return spiritPower;
    }

    public void setSpiritPower(Integer spiritPower) {
        this.spiritPower = spiritPower;
    }
}
