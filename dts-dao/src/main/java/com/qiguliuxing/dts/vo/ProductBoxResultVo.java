package com.qiguliuxing.dts.vo;

import java.math.BigDecimal;

public class ProductBoxResultVo {
    /**
     *  箱子ID
      */
    private Integer boxId;

    /**
     * 箱子编号
     */
    private String boxNumber;

    /**
     * 产品ID
     */
    private Integer productId;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 产品数量
     */
    private Integer quantity;

    /**
     * 已售数量
     */
    private Integer soldQuantity;

    /**
     * 产品图片
     */
    private String productImage;

    /**
     * 产品价格
     */
    private BigDecimal productPrice;

    /**
     * 产品等级（A赏，B赏，终赏）
     */
    private String levelName;

    /**
     * 系列参考价格
     */
    private BigDecimal seriesPrice;

    /**
     * 是否已售
     */
    private boolean isSoldOut;

    public Integer getBoxId() {
        return boxId;
    }

    public void setBoxId(Integer boxId) {
        this.boxId = boxId;
    }

    public String getBoxNumber() {
        return boxNumber;
    }

    public void setBoxNumber(String boxNumber) {
        this.boxNumber = boxNumber;
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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getSoldQuantity() {
        return soldQuantity;
    }

    public void setSoldQuantity(Integer soldQuantity) {
        this.soldQuantity = soldQuantity;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public BigDecimal getSeriesPrice() {
        return seriesPrice;
    }

    public void setSeriesPrice(BigDecimal seriesPrice) {
        this.seriesPrice = seriesPrice;
    }

    public boolean isSoldOut() {
        return isSoldOut;
    }

    public void setSoldOut(boolean soldOut) {
        isSoldOut = soldOut;
    }
}
