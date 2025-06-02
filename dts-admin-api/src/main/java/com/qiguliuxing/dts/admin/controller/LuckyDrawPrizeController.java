package com.qiguliuxing.dts.admin.controller;

import com.github.pagehelper.PageInfo;
import com.qiguliuxing.dts.core.util.ResponseUtil;
import com.qiguliuxing.dts.db.service.LuckyDrawActivityService;
import com.qiguliuxing.dts.db.service.LuckyDrawPrizeService;
import com.qiguliuxing.dts.vo.LuckyDrawActivityVo;
import com.qiguliuxing.dts.vo.LuckyDrawPrizeVo;
import com.qiguliuxing.dts.vo.ProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/luckydraw/prize")
public class LuckyDrawPrizeController {
    @Autowired
    private LuckyDrawPrizeService prizeService;

    @Autowired
    private LuckyDrawActivityService activityService;

    @PostMapping("/create")
    public Object create(@RequestBody LuckyDrawPrizeVo prize) {
        int count = prizeService.create(prize);
        if (count == 0) {
            return ResponseUtil.fail();
        }
        return ResponseUtil.ok(prize);
    }

    @PostMapping("/update")
    public Object update(@RequestBody LuckyDrawPrizeVo prize) {
        int count = prizeService.update(prize);
        if (count == 0) {
            return ResponseUtil.fail();
        }
        return ResponseUtil.ok(prize);
    }

    @PostMapping("/delete")
    public Object delete(@RequestBody LuckyDrawPrizeVo prize) {
        int count = prizeService.delete(prize.getPrizeId());
        if (count == 0) {
            return ResponseUtil.fail();
        }
        return ResponseUtil.ok();
    }

    @GetMapping("/getAllActivityNames")
    public Object getAllActivityNames() {
        List<LuckyDrawActivityVo> activityVos = activityService.findAll();
        Map<Integer, String> activityIdToName = activityVos.stream().collect(Collectors.toMap(LuckyDrawActivityVo::getActivityId, LuckyDrawActivityVo::getActivityName));
        return ResponseUtil.ok(activityIdToName);
    }

    @GetMapping("/getPrizesByCondition")
    public Object getPrizesByCondition(Integer activityId, String activityName, String productName) {
        List<LuckyDrawPrizeVo> prizes = prizeService.getPrizesByCondition(activityId, activityName, productName);
        long total = PageInfo.of(prizes).getTotal();
        Map<String, Object> data = new HashMap<>();
        data.put("total", total);
        data.put("items", prizes);
        return ResponseUtil.ok(data);
    }

    @PostMapping("/decreaseQuantity")
    public Object decreaseQuantity(@RequestParam Integer prizeId, @RequestParam Integer quantity) {
        int count = prizeService.decreaseQuantity(prizeId, quantity);
        if (count == 0) {
            return ResponseUtil.fail(500, "扣减库存失败，可能库存不足");
        }
        return ResponseUtil.ok();
    }

    @GetMapping("/getNoSeriesProducts")
    public Object getNoSeriesProducts() {
        List<ProductVO> noSeriesProducts = prizeService.getNoSeriesProducts();
        long total = PageInfo.of(noSeriesProducts).getTotal();
        Map<String, Object> data = new HashMap<>();
        data.put("total", total);
        data.put("items", noSeriesProducts);
        return ResponseUtil.ok(data);
    }
}
