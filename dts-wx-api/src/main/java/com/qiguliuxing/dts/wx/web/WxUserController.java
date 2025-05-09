package com.qiguliuxing.dts.wx.web;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.qiguliuxing.dts.wx.dao.UserRegisterVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSONObject;
import com.qiguliuxing.dts.core.util.ResponseUtil;
import com.qiguliuxing.dts.db.domain.DtsUser;
import com.qiguliuxing.dts.db.domain.DtsUserAccount;
import com.qiguliuxing.dts.db.service.DtsAccountService;
import com.qiguliuxing.dts.db.service.DtsCouponService;
import com.qiguliuxing.dts.db.service.DtsOrderService;
import com.qiguliuxing.dts.db.service.DtsUserService;
import com.qiguliuxing.dts.wx.annotation.LoginUser;
import com.qiguliuxing.dts.wx.util.WxResponseCode;
import com.qiguliuxing.dts.wx.util.WxResponseUtil;

/**
 * 用户服务
 */
@RestController
@RequestMapping("/wx/user")
@Validated
public class WxUserController {
	private static final Logger logger = LoggerFactory.getLogger(WxUserController.class);

    @Autowired
    private DtsUserService dtsUserService;


	@GetMapping("/currentSpiritPower")
	public Object currentSpiritPower(String userId) {
		return ResponseUtil.ok(dtsUserService.currentSpiritPower(userId));
	}

}
