package com.qiguliuxing.dts.db.dao;

import com.qiguliuxing.dts.db.domain.LuckyDrawActivity;
import com.qiguliuxing.dts.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LuckyDrawMapper {

    LuckyDrawActivity getActiveActivities();

    List<LuckyDrawPrizeVo> selectPrizesByActivityId(@Param("activityId") Integer activityId);

    /**
     * 查询活动参与人列表
     * @param activityId 活动ID
     * @param periodNumber 活动期数
     * @return 参与人列表
     */
    List<ActivityParticipantVo> selectParticipantsByActivity(
            @Param("activityId") Integer activityId,
            @Param("periodNumber") Integer periodNumber);

    /**
     * 查询中奖用户列表
     * @param activityId 活动ID
     * @param periodNumber 活动期数
     * @return 中奖用户列表
     */
    List<WinnerInfoVo> selectWinnersByActivity(
            @Param("activityId") Integer activityId,
            @Param("periodNumber") Integer periodNumber);

    /**
     * 查询用户抽奖码列表
     * @param userId 用户ID
     * @param activityId 活动ID（可选）
     * @param periodNumber 活动期数（可选）
     * @return 抽奖码列表
     */
    List<UserDrawCodeVo> selectUserDrawCodes(
            @Param("userId") String userId,
            @Param("activityId") Integer activityId,
            @Param("periodNumber") Integer periodNumber);

    /**
     * 查询用户被助力记录
     * @param userId 用户ID
     * @return 被助力记录列表
     */
    List<HelpRecordVo> selectHelpRecords(@Param("userId") String userId);

    /**
     * 检查抽奖码是否已存在
     * @param code 抽奖码
     * @return 是否存在
     */
    boolean existsByCode(@Param("code") String code);

    /**
     * 插入助力记录
     * @param codeVo 抽奖码VO
     * @return 影响行数
     */
    int insertHelpRecord(@Param("codeVo") LuckyDrawCodeVo codeVo);

    /**
     * 参与抽奖
     * @param codeVo 抽奖码VO
     * @return 影响行数
     */
    int joinLottery(@Param("codeVo") LuckyDrawCodeVo codeVo);

    /**
     * 查询活动所有待开奖的抽奖码
     */
    List<LuckyDrawCodeVo> selectPendingCodesByActivityId(@Param("activityId") Integer activityId);



    /**
     * 批量更新抽奖码状态
     * @param codes 需要更新的抽奖码列表
     * @return 更新的记录数
     */
    int batchUpdateResults(@Param("list") List<LuckyDrawCodeVo> codes);
}
