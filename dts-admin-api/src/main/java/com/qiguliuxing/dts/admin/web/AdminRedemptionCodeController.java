package com.qiguliuxing.dts.admin.web;


import com.github.pagehelper.PageInfo;
import com.qiguliuxing.dts.core.util.ResponseUtil;
import com.qiguliuxing.dts.db.service.RedemptionCodeService;
import com.qiguliuxing.dts.vo.RedemptionCodeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/redemptionCode")
public class AdminRedemptionCodeController {

    @Autowired
    private RedemptionCodeService redemptionCodeService;

    @PostMapping("/create")
    public Object create() {
        if (redemptionCodeService.createRedemptionCode()) {
            return ResponseUtil.ok();
        }
        return ResponseUtil.fail();
    }

    @PostMapping("/delete/{code}")
    public Object delete(@PathVariable String code) {
        if (redemptionCodeService.deleteRedemptionCode(code)) {
            return ResponseUtil.ok();
        }
        return ResponseUtil.fail();
    }

    @GetMapping("/detail/{code}")
    public Object detail(@PathVariable String code) {
        RedemptionCodeVo redemptionCode = redemptionCodeService.getRedemptionCode(code);
        return ResponseUtil.ok(redemptionCode);
    }

    @GetMapping("/list")
    public Object list(
            @RequestParam(required = false) Integer codeType,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) Boolean available,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        List<RedemptionCodeVo> list = redemptionCodeService.listRedemptionCodes(codeType, code, available, page, size);
        long total = PageInfo.of(list).getTotal();
        Map<String, Object> data = new HashMap<>();
        data.put("total", total);
        data.put("items", list);
        return ResponseUtil.ok(data);
    }

    @PostMapping("/updateStatus")
    public Object updateStatus(
            @RequestParam String code,
            @RequestParam boolean available) {
        if (redemptionCodeService.updateRedemptionCodeStatus(code, available)) {
            return ResponseUtil.ok();
        }
        return ResponseUtil.fail();
    }
}
