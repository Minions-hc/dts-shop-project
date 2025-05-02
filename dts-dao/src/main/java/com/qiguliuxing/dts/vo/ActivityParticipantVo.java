package com.qiguliuxing.dts.vo;

public class ActivityParticipantVo {
    private String userId;         // 用户ID
    private String avatar;        // 用户头像
    private Integer codeCount;    // 抽奖码总数量

    // getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getCodeCount() {
        return codeCount;
    }

    public void setCodeCount(Integer codeCount) {
        this.codeCount = codeCount;
    }
}
