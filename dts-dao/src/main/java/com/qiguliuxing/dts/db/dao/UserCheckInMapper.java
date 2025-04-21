package com.qiguliuxing.dts.db.dao;

import com.qiguliuxing.dts.vo.UserCheckInVO;

import java.util.Date;
import java.util.List;

public interface UserCheckInMapper {

    UserCheckInVO findUserCheckInByUserId(String userId);

    void insertUserCheckIn(UserCheckInVO userCheckIn);

    void updateUserCheckIn(UserCheckInVO userCheckIn);

}
