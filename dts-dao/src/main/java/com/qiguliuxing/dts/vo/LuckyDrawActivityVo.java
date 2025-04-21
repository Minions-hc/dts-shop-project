package com.qiguliuxing.dts.vo;

import java.util.Date;

/**
 * 幸运大抽奖活动VO
 */
public class LuckyDrawActivityVo {
    private Integer activityId;      // 活动ID
    private Integer periodNumber;    // 活动期数
    private String activityName;     // 活动名字
    private String activityRules;    // 活动规则
    private Date drawDate;          // 开奖日期
    private String activityDetails;  // 活动详情图片地址
    private boolean isActive;
    private Date createdAt;          // 创建时间
    private Date updatedAt;          // 更新时间

    // 构造方法
    public LuckyDrawActivityVo() {
    }

    // getters and setters
    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Integer getPeriodNumber() {
        return periodNumber;
    }

    public void setPeriodNumber(Integer periodNumber) {
        this.periodNumber = periodNumber;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityRules() {
        return activityRules;
    }

    public void setActivityRules(String activityRules) {
        this.activityRules = activityRules;
    }

    public Date getDrawDate() {
        return drawDate;
    }

    public void setDrawDate(Date drawDate) {
        this.drawDate = drawDate;
    }

    public String getActivityDetails() {
        return activityDetails;
    }

    public void setActivityDetails(String activityDetails) {
        this.activityDetails = activityDetails;
    }

    public String[] getActivityDetailImages() {
        if (this.activityDetails == null) {
            return new String[0];
        }
        return this.activityDetails.split(";");
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
