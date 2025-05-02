package com.qiguliuxing.dts.db.domain;

import java.io.Serializable;
import java.util.Date;

public class LuckyDrawActivity implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer activityId;
    private Integer periodNumber;
    private String activityName;
    private String activityRules;
    private Date drawDate;
    private String activityDetails;
    private Date createdAt;
    private Date updatedAt;
    private Boolean isActive;

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

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public String toString() {
        return "LuckyDrawActivity{" +
                "activityId=" + activityId +
                ", periodNumber=" + periodNumber +
                ", activityName='" + activityName + '\'' +
                ", activityRules='" + activityRules + '\'' +
                ", drawDate=" + drawDate +
                ", activityDetails='" + activityDetails + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", isActive=" + isActive +
                '}';
    }
}
