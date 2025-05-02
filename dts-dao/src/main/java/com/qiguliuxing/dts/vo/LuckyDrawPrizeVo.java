package com.qiguliuxing.dts.vo;

import java.util.Date;

/**
 * 幸运大抽奖奖品VO
 */
public class LuckyDrawPrizeVo {
    private Integer prizeId;         // 奖品ID
    private Integer productId;
    private String productName;     // 产品名称
    private Integer productQuantity; // 产品数量
    private Integer activityId;     // 关联的活动ID
    private String activityName;
    private String redemptionCode;
    private Date createdAt;         // 创建时间
    private Date updatedAt;         // 更新时间

    // 构造方法
    public LuckyDrawPrizeVo() {
    }

    // getters and setters
    public Integer getPrizeId() {
        return prizeId;
    }

    public void setPrizeId(Integer prizeId) {
        this.prizeId = prizeId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(Integer productQuantity) {
        this.productQuantity = productQuantity;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getRedemptionCode() {
        return redemptionCode;
    }

    public void setRedemptionCode(String redemptionCode) {
        this.redemptionCode = redemptionCode;
    }
}
