package com.qiguliuxing.dts.vo;
import com.alibaba.fastjson.JSON;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ProductSeriesVO {

    private Integer categoryId; // 类目ID
    private String categoryName;
    private Integer seriesId; // 系列ID
    private String seriesName; // 系列名称
    private String seriesImage; // 系列主图
    private String seriesDescription; // 系列描述
    private Boolean isHot; // 是否热榜
    private Boolean isAvoid; // 是否踩雷
    private Boolean isPopularNew;
    private Boolean isHotRecommend;
    private Integer purchaseCount; // 购买次数
    private Double price;
    private String createdBy; // 创建人
    private Date createdTime; // 创建时间
    private String updatedBy; // 更新人
    private Date updatedTime; // 更新时间

    // Getters and Setters
    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(Integer seriesId) {
        this.seriesId = seriesId;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public String getSeriesImage() {
        return seriesImage;
    }

    public void setSeriesImage(String seriesImage) {
        this.seriesImage = seriesImage;
    }

    public String getSeriesDescription() {
        return seriesDescription;
    }

    public void setSeriesDescription(String seriesDescription) {
        this.seriesDescription = seriesDescription;
    }

    public Boolean getIsHot() {
        return isHot;
    }

    public void setIsHot(Boolean isHot) {
        this.isHot = isHot;
    }

    public Boolean getIsAvoid() {
        return isAvoid;
    }

    public void setIsAvoid(Boolean isAvoid) {
        this.isAvoid = isAvoid;
    }

    public Integer getPurchaseCount() {
        return purchaseCount;
    }

    public void setPurchaseCount(Integer purchaseCount) {
        this.purchaseCount = purchaseCount;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Boolean getIsPopularNew() {
        return isPopularNew;
    }

    public void setIsPopularNew(Boolean popularNew) {
        isPopularNew = popularNew;
    }

    public Boolean getIsHotRecommend() {
        return isHotRecommend;
    }

    public void setIsHotRecommend(Boolean hotRecommend) {
        isHotRecommend = hotRecommend;
    }

    private String priceRanges; // 改为String类型存储JSON字符串

    public String getPriceRanges() {
        return priceRanges;
    }

    public void setPriceRanges(String priceRanges) {
        this.priceRanges = priceRanges;
    }
}
