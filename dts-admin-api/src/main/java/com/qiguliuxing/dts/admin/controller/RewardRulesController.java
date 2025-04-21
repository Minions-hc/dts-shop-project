package com.qiguliuxing.dts.admin.controller;


import com.github.pagehelper.PageInfo;
import com.qiguliuxing.dts.core.util.ResponseUtil;
import com.qiguliuxing.dts.db.service.RewardRulesService;
import com.qiguliuxing.dts.vo.RewardRulesVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/rewardrules")
public class RewardRulesController {
    @Autowired
    private RewardRulesService rewardRulesService;

    /**
     * 添加奖励规则
     */
    @PostMapping("/create")
    public Object create(@RequestBody RewardRulesVo rewardRulesVo) {
        int count = rewardRulesService.add(rewardRulesVo);
        if (count == 0) {
            return ResponseUtil.fail();
        }
        return ResponseUtil.ok(rewardRulesVo);
    }

    /**
     * 更新奖励规则
     */
    @PostMapping("/update")
    public Object update(@RequestBody RewardRulesVo rewardRulesVo) {
        int count = rewardRulesService.update(rewardRulesVo);
        if (count == 0) {
            return ResponseUtil.fail();
        }
        return ResponseUtil.ok(rewardRulesVo);
    }

    /**
     * 删除奖励规则
     */
    @PostMapping("/delete")
    public Object delete(@RequestBody RewardRulesVo rewardRulesVo) {
        int count = rewardRulesService.delete(rewardRulesVo.getId());
        if (count == 0) {
            return ResponseUtil.fail();
        }
        return ResponseUtil.ok();
    }

    /**
     * 获取奖励规则详情
     */
    @GetMapping("/detail")
    public Object detail(Integer id) {
        RewardRulesVo rewardRulesVo = rewardRulesService.findById(id);
        return ResponseUtil.ok(rewardRulesVo);
    }

    /**
     * 获取所有奖励规则列表
     */
    @GetMapping("/list")
    public Object list() {
        List<RewardRulesVo> list = rewardRulesService.findAll();
        long total = PageInfo.of(list).getTotal();
        Map<String, Object> data = new HashMap<>();
        data.put("total", total);
        data.put("items", list);
        return ResponseUtil.ok(data);
    }

    /**
     * 根据排名顺序获取奖励规则
     */
    @GetMapping("/getByRankOrder")
    public Object getByRankOrder(Integer rankOrder) {
        RewardRulesVo rewardRulesVo = rewardRulesService.findByRankOrder(rankOrder);
        return ResponseUtil.ok(rewardRulesVo);
    }
}
