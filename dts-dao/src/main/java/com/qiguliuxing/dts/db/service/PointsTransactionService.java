package com.qiguliuxing.dts.db.service;

import com.qiguliuxing.dts.db.dao.PointsTransactionMapper;
import com.qiguliuxing.dts.db.util.PointsTransactionType;
import com.qiguliuxing.dts.vo.PointsTransactionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class PointsTransactionService {

    @Autowired
    private PointsTransactionMapper pointsTransactionMapper;

    @Transactional
    public void insertPointsTransaction(String userId, Integer points, Integer transactionType, String relatedId) {
        Integer latestBalance = pointsTransactionMapper.findLatestBalanceByUserId(userId) == null ? 0 : pointsTransactionMapper.findLatestBalanceByUserId(userId);
        PointsTransactionVO pointsTransactionVO = new PointsTransactionVO();
        pointsTransactionVO.setUserId(userId);
        pointsTransactionVO.setTransactionType(transactionType);
        pointsTransactionVO.setPointsChange(points);
        pointsTransactionVO.setCreatedAt(new Date());
        pointsTransactionVO.setRelatedId(relatedId);
        if (transactionType.equals(PointsTransactionType.ORDER_DEDUCTION.getCode())) {
            pointsTransactionVO.setBalanceAfter(latestBalance - points);
        } else {
            pointsTransactionVO.setBalanceAfter(latestBalance + points);
        }
        pointsTransactionMapper.insertPointsTransaction(pointsTransactionVO);
    }

    /**
     * 获取用户所有积分变动记录
     * @param userId 用户ID
     * @return 积分流水列表
     */
    public List<PointsTransactionVO> getTransactionsByUser(String userId) {
        return pointsTransactionMapper.selectByUserId(userId);
    }


    /**
     *  获取用户当前积分
     * @param userId 用户ID
     * @return 当前积分值
     */
    public Integer getUserCurrentPoints(String userId) {
        return pointsTransactionMapper.findLatestBalanceByUserId(userId);
    }

    /**
     * 查询用户最后一天签到时间
     * @param userId 用户ID
     * @return 最后一天签到时间
     */
    public Date findUserLatestCheckInDay(String userId) {
        return pointsTransactionMapper.findUserLatestCheckInDay(userId);
    }
}
