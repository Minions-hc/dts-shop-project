package com.qiguliuxing.dts.db.dao;

import com.qiguliuxing.dts.vo.RewardRulesVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RewardRulesMapper {

    int insert(RewardRulesVo rewardRulesVo);

    int updateById(RewardRulesVo rewardRulesVo);
    int deleteById(@Param("id") Integer id);
    RewardRulesVo selectById(@Param("id") Integer id);
    List<RewardRulesVo> selectAll();
    RewardRulesVo selectByRankOrder(@Param("rankOrder") Integer rankOrder);
}
