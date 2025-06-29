package com.qiguliuxing.dts.db.service;

import com.alibaba.druid.util.StringUtils;
import com.qiguliuxing.dts.db.dao.DtsUserMapper;
import com.qiguliuxing.dts.db.dao.LuckyKingRankMapper;
import com.qiguliuxing.dts.vo.LuckyKingRankVO;
import com.qiguliuxing.dts.vo.UserVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class LuckyKingRankService {

    @Resource
    private LuckyKingRankMapper luckyKingRankMapper;

    @Resource
    private DtsUserMapper dtsUserMapper;

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

    /**
     * 获取当月全体用户欧皇榜总积分
     * @return 所有用户的总积分和
     */
    public LuckyKingRankVO getUserLuckyKingPoints(String userId) {
        UserVO user = dtsUserMapper.selectUserById(userId);
        LuckyKingRankVO luckyKingRankVO = luckyKingRankMapper.selectUserLuckyKingPoints(userId);
        List<LuckyKingRankVO> list = luckyKingRankMapper.selectLuckyKingTop10();
        // 当出现没有欧皇榜数据时，直接与前十的差距设为0
        if (list.isEmpty()) {
            if (StringUtils.isEmpty(luckyKingRankVO.getUserName())) {
                luckyKingRankVO.setUserId(user.getUserId());
                luckyKingRankVO.setUserName(user.getUserName());
                luckyKingRankVO.setAvatar(user.getAvatar());
            }
            luckyKingRankVO.setDistancePoints(0);
            Map<String, LuckyKingRankVO> userIdAndLuckyKingRankMap = new HashMap<>();
            userIdAndLuckyKingRankMap.put(userId, luckyKingRankVO);
            return luckyKingRankVO;
        }
        // 添加排名
        IntStream.range(0, list.size()).forEach(i -> list.get(i).setRank(i + 1));
        // 使用用户id构建map
        Map<String, LuckyKingRankVO> userIdAndLuckyKingRankMap = list.stream().collect(Collectors.toMap(LuckyKingRankVO::getUserId, rankVO -> rankVO, (a, b) -> b));

        // 如果前十包含当前用户，则处理查询距离前一名的差距
        if (userIdAndLuckyKingRankMap.containsKey(userId)) {
            for (int i = 0; i < list.size(); i++) {
                LuckyKingRankVO temp = list.get(i);
                if (Objects.equals(temp.getUserId(), luckyKingRankVO.getUserId())) {
                    // 如果当前用户是第一名，就无差距了
                    if (i==0) {
                        luckyKingRankVO.setDistancePoints(0);
                    }else {
                        // 取前一名的数据
                        LuckyKingRankVO beforeLuckyKingRankVO = list.get(i-1);
                        // 设置当前用户的差距
                        luckyKingRankVO.setDistancePoints(beforeLuckyKingRankVO.getTotalPoints() - luckyKingRankVO.getTotalPoints());
                    }
                    luckyKingRankVO.setRank(temp.getRank());
                    break;
                }
            }
        }else {
            // 非前十名，就取列表的最后一名
            LuckyKingRankVO lastLuckyKingRankVO = list.get(list.size()-1);
            luckyKingRankVO.setDistancePoints(lastLuckyKingRankVO.getTotalPoints() - luckyKingRankVO.getTotalPoints());
        }
        return luckyKingRankVO;
    }
}
