package com.qiguliuxing.dts.vo;

import java.util.Date;

public class UserDrawCodeVo {
    private String code;            // 抽奖码
    private Date createTime;       // 获得时间
    private String status;         // 状态：PENDING/NOT_WIN/WIN
    private String activityName;   // 活动名称
    private Integer periodNumber;  // 活动期数
    private String source;         // 获取途径：INITIAL/HELP
    private String helperAvatar;   // 助力人头像
    private String helperUserId;   // 助力人ID

    // getters and setters
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Integer getPeriodNumber() {
        return periodNumber;
    }

    public void setPeriodNumber(Integer periodNumber) {
        this.periodNumber = periodNumber;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getHelperAvatar() {
        return helperAvatar;
    }

    public void setHelperAvatar(String helperAvatar) {
        this.helperAvatar = helperAvatar;
    }

    public String getHelperUserId() {
        return helperUserId;
    }

    public void setHelperUserId(String helperUserId) {
        this.helperUserId = helperUserId;
    }
}
