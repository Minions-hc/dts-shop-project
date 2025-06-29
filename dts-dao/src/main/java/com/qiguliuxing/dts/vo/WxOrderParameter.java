package com.qiguliuxing.dts.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 微信支付订单参数实体类
 */
public class WxOrderParameter {
    private Long id;
    private String userId;          // 用户ID
    private List<Integer> numbers;  // 抽取的编号数组
    private String boxNumber;       // 盲盒编号
    private Integer seriesId;       // 系列ID
    private Integer spiritPower;    // 魂力值
    private String activityType;    // 盲盒类型（踩雷赏/一番赏/魂力赏）
    private BigDecimal pointDeduction; // 积分减免金额
    private BigDecimal couponDeduction; // 优惠券减免金额
    private BigDecimal orderAmount; // 订单金额
    private BigDecimal paymentAmount; // 实际支付金额
    private List<Integer> ids;      // 提货ID列表
    private Integer businessType;   // 业务类型（1，抽赏；2，提货）
    private String outTradeNo;      // 商户订单号
    private String wxOrderNo;      // 微信订单号
    private Date createTime;        // 创建时间
    private Date updateTime;        // 更新时间
    private Integer point;          // 积分
    private Integer couponId;       // 优惠券ID
    private Boolean isLock;       // 是否锁箱

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Integer getSpiritPower() {
        return spiritPower;
    }

    public void setSpiritPower(Integer spiritPower) {
        this.spiritPower = spiritPower;
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

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

    public Integer getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
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

    public String getWxOrderNo() {
        return wxOrderNo;
    }

    public void setWxOrderNo(String wxOrderNo) {
        this.wxOrderNo = wxOrderNo;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public Integer getCouponId() {
        return couponId;
    }

    public void setCouponId(Integer couponId) {
        this.couponId = couponId;
    }

    public Boolean getLock() {
        return isLock;
    }

    public void setLock(Boolean lock) {
        isLock = lock;
    }
}
