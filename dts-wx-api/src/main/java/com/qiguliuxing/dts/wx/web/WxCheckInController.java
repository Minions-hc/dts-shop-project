package com.qiguliuxing.dts.wx.web;

import com.qiguliuxing.dts.core.util.JacksonUtil;
import com.qiguliuxing.dts.core.util.ResponseUtil;
import com.qiguliuxing.dts.db.service.CheckInService;
import com.qiguliuxing.dts.db.service.PointsTransactionService;
import com.qiguliuxing.dts.wx.annotation.LoginUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/wx/checkin")
@Validated
public class WxCheckInController {

    private static final Logger logger = LoggerFactory.getLogger(WxCheckInController.class);

    @Autowired
    private CheckInService checkInService;

    @Autowired
    private PointsTransactionService pointsTransactionService;

    @PostMapping("userCheckIn")
    public Object userCheckIn(@RequestBody String body) {
        String userId = JacksonUtil.parseString(body, "userId");
        if (StringUtils.isEmpty(userId)) {
            logger.error("用户收藏添加或删除失败:用户未登录！！！");
            return ResponseUtil.unlogin();
        }
        Integer checkInDay = JacksonUtil.parseInteger(body, "checkInDay");
        Integer points = JacksonUtil.parseInteger(body, "points");
        checkInService.checkIn(userId, checkInDay, points);
        return ResponseUtil.ok();
    }

    @GetMapping("currentCheckInDay")
    public Object currentCheckInDay(String userId) {
        Integer checkInDay = checkInService.currentCheckInDay(userId);
        Integer currentPoints = pointsTransactionService.getUserCurrentPoints(userId);
        Date lastCheckInDay = pointsTransactionService.findUserLatestCheckInDay(userId);
        Map<String, Object> data = new HashMap<>();
        data.put("checkInDay", checkInDay);
        data.put("currentPoints", currentPoints);
        data.put("lastCheckInDay", lastCheckInDay);
        return ResponseUtil.ok(data);
    }

}
