package com.qiguliuxing.dts.wx.web;

import com.qiguliuxing.dts.core.util.JacksonUtil;
import com.qiguliuxing.dts.core.util.ResponseUtil;
import com.qiguliuxing.dts.vo.*;
import com.qiguliuxing.dts.db.service.LuckyDrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/wx/luckyDraw")
public class WxLuckyDrawController {

    @Autowired
    private LuckyDrawService luckyDrawService;

    @GetMapping("/activity")
    public Object getActivityDetail() {
        LuckyDrawActivityVo activityDetail = luckyDrawService.getActivityDetail();
        return ResponseUtil.ok(activityDetail);
    }

    /**
     * 获取活动参与人列表
     * @param activityId 活动ID
     * @param periodNumber 活动期数
     * @return 参与人列表，包含用户ID、头像和抽奖码数量
     */
    @GetMapping("/participants")
    public Object getActivityParticipants(
            @RequestParam Integer activityId,
            @RequestParam Integer periodNumber) {
        List<ActivityParticipantVo> participants =
                luckyDrawService.getActivityParticipants(activityId, periodNumber);
        return ResponseUtil.ok(participants);
    }

    /**
     * 获取中奖用户列表
     * @param activityId 活动ID
     * @param periodNumber 活动期数
     * @return 中奖用户列表
     */
    @GetMapping("/winners")
    public Object getWinnerList(
            @RequestParam Integer activityId,
            @RequestParam Integer periodNumber) {
        List<WinnerInfoVo> winners = luckyDrawService.getWinnerList(activityId, periodNumber);
        return ResponseUtil.ok(winners);
    }

    /**
     * 获取用户抽奖码列表
     * @param userId 用户ID
     * @param activityId 活动ID（可选）
     * @param periodNumber 活动期数（可选）
     * @return 抽奖码列表
     */
    @GetMapping("/userCodes")
    public Object getUserDrawCodes(
            @RequestParam String userId,
            @RequestParam(required = false) Integer activityId,
            @RequestParam(required = false) Integer periodNumber) {
        List<UserDrawCodeVo> codes = luckyDrawService.getUserDrawCodes(userId, activityId, periodNumber);
        return ResponseUtil.ok(codes);
    }

    /**
     * 获取用户被助力记录列表
     * @param userId 用户ID
     * @return 被助力记录列表
     */
    @GetMapping("/helpRecords/{userId}")
    public Object getHelpRecords(@PathVariable String userId) {
        List<HelpRecordVo> records = luckyDrawService.getHelpRecords(userId);
        return ResponseUtil.ok(records);
    }

    /**
     * 助力接口
     * @param helpRequest 助力请求参数
     * @return 操作结果
     */
    @PostMapping("/help")
    public Object helpUser(@Valid @RequestBody HelpRequestVO helpRequest) {
        return luckyDrawService.processHelp(helpRequest);
    }

    /**
     * 用户参与抽奖
     * @param body 请求参数JSON
     * @return 被助力记录列表
     */
    @PostMapping("/joinLottery")
    public Object joinLottery(@RequestBody String body) {
        String userId = JacksonUtil.parseString(body, "userId");
        Integer activityId = JacksonUtil.parseInteger(body, "activityId");
        String code = luckyDrawService.joinLottery(userId, activityId);
        if (StringUtils.isEmpty(code)) {
            ResponseUtil.fail(1, "参与抽奖失败");
        }
        return ResponseUtil.ok(code);
    }
}
