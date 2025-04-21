package com.qiguliuxing.dts.db.dao;

import com.qiguliuxing.dts.vo.LuckyDrawActivityVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LuckyDrawActivityMapper {
    int insert(LuckyDrawActivityVo activity);
    int update(LuckyDrawActivityVo activity);
    int deleteById(@Param("activityId") Integer activityId);
    int setAllActivitiesInactive();
    LuckyDrawActivityVo findById(@Param("activityId") Integer activityId);
    List<LuckyDrawActivityVo> findAll();
    LuckyDrawActivityVo findByPeriodNumber(@Param("periodNumber") Integer periodNumber);
}
