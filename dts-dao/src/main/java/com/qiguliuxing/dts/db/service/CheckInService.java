package com.qiguliuxing.dts.db.service;

import com.qiguliuxing.dts.db.dao.UserCheckInMapper;
import com.qiguliuxing.dts.db.util.PointsTransactionType;
import com.qiguliuxing.dts.vo.UserCheckInVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
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

    public Boolean userConsumptionInFirday(String userId) {
        UserCheckInVO userCheckIn = userCheckInMapper.findUserCheckInByUserId(userId);
        if (userCheckIn == null) {
            return false;
        }
        Calendar current = Calendar.getInstance();
        Calendar old = Calendar.getInstance();
        old.setTime(userCheckIn.getCheckInDate());

        // 清除时间部分（仅比较日期）
        clearTime(current);
        clearTime(old);

        // 计算毫秒差并转换为天数
        long diffMillis = current.getTimeInMillis() - old.getTimeInMillis();
        long daysBetween = diffMillis / (24 * 60 * 60 * 1000);

        // 判断是否超过5天
        return  daysBetween > 5;
    }

    private static void clearTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }
}
