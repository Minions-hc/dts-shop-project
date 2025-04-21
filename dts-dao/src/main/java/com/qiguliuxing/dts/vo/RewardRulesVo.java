package com.qiguliuxing.dts.vo;

import java.util.Date;

public class RewardRulesVo {
    private Integer id;          // 主键ID
    private String rankName;     // 名次(如第一名、第二名)
    private Integer rankOrder;   // 排名顺序(1,2,3...)
    private Double percentage;   // 奖励百分比(如35.00表示35%)
    private Date createdAt;      // 记录创建时间
    private Date updatedAt;      // 记录更新时间

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRankName() {
        return rankName;
    }

    public void setRankName(String rankName) {
        this.rankName = rankName;
    }

    public Integer getRankOrder() {
        return rankOrder;
    }

    public void setRankOrder(Integer rankOrder) {
        this.rankOrder = rankOrder;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
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
}
