package com.qiguliuxing.dts.vo;

import java.util.Date;

/**
 *  用户签到记录实体类
 */
public class UserCheckInVO {

    private String userId;
    private Date checkInDate;
    private Integer checkInDay;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public Integer getCheckInDay() {
        return checkInDay;
    }

    public void setCheckInDay(Integer checkInDay) {
        this.checkInDay = checkInDay;
    }
}
