package com.qiguliuxing.dts.db.service;

import com.qiguliuxing.dts.db.dao.LuckyDrawMapper;
import com.qiguliuxing.dts.db.domain.LuckyDrawActivity;
import com.qiguliuxing.dts.vo.*;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class LuckyDrawService {

    private static final String DRAW_CODE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int DRAW_CODE_LENGTH = 6;
    private static final int MAX_RETRY_TIMES = 5;

    private static final Logger logger = LoggerFactory.getLogger(LuckyDrawService.class);

    @Autowired
    private LuckyDrawMapper luckyDrawMapper;

    public LuckyDrawActivityVo getActivityDetail() {
        // 查询有效活动基本信息
        LuckyDrawActivity activity = luckyDrawMapper.getActiveActivities();
        if (activity == null) {
            return null;
        }

        // 查询活动奖品列表
        List<LuckyDrawPrizeVo> prizeList = selectPrizesByActivityId(activity.getActivityId());

        // 组装返回VO
        LuckyDrawActivityVo activityVo = new LuckyDrawActivityVo();
        BeanUtils.copyProperties(activity, activityVo);
        activityVo.setPrizeList(prizeList);
        return activityVo;
    }

    /**
     * 查询活动参与人列表
     * @param activityId 活动ID
     * @param periodNumber 活动期数
     * @return 参与人列表，包含用户基本信息及抽奖码数量
     */
    public List<ActivityParticipantVo> getActivityParticipants(
            Integer activityId,
            Integer periodNumber) {
        return luckyDrawMapper.selectParticipantsByActivity(activityId, periodNumber);
    }


    /**
     * 查询中奖用户列表
     * @param activityId 活动ID
     * @param periodNumber 活动期数
     * @return 中奖用户列表，包含用户信息和奖品信息
     */
    public List<WinnerInfoVo> getWinnerList(Integer activityId, Integer periodNumber) {
        return luckyDrawMapper.selectWinnersByActivity(activityId, periodNumber);
    }

    /**
     * 查询用户抽奖码列表
     * @param userId 用户ID
     * @param activityId 活动ID（可选）
     * @param periodNumber 活动期数（可选）
     * @return 抽奖码列表
     */
    public List<UserDrawCodeVo> getUserDrawCodes(
            String userId,
            Integer activityId,
            Integer periodNumber) {
        return luckyDrawMapper.selectUserDrawCodes(userId, activityId, periodNumber);
    }

    /**
     * 查询用户被助力记录
     * @param userId 用户ID
     * @return 被助力记录列表
     */
    public List<HelpRecordVo> getHelpRecords(String userId) {
        return luckyDrawMapper.selectHelpRecords(userId);
    }


    /**
     * 处理助力请求
     * @param helpRequest 助力请求
     * @return 操作结果
     */
    public Object processHelp(HelpRequestVO helpRequest) {
        // 1. 生成唯一抽奖码
        String drawCode = generateUniqueDrawCode();

        // 2. 创建助力记录
        LuckyDrawCodeVo codeVo = new LuckyDrawCodeVo();
        codeVo.setUserId(helpRequest.getUserId());
        codeVo.setCode(drawCode);
        codeVo.setSource("HELP");
        codeVo.setHelpUserId(helpRequest.getHelperId());
        codeVo.setCreateTime(new Date());
        codeVo.setStatus("PENDING");
        codeVo.setActivityId(helpRequest.getActivityId());
        codeVo.setActivityName(helpRequest.getActivityName());
        codeVo.setPeriodNumber(helpRequest.getPeriodNumber());
        return  luckyDrawMapper.insertHelpRecord(codeVo);
    }


    public String joinLottery(String userId, Integer activityId) {
        // 查询有效活动基本信息
        LuckyDrawActivity activity = luckyDrawMapper.getActiveActivities();
        if (activity == null) {
            return null;
        }
        // 1. 生成唯一抽奖码
        String drawCode = generateUniqueDrawCode();

        // 2. 创建助力记录
        LuckyDrawCodeVo codeVo = new LuckyDrawCodeVo();
        codeVo.setUserId(userId);
        codeVo.setCode(drawCode);
        codeVo.setSource("INITIAL");
        codeVo.setHelpUserId(null);
        codeVo.setCreateTime(new Date());
        codeVo.setStatus("PENDING");
        codeVo.setActivityId(activity.getActivityId());
        codeVo.setActivityName(activity.getActivityName());
        codeVo.setPeriodNumber(activity.getPeriodNumber());
        int result = luckyDrawMapper.joinLottery(codeVo);
        if(result != 1){
            return null;
        }
        return drawCode;
    }

    /**
     * 生成唯一抽奖码
     * @return 唯一抽奖码
     */
    public String generateUniqueDrawCode() {
        return generateUniqueDrawCode(0);
    }

    /**
     * 递归生成唯一抽奖码
     * @param retryCount 重试次数
     * @return 唯一抽奖码
     */
    private String generateUniqueDrawCode(int retryCount) {
        if (retryCount >= MAX_RETRY_TIMES) {
            throw new RuntimeException("生成抽奖码失败，超过最大重试次数");
        }

        String code = generateRandomCode();
        if (!luckyDrawMapper.existsByCode(code)) {
            return code;
        }
        return generateUniqueDrawCode(retryCount + 1);
    }

    /**
     * 生成随机抽奖码
     * @return 6位随机字符串(大写字母+数字)
     */
    private String generateRandomCode() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < DRAW_CODE_LENGTH; i++) {
            sb.append(DRAW_CODE_CHARS.charAt(random.nextInt(DRAW_CODE_CHARS.length())));
        }
        return sb.toString();
    }

    /**
     * 查询当期获得的所有奖品
     * @param activityId 活动ID
     * @return 奖品列表
     */
    public List<LuckyDrawPrizeVo> selectPrizesByActivityId(Integer activityId){
        return luckyDrawMapper.selectPrizesByActivityId(activityId);
    }


    /**
     * 查询活动所有待开奖的抽奖码
     */
    public List<LuckyDrawCodeVo> selectPendingCodesByActivityId(Integer activityId){
        return luckyDrawMapper.selectPendingCodesByActivityId(activityId);
    }

    /**
     * 批量更新抽奖码状态
     * @param codes 需要更新的抽奖码列表
     * @return 更新的记录数
     */
    public int batchUpdateResults(List<LuckyDrawCodeVo> codes){
        return luckyDrawMapper.batchUpdateResults(codes);
    }
}
