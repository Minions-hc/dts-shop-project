package com.qiguliuxing.dts.wx.web;

import com.qiguliuxing.dts.db.service.RewardRulesService;
import com.qiguliuxing.dts.vo.RewardRulesVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 奖励规则Controller
 */
@RestController
@RequestMapping("/wx/reward")
public class WxRewardRuleController {

    @Resource
    private RewardRulesService rewardRulesService;

    /**
     * 获取前10名奖励规则
     *
     * @return 奖励规则列表（格式：第一名 35.00%）
     */
    @GetMapping("/rules")
    public List<RewardRulesVo> getRewardRules() {
        return rewardRulesService.getTop10RewardRules();
    }
}
