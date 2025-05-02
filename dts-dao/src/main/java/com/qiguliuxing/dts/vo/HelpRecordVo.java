package com.qiguliuxing.dts.vo;

import java.util.Date;

public class HelpRecordVo {
    private Date helpTime;       // 助力时间
    private String helperId;     // 助力人ID
    private String helperName;   // 助力人昵称
    private String helperAvatar; // 助力人头像
    private String activityName; // 活动名称（保留用于展示）

    // getters and setters
    public Date getHelpTime() {
        return helpTime;
    }

    public void setHelpTime(Date helpTime) {
        this.helpTime = helpTime;
    }

    public String getHelperId() {
        return helperId;
    }

    public void setHelperId(String helperId) {
        this.helperId = helperId;
    }

    public String getHelperName() {
        return helperName;
    }

    public void setHelperName(String helperName) {
        this.helperName = helperName;
    }

    public String getHelperAvatar() {
        return helperAvatar;
    }

    public void setHelperAvatar(String helperAvatar) {
        this.helperAvatar = helperAvatar;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }
}
