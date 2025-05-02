package com.qiguliuxing.dts.db.service;

import com.qiguliuxing.dts.db.dao.LuckyKingRankMapper;
import com.qiguliuxing.dts.vo.LuckyKingRankVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class LuckyKingRankService {

    @Resource
    private LuckyKingRankMapper luckyKingRankMapper;

    public List<LuckyKingRankVO> getCurrentMonthTop10() {
        List<LuckyKingRankVO> list = luckyKingRankMapper.selectLuckyKingTop10();
        // 添加排名
        IntStream.range(0, list.size())
                .forEach(i -> list.get(i).setRank(i + 1));
        return list;
    }

    /**
     * 获取当月全体用户欧皇榜总积分
     * @return 所有用户的总积分和
     */
    public Integer getTotalLuckyKingPoints() {
        return luckyKingRankMapper.selectTotalLuckyKingPoints();
    }

}
