package com.qiguliuxing.dts.vo;
import java.util.Date;

public class MarketProductVO {

    private Integer productId; // 产品ID
    private String productName;// 产品名称
    private String productImage; // 产品图片
    private Integer productSeriesId;// 产品系列id
    private String productSeriesName;// 产品系列名称
    private String productLevelName;// 产品级别
    private Integer productLevelId;// 产品级别id
    private String productBadge; // 产品兑换所需勋章（等于价格）
    private String productDetail;// 产品详请用来记录多个图片
    private Boolean available;// 是否有效0失效，1有效
    private String createdBy; // 创建人
    private Date createdTime; // 创建时间
    private String updatedBy; // 更新人
    private Date updatedTime; // 更新时间

    // Getters and Setters

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

    public Integer getProductSeriesId() {
        return productSeriesId;
    }

    public void setProductSeriesId(Integer productSeriesId) {
        this.productSeriesId = productSeriesId;
    }

    public String getProductSeriesName() {
        return productSeriesName;
    }

    public void setProductSeriesName(String productSeriesName) {
        this.productSeriesName = productSeriesName;
    }

    public String getProductLevelName() {
        return productLevelName;
    }

    public void setProductLevelName(String productLevelName) {
        this.productLevelName = productLevelName;
    }

    public Integer getProductLevelId() {
        return productLevelId;
    }

    public void setProductLevelId(Integer productLevelId) {
        this.productLevelId = productLevelId;
    }

    public String getProductBadge() {
        return productBadge;
    }

    public void setProductBadge(String productBadge) {
        this.productBadge = productBadge;
    }

    public String getProductDetail() {
        return productDetail;
    }

    public void setProductDetail(String productDetail) {
        this.productDetail = productDetail;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
}