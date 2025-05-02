package com.qiguliuxing.dts.db.service;

import com.qiguliuxing.dts.db.dao.UserCheckInMapper;
import com.qiguliuxing.dts.db.util.PointsTransactionType;
import com.qiguliuxing.dts.vo.UserCheckInVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class CheckInService {

    @Autowired
    private UserCheckInMapper userCheckInMapper;

    @Autowired
    private PointsTransactionService pointsTransactionService;

    @Transactional
    public void checkIn(String userId, Integer checkInDay, Integer points){
        //Todo 校验是否5天内消费
        UserCheckInVO userCheckIn = userCheckInMapper.findUserCheckInByUserId(userId);
        if( userCheckIn != null ) {
            userCheckIn.setCheckInDay(checkInDay);
            userCheckIn.setCheckInDate(new Date());
            userCheckInMapper.updateUserCheckIn(userCheckIn);
        } else {
            UserCheckInVO newUserCheckIn = new UserCheckInVO();
            newUserCheckIn.setUserId(userId);
            newUserCheckIn.setCheckInDay(checkInDay);
            newUserCheckIn.setCheckInDate(new Date());
            userCheckInMapper.insertUserCheckIn(newUserCheckIn);
        }
        pointsTransactionService.insertPointsTransaction(userId, points, PointsTransactionType.SIGN_IN_REWARD.getCode(), null);
    }


    public Integer currentCheckInDay(String userId) {
        UserCheckInVO userCheckIn = userCheckInMapper.findUserCheckInByUserId(userId);
        if (userCheckIn == null) {
            return 0;
        }
        return userCheckIn.getCheckInDay();
    }
}
