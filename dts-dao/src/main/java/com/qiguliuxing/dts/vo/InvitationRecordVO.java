package com.qiguliuxing.dts.vo;

import java.util.Date;

public class InvitationRecordVO {

    private String userId;          // 被邀请人ID
    private String avatar;          // 被邀请人头像
    private String inviteCode;      // 被邀请人邀请码
    private Date createTime;       // 被邀请人注册时间

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

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
