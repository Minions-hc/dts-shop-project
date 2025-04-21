package com.qiguliuxing.dts.db.dao;

import com.qiguliuxing.dts.vo.PointsTransactionVO;

public interface PointsTransactionMapper {

    void insertPointsTransaction(PointsTransactionVO pointsTransaction);

    Integer findLatestBalanceByUserId(String userId);
}
