package com.qiguliuxing.dts.vo;

import java.util.Date;

/**
 * 盲盒开赏记录VO
 */
public class BlindBoxRecordVO {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 盲盒编号
     */
    private Integer number;

    /**
     * 产品ID
     */
    private Integer productId;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 产品图片
     */
    private String productImage;

    /**
     * 产品等级名称
     */
    private String levelName;

    /**
     * 创建时间(开赏时间)
     */
    private Date createdAt;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
