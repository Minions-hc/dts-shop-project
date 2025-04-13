package com.qiguliuxing.dts.vo;

import java.util.Date;

public class AddressVO {

    private Long addressId;         // 地址ID
    private String userId;          // 用户ID
    private String userName;
    private String receiverName;    // 收货人姓名
    private String phone;           // 手机号码
    private String region;          // 所在地区
    private String detailAddress;   // 详细地址
    private Integer tag;            // 标签（1家，2公司，3学校）
    private Boolean isDefault;      // 是否默认地址
    private Boolean isPickup;       // 是否自提地址
    private String createBy;        // 创建人
    private Date createTime;        // 创建时间
    private String updateBy;        // 修改人
    private Date updateTime;        // 修改时间

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public Integer getTag() {
        return tag;
    }

    public void setTag(Integer tag) {
        this.tag = tag;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public Boolean getPickup() {
        return isPickup;
    }

    public void setPickup(Boolean pickup) {
        isPickup = pickup;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
