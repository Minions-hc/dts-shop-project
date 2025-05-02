package com.qiguliuxing.dts.db.dao;

import com.qiguliuxing.dts.vo.PointsTransactionVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface PointsTransactionMapper {

    void insertPointsTransaction(PointsTransactionVO pointsTransaction);

    Integer findLatestBalanceByUserId(String userId);

    /**
     * 查询用户最后一天签到时间
     * @param userId 用户ID
     * @return 最后一天签到时间
     */
    Date findUserLatestCheckInDay(String userId);

    /**
     * 查询用户所有积分变动记录
     * @param userId 用户ID
     * @return 积分流水列表
     */
    List<PointsTransactionVO> selectByUserId(@Param("userId") String userId);
}
