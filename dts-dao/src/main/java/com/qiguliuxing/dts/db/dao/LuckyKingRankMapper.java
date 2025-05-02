package com.qiguliuxing.dts.db.dao;

import com.qiguliuxing.dts.vo.LuckyKingRankVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LuckyKingRankMapper {
    /**
     * 查询当月欧皇榜TOP10
     */
    List<LuckyKingRankVO> selectLuckyKingTop10();

    /**
     * 查询当月所有用户欧皇榜总积分汇总
     * @return 全体用户的总积分和
     */
    Integer selectTotalLuckyKingPoints();
}
