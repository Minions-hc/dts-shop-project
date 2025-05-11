package com.qiguliuxing.dts.wx.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.qiguliuxing.dts.core.util.ResponseUtil;
import com.qiguliuxing.dts.db.service.DtsUserService;

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
