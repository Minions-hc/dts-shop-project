package com.qiguliuxing.dts.vo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class OrderDetailVO {

    private Integer orderId;

    private String orderNo;

    private String userId;

    private String userName;

    // 收货信息
    // 收货人姓名
    private String receiverName;

    // 收货人电话
    private String receiverPhone;

    // 详细地址
    private String fullAddress;

    // 快递信息
    // 快递渠道
    private String shippingChannel;

    // 快递单号
    private String trackingNumber;

    // 订单状态
    private String orderStatus;

    // 金额信息
    // 订单金额
    private Double orderAmount;

    // 支付金额
    private Double paymentAmount;

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

    // 支付时间
    private Date paymentTime;

    private Date deliveryTime;

    // 商品列表
    private List<OrderItemVO> orderItems;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
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

    public List<OrderItemVO> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemVO> orderItems) {
        this.orderItems = orderItems;
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
