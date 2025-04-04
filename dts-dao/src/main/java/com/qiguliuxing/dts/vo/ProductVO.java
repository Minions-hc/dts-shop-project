package com.qiguliuxing.dts.vo;
import java.util.Date;

public class ProductVO {

    private Integer productId; // 产品ID
    private String productName;;
    private String productImage; // 产品图片
    private Double productPrice; // 产品价格
    private Integer quantity;
    private Integer productSeriesId;
    private String productSeriesName;
    private String productLevelName;
    private Integer productLevelId;
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

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getProductLevelId() {
        return productLevelId;
    }

    public void setProductLevelId(Integer productLevelId) {
        this.productLevelId = productLevelId;
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

    public Integer getProductSeriesId() {
        return productSeriesId;
    }

    public void setProductSeriesId(Integer productSeriesId) {
        this.productSeriesId = productSeriesId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
