package com.qiguliuxing.dts.db.dao;

import com.qiguliuxing.dts.vo.LuckyDrawPrizeVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LuckyDrawPrizeMapper {

    int insert(LuckyDrawPrizeVo prize);
    int update(LuckyDrawPrizeVo prize);
    int deleteById(@Param("prizeId") Integer prizeId);
    int deleteByActivityId(@Param("activityId") Integer activityId);
    List<LuckyDrawPrizeVo> selectByCondition(@Param("activityId") Integer activityId, @Param("activityName") String activityName, @Param("productName") String productName);
    int decreaseQuantity(@Param("prizeId") Integer prizeId, @Param("quantity") Integer quantity);

}
