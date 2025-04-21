package com.qiguliuxing.dts.vo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class OrderVO {


    private Long orderId;           // 订单ID
    private String userId;
    private String userName;// 用户ID
    private String orderNo;         // 订单编号
    private Long addressId;         // 收货地址ID
    private Double orderAmount;     // 订单金额
    private Double paymentAmount;   // 支付金额
    private Date paymentTime;      // 支付时间
    private Date deliveryTime;
    private String shippingChannel; // 物流渠道
    private String trackingNumber;  // 物流单号
    private String orderStatus;    // 订单状态（1.系统取消，2.待付款，3.待发货，4.待收货，5.已完成）
    private String createBy;        // 创建人
    private Date createTime;        // 创建时间
    private String updateBy;        // 修改人
    private Date updateTime;

    /**
     * 快递费用
     */
    private Double shippingFee;

    /**
     * 优惠减免
     */
    private Double discountAmount;

    /**
     * 积分减免
     */
    private Double pointsDeduction;

    // 非数据库字段
    private List<OrderItemVO> items;  // 订单商品列表


    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Double orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPaymentTimeStr() {
        if (this.paymentTime == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(this.paymentTime);
    }

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    public String getShippingChannel() {
        return shippingChannel;
    }

    public void setShippingChannel(String shippingChannel) {
        this.shippingChannel = shippingChannel;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Double getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(Double shippingFee) {
        this.shippingFee = shippingFee;
    }

    public Double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Double getPointsDeduction() {
        return pointsDeduction;
    }

    public void setPointsDeduction(Double pointsDeduction) {
        this.pointsDeduction = pointsDeduction;
    }

    public String getDeliveryTimeStr() {
        if (this.deliveryTime == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(this.deliveryTime);
    }

    public Date getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }
}
