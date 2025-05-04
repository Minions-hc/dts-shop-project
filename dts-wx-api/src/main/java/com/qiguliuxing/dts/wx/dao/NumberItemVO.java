package com.qiguliuxing.dts.wx.dao;

public class NumberItemVO {

    /**
     * 编号
     */
    private Integer number;

    /**
     * 产品图片
     */
    private String productImage;

    /**
     * 是否已售完
     */
    private Boolean soldOut;

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public Boolean getSoldOut() {
        return soldOut;
    }

    public void setSoldOut(Boolean soldOut) {
        this.soldOut = soldOut;
    }
}
