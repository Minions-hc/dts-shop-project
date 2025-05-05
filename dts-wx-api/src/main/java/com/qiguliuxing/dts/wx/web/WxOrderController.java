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


	/**
	 * 订单详情
	 *
	 * @param userId
	 *            用户ID
	 * @param orderId
	 *            订单ID
	 * @return 订单详情
	 */
	@GetMapping("detail")
	public Object detail(Integer userId, @NotNull Integer orderId) {
		logger.info("【请求开始】查库订单详情,请求参数,userId:{},orderId:{}", userId, orderId);
		return wxOrderService.detail(userId, orderId);
	}

	/**
	 * 物流跟踪
	 *
	 * @param userId
	 * @param orderId
	 * @return
	 */
	@GetMapping("expressTrace")
	public Object expressTrace(@LoginUser Integer userId, @NotNull Integer orderId) {
		logger.info("【请求开始】查库订单物流跟踪,请求参数,userId:{},orderId:{}", userId, orderId);
		return wxOrderService.expressTrace(userId, orderId);
	}

	/**
	 * 提交订单
	 *
	 * @param userId
	 *            用户ID
	 * @param body
	 *            订单信息，{ cartId：xxx, addressId: xxx, couponId: xxx, message: xxx,
	 *            grouponRulesId: xxx, grouponLinkId: xxx}
	 * @return 提交订单操作结果
	 */
	@PostMapping("submit")
	public Object submit(@LoginUser Integer userId, @RequestBody String body) {
		logger.info("【请求开始】提交用户订单,请求参数,userId:{},body:{}", userId, body);
		return wxOrderService.submit(userId, body);
	}


	/**
	 * 付款订单的预支付会话标识
	 *
	 * @param userId
	 *            用户ID
	 * @param body
	 *            订单信息，{ orderId：xxx }
	 * @return 支付订单ID
	 */
	@PostMapping("prepay")
	public Object prepay(@LoginUser Integer userId, @RequestBody String body, HttpServletRequest request) {
		logger.info("【请求开始】付款订单的预支付会话标识,请求参数,userId:{},body:{}", userId, body);
		return wxOrderService.prepay(userId, body, request);
	}

	/**
	 * 微信付款成功或失败回调接口
	 * <p>
	 * TODO 注意，这里pay-notify是示例地址，建议开发者应该设立一个隐蔽的回调地址
	 *
	 * @param request
	 *            请求内容
	 * @param response
	 *            响应内容
	 * @return 操作结果
	 */
	@PostMapping("dtsNotify")
	public Object payNotify(HttpServletRequest request, HttpServletResponse response) {
		logger.info("【请求开始】微信付款成功或失败回调...");
		return wxOrderService.dtsPayNotify(request, response);
	}

}
