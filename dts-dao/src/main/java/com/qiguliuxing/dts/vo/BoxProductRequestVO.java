package com.qiguliuxing.dts.vo;

import java.util.List;

public class BoxProductRequestVO {

    private String userId;

    private Integer productId;

    private List<Integer> ids;

    private Integer totalProductBadge;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }


    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

    public Integer getTotalProductBadge() {
        return totalProductBadge;
    }

    public void setTotalProductBadge(Integer totalProductBadge) {
        this.totalProductBadge = totalProductBadge;
    }
}
