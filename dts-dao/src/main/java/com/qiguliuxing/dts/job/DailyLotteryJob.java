package com.qiguliuxing.dts.job;

import com.qiguliuxing.dts.db.service.LuckyDrawActivityService;
import com.qiguliuxing.dts.db.service.LuckyDrawService;
import com.qiguliuxing.dts.vo.LuckyDrawActivityVo;
import com.qiguliuxing.dts.vo.LuckyDrawCodeVo;
import com.qiguliuxing.dts.vo.LuckyDrawPrizeVo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 每日开奖定时任务
 */
@Component
public class DailyLotteryJob {

    @Resource
    private LuckyDrawActivityService luckyDrawActivityService;

    @Resource
    private LuckyDrawService luckyDrawService;

    private static boolean isSameDate(LocalDate localDate, Date date) {
        if (date == null) {
            return false; // 或者根据你的需求处理null情况
        }
        // 将Date转换为LocalDate（使用系统默认时区）
        LocalDate convertedDate = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        return localDate.equals(convertedDate);
    }

    /**
     * 每天23:00执行开奖
     */
    @Scheduled(cron = "0 0 23 * * ?")
    @Transactional
    public void executeDailyLottery() {
        LocalDate today = LocalDate.now();
        System.out.println("开始执行每日开奖任务，当前日期: " + today);

        // 1. 查询今天需要开奖的有效活动
        LuckyDrawActivityVo activitiy = luckyDrawActivityService.selectValidActivitiy();
        if (activitiy == null) {
            System.out.println("没有需要开奖的活动");
            return;
        }
        // 判断是否是开奖日期
        if (!isSameDate(today, activitiy.getDrawDate())){
            System.out.println("开奖日期还没到");
            return;
        }

        // 3. 查询活动奖品
        // 查询活动奖品列表
        List<LuckyDrawPrizeVo> prizeList = luckyDrawService.selectPrizesByActivityId(activitiy.getActivityId());
        if (prizeList.isEmpty()) {
            System.out.println("活动[" + activitiy.getActivityName() + "]没有设置奖品，跳过开奖");
            return;
        }

        // 4. 查询活动所有抽奖码
        List<LuckyDrawCodeVo> allCodes = luckyDrawService.selectPendingCodesByActivityId(activitiy.getActivityId());
        if (allCodes.isEmpty()) {
            System.out.println("活动[" + activitiy.getActivityName() + "]没有抽奖码，跳过开奖");
            return;
        }
        int allPrizeQuantity = 0;
        // 获取所有奖品数量
        for (LuckyDrawPrizeVo prize : prizeList) {
            allPrizeQuantity = allPrizeQuantity + prize.getProductQuantity();
        }
        // 5. 随机抽取中奖者（奖品数量=中奖人数）
        List<LuckyDrawCodeVo> winners = drawWinners(allCodes, allPrizeQuantity);

        List<LuckyDrawCodeVo> winnerUsers = recordWinners(prizeList, winners);

        // 2. 标记未中奖的抽奖码
        Set<String> winnerCodes = winners.stream()
                .map(LuckyDrawCodeVo::getCode)
                .collect(Collectors.toSet());
        List<LuckyDrawCodeVo> loserUsers = allCodes.stream()
                .filter(code -> !winnerCodes.contains(code.getCode())).collect(Collectors.toList());

        if(!CollectionUtils.isEmpty(winnerUsers)){
            int result = luckyDrawService.batchUpdateResults(winnerUsers);
            if (result < 1){
                System.out.println("活动[" + activitiy.getActivityName() + "]，更新中奖信息失败！");
            }
        }

        if(!CollectionUtils.isEmpty(loserUsers)){
            int result = luckyDrawService.batchUpdateResults(loserUsers);
            if (result < 1){
                System.out.println("活动[" + activitiy.getActivityName() + "]，更新中奖信息失败！");
            }
        }
        activitiy.setActive(false);
        int result = luckyDrawActivityService.update(activitiy);
        if (result < 1){
            System.out.println("活动[" + activitiy.getActivityName() + "]，更新活动信息失败！");
        }
    }

    /**
     * 随机抽取中奖者
     */
    private List<LuckyDrawCodeVo> drawWinners(List<LuckyDrawCodeVo> allCodes, int winnerCount) {
        // 如果抽奖码数量不足，则按照抽奖码的数量抽取
        if (allCodes.size() <= winnerCount) {
            winnerCount = allCodes.size();
        }

        // 1. 按用户分组，统计每个用户的抽奖码数量
        Map<String, List<LuckyDrawCodeVo>> userCodesMap = allCodes.stream()
                .collect(Collectors.groupingBy(LuckyDrawCodeVo::getUserId));

        // 2. 创建包含所有抽奖码的列表（用户有N个抽奖码就包含N次）
        List<LuckyDrawCodeVo> weightedCodes = new ArrayList<>();
        userCodesMap.forEach((userId, codes) -> {
            weightedCodes.addAll(codes);
        });

        // 3. 随机抽取（考虑权重）
        Set<String> winnerUserIds = new HashSet<>();
        List<LuckyDrawCodeVo> winners = new ArrayList<>();
        Random random = new Random();

        while (winners.size() < winnerCount && !weightedCodes.isEmpty()) {
            // 随机选取一个抽奖码（考虑权重）
            int randomIndex = random.nextInt(weightedCodes.size());
            LuckyDrawCodeVo selectedCode = weightedCodes.get(randomIndex);
            String userId = selectedCode.getUserId();

            if (!winnerUserIds.contains(userId)) {
                // 新中奖用户
                winners.add(selectedCode);
                winnerUserIds.add(userId);

                // 移除该用户的所有抽奖码（防止重复中奖）
                weightedCodes.removeIf(code -> code.getUserId().equals(userId));
            } else {
                // 已中奖用户，只移除当前抽奖码
                weightedCodes.remove(randomIndex);
            }
        }
        return winners;
    }

    /**
     * 记录中奖结果
     *
     * @return
     */
    private List<LuckyDrawCodeVo> recordWinners(List<LuckyDrawPrizeVo> prizes, List<LuckyDrawCodeVo> winners) {

        List<LuckyDrawCodeVo> winnerUsers = new ArrayList<>();
        for (int i = 0; i < Math.min(prizes.size(), winners.size()); i++) {
            LuckyDrawPrizeVo prize = prizes.get(i);
            LuckyDrawCodeVo winner = winners.get(i);
            winner.setProductId(prize.getProductId());
            winner.setProductName(prize.getProductName());
            winner.setStatus("WIN");
            winnerUsers.add(winner);
        }
        return winnerUsers;
    }

}
