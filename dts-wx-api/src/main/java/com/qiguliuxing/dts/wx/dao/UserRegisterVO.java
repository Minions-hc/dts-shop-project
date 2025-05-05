package com.qiguliuxing.dts.wx.dao;


public class UserRegisterVO {

    /**
     * 用户ID（微信ID）
     */
    private String wxOpenId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 邀请人
     */
    private String inviteId;

    /**
     * 手机号码
     */
    private String phone;



    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getInviteId() {
        return inviteId;
    }

    public void setInviteId(String inviteId) {
        this.inviteId = inviteId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWxOpenId() {
        return wxOpenId;
    }

    public void setWxOpenId(String wxOpenId) {
        this.wxOpenId = wxOpenId;
    }
}
