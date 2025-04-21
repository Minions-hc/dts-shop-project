package com.qiguliuxing.dts.wx.web;

import com.qiguliuxing.dts.core.util.JacksonUtil;
import com.qiguliuxing.dts.core.util.ResponseUtil;
import com.qiguliuxing.dts.db.service.CheckInService;
import com.qiguliuxing.dts.wx.annotation.LoginUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wx/checkin")
@Validated
public class WxCheckInController {

    private static final Logger logger = LoggerFactory.getLogger(WxCheckInController.class);

    @Autowired
    private CheckInService checkInService;

    @PostMapping("userCheckIn")
    public Object userCheckIn(@LoginUser String userId, @RequestBody String body) {
        if (StringUtils.isEmpty(userId)) {
            logger.error("用户收藏添加或删除失败:用户未登录！！！");
            return ResponseUtil.unlogin();
        }
        Integer checkInDay = JacksonUtil.parseInteger(body, "checkInDay");
        Integer points = JacksonUtil.parseInteger(body, "points");
        checkInService.checkIn(userId, checkInDay, points);
        return ResponseUtil.ok();
    }

}
