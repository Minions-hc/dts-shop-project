package com.qiguliuxing.dts.admin.controller;


import com.github.pagehelper.PageInfo;
import com.qiguliuxing.dts.core.util.ResponseUtil;
import com.qiguliuxing.dts.db.service.LuckyDrawActivityService;
import com.qiguliuxing.dts.vo.LuckyDrawActivityVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/luckydraw/activity")
public class LuckyDrawActivityController {
    @Autowired
    private LuckyDrawActivityService activityService;

    @PostMapping("/create")
    public Object create(@RequestBody LuckyDrawActivityVo activity) {
        // 检查期数是否已存在
        LuckyDrawActivityVo exist = activityService.findByPeriodNumber(activity.getPeriodNumber());
        if (exist != null) {
            return ResponseUtil.fail(500, "该活动期数已存在");
        }

        int count = activityService.create(activity);
        if (count == 0) {
            return ResponseUtil.fail();
        }
        return ResponseUtil.ok(activity);
    }

    @PostMapping("/update")
    public Object update(@RequestBody LuckyDrawActivityVo activity) {
        int count = activityService.update(activity);
        if (count == 0) {
            return ResponseUtil.fail();
        }
        return ResponseUtil.ok(activity);
    }

    @PostMapping("/delete")
    public Object delete(@RequestBody LuckyDrawActivityVo activity) {
        int count = activityService.delete(activity.getActivityId());
        if (count == 0) {
            return ResponseUtil.fail();
        }
        return ResponseUtil.ok();
    }

    @GetMapping("/detail")
    public Object detail(@RequestParam Integer activityId) {
        LuckyDrawActivityVo activity = activityService.findById(activityId);
        if (activity == null) {
            return ResponseUtil.fail(404, "活动不存在");
        }
        return ResponseUtil.ok(activity);
    }

    @GetMapping("/list")
    public Object list() {
        List<LuckyDrawActivityVo> activities = activityService.findAll();
        long total = PageInfo.of(activities).getTotal();
        Map<String, Object> data = new HashMap<>();
        data.put("total", total);
        data.put("items", activities);
        return ResponseUtil.ok(data);
    }
}
