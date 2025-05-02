package com.qiguliuxing.dts.job;


import com.qiguliuxing.dts.db.dao.RewardRulesMapper;
import com.qiguliuxing.dts.db.service.LuckyKingRankService;
import com.qiguliuxing.dts.db.service.PointsTransactionService;
import com.qiguliuxing.dts.db.util.PointsTransactionType;
import com.qiguliuxing.dts.vo.LuckyKingRankVO;
import com.qiguliuxing.dts.vo.RewardRulesVo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

/**
 * 月度积分分发定时任务
 */
@Component
public class MonthlyPointsDistributionJob {

    @Resource
    private LuckyKingRankService luckyKingRankService;

    @Resource
    private RewardRulesMapper rewardRulesMapper;

    @Resource
    private PointsTransactionService pointsTransactionService;


    /**
     * 每月最后一天23:59:59执行积分分发
     */
    @Scheduled(cron = "59 59 23 L * ?")
    @Transactional
    public void distributeMonthlyPoints() {
        // 1. 获取当月最后一天
        LocalDate lastDayOfMonth = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
        System.out.println("开始执行月度积分分发，月份最后一天: " + lastDayOfMonth);

        // 2. 获取当月欧皇榜TOP10
        List<LuckyKingRankVO> top10Users = luckyKingRankService.getCurrentMonthTop10();
        if (top10Users.isEmpty()) {
            System.out.println("当月没有欧皇榜数据，跳过积分分发");
            return;
        }

        // 3. 获取当月总积分
        Integer totalPoints = luckyKingRankService.getTotalLuckyKingPoints();
        if (totalPoints == null || totalPoints == 0) {
            System.out.println("当月总积分为0，跳过积分分发");
            return;
        }

        System.out.println("当月欧皇榜总积分: " + totalPoints);

        // 4. 获取奖励规则
        List<RewardRulesVo> rewardRules = rewardRulesMapper.selectTop10RewardRules();
        if (rewardRules.isEmpty()) {
            System.out.println("没有配置奖励规则，跳过积分分发");
            return;
        }

        // 5. 按排名分发积分
        for (int i = 0; i < Math.min(top10Users.size(), rewardRules.size()); i++) {
            LuckyKingRankVO user = top10Users.get(i);
            RewardRulesVo rule = rewardRules.get(i);

            // 计算应得积分 = 总积分 * 百分比
            BigDecimal pointsToAdd = BigDecimal.valueOf(totalPoints)
                    .multiply(rule.getPercentage().divide(BigDecimal.valueOf(100)));
            int roundedPoints = pointsToAdd.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
            // 更新用户积分
            pointsTransactionService.insertPointsTransaction(user.getUserId(), roundedPoints, PointsTransactionType.LUCKY_LIST.getCode(), null);
            System.out.printf("用户 %s(%s) 获得第%d名奖励: %d 积分 (%.2f%%)\n",
                    user.getUserName(), user.getUserId(),
                    user.getRank(), roundedPoints, rule.getPercentage());
        }

        System.out.println("月度积分分发完成");
    }
}
