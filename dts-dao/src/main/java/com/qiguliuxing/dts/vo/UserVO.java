package com.qiguliuxing.dts.vo;

import java.util.Date;

public class UserVO {

    private String userId;          // 用户ID（微信ID）
    private String wxOpenId;
    private String nickName;
    private String userName;        // 用户名称
    private String avatar;          // 头像URL
    private Integer points;         // 积分
    private Integer productSpiritPower; // 魂力值
    private String inviterId;       // 上级邀请人ID
    private String inviteCode;
    private String phone;           // 手机号码
    private Double redPacketBalance; // 红包余额
    private String createBy;        // 创建人
    private Date createTime;        // 创建时间
    private String updateBy;        // 修改人
    private Date updateTime;        // 修改时间

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getInviterId() {
        return inviterId;
    }

    public void setInviterId(String inviterId) {
        this.inviterId = inviterId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getRedPacketBalance() {
        return redPacketBalance;
    }

    public void setRedPacketBalance(Double redPacketBalance) {
        this.redPacketBalance = redPacketBalance;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getWxOpenId() {
        return wxOpenId;
    }

    public void setWxOpenId(String wxOpenId) {
        this.wxOpenId = wxOpenId;
    }

    public Integer getProductSpiritPower() {
        return productSpiritPower;
    }

    public void setProductSpiritPower(Integer productSpiritPower) {
        this.productSpiritPower = productSpiritPower;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }
}
