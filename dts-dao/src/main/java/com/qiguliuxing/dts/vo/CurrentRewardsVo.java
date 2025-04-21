package com.qiguliuxing.dts.vo;

import java.util.Date;

public class CurrentRewardsVo {

    private String userId;       // 微信用户ID
    private String month;        // 所属月份(格式:YYYY-MM)
    private Integer aCount;      // A赏数量
    private Integer bCount;      // B赏数量
    private Integer totalScore;  // 当前总积分(A赏10分+B赏1分)
    private Date createdAt;  // 记录创建时间
    private Date updatedAt;  // 记录更新时间


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Integer getaCount() {
        return aCount;
    }

    public void setaCount(Integer aCount) {
        this.aCount = aCount;
    }

    public Integer getbCount() {
        return bCount;
    }

    public void setbCount(Integer bCount) {
        this.bCount = bCount;
    }

    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
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
