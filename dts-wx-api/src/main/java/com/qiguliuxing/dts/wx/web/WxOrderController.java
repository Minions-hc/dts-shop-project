package com.qiguliuxing.dts.wx.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import com.github.pagehelper.PageInfo;
import com.mysql.jdbc.StringUtils;
import com.qiguliuxing.dts.core.util.ResponseUtil;
import com.qiguliuxing.dts.vo.OrderVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qiguliuxing.dts.wx.annotation.LoginUser;
import com.qiguliuxing.dts.wx.service.WxOrderService;

import java.util.*;

@RestController
@RequestMapping("/wx/order")
@Validated
public class WxOrderController {
	private static final Logger logger = LoggerFactory.getLogger(WxOrderController.class);

	@Autowired
	private WxOrderService wxOrderService;

	/**
	 * 订单列表
	 *
	 * @param userId
	 */
	@GetMapping("queryOrderList")
	public Object queryOrderList(String userId, String orderNo, String orderStatusStr) {
		List<String> orderStatusList = new ArrayList<>();
		if (!StringUtils.isNullOrEmpty(orderStatusStr)) {
			orderStatusList = Arrays.asList(orderStatusStr.split(","));
		}
		List<OrderVO> orderList = wxOrderService.queryOrderList(userId, orderNo, orderStatusList);
		Map<String, Object> data = new HashMap<>();
		data.put("items", orderList);
		return ResponseUtil.ok(data);
	}

}
