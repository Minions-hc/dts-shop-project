package com.qiguliuxing.dts.db.service;

import com.qiguliuxing.dts.db.dao.PointsTransactionMapper;
import com.qiguliuxing.dts.vo.PointsTransactionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class PointsTransactionService {

    @Autowired
    private PointsTransactionMapper pointsTransactionMapper;

    @Transactional
    public void insertPointsTransaction(String userId, Integer points, Integer transactionType, String relatedId) {
        Integer latestBalance = pointsTransactionMapper.findLatestBalanceByUserId(userId);
        PointsTransactionVO pointsTransactionVO = new PointsTransactionVO();
        pointsTransactionVO.setUserId(userId);
        pointsTransactionVO.setTransactionType(transactionType);
        pointsTransactionVO.setPointsChange(points);
        pointsTransactionVO.setCreatedAt(new Date());
        pointsTransactionVO.setRelatedId(relatedId);
        pointsTransactionVO.setBalanceAfter(latestBalance + points);
        pointsTransactionMapper.insertPointsTransaction(pointsTransactionVO);
    }

}
