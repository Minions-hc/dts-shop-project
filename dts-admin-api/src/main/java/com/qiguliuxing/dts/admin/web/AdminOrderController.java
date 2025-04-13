package com.qiguliuxing.dts.admin.web;

import java.util.*;

import javax.validation.constraints.NotNull;

import com.github.pagehelper.PageInfo;
import com.mysql.jdbc.StringUtils;
import com.qiguliuxing.dts.admin.util.AdminResponseUtil;
import com.qiguliuxing.dts.core.util.ResponseUtil;
import com.qiguliuxing.dts.db.service.DtsOrderService;
import com.qiguliuxing.dts.vo.OrderDetailVO;
import com.qiguliuxing.dts.vo.OrderVO;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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

import com.qiguliuxing.dts.admin.annotation.RequiresPermissionsDesc;
import com.qiguliuxing.dts.admin.service.AdminOrderService;
import com.qiguliuxing.dts.core.validator.Order;
import com.qiguliuxing.dts.core.validator.Sort;

import static com.qiguliuxing.dts.admin.util.AdminResponseCode.ORDER_NOT_EXIST;

@RestController
@RequestMapping("/admin/order")
@Validated
public class AdminOrderController {
	private static final Logger logger = LoggerFactory.getLogger(AdminOrderController.class);

	@Autowired
	private AdminOrderService adminOrderService;


	@Autowired
	private DtsOrderService dtsOrderService;

	/**
	 * 查询订单
	 *
	 * @param userId
	 * @param page
	 * @param limit
	 * @return
	 */
	@RequiresPermissions("admin:order:list")
	@RequiresPermissionsDesc(menu = { "商场管理", "订单管理" }, button = "查询")
	@GetMapping("/queryOrderList")
	public Object queryOrderList(String userId, String orderNo, String orderStatusStr,
			@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit) {
		logger.info("【请求开始】商场管理->订单管理->查询,请求参数:userId:{},orderSn:{},page:{}", userId, orderNo, page);
		List<String> orderStatusList = new ArrayList<>();
		if (!StringUtils.isNullOrEmpty(orderStatusStr)) {
			orderStatusList = Arrays.asList(orderStatusStr.split(","));
		}
		List<OrderVO> orderList = dtsOrderService.queryOrderList(userId, orderNo, orderStatusList, page, limit);
		long total = PageInfo.of(orderList).getTotal();
		Map<String, Object> data = new HashMap<>();
		data.put("total", total);
		data.put("items", orderList);
		return ResponseUtil.ok(data);
	}

	/**
	 * 订单详情
	 *
	 * @return
	 */
	@RequiresPermissions("admin:order:read")
	@RequiresPermissionsDesc(menu = { "商场管理", "订单管理" }, button = "详情")
	@GetMapping("/queryOrderDetail")
	public Object queryOrderDetail(@NotNull String orderNo) {
		logger.info("【请求开始】商场管理->订单管理->详情,请求参数: orderNo:{}", orderNo);
		OrderDetailVO orderDetail = dtsOrderService.queryOrderDetail(orderNo);
		if (orderDetail != null) {
			return AdminResponseUtil.ok(orderDetail);
		}

		return AdminResponseUtil.fail(ORDER_NOT_EXIST);
	}

	/**
	 * 订单退款
	 *
	 * @param body 订单信息，{ orderId：xxx }
	 * @return 订单退款操作结果
	 */
	@RequiresPermissions("admin:order:refund")
	@RequiresPermissionsDesc(menu = { "商场管理", "订单管理" }, button = "订单退款")
	@PostMapping("/refund")
	public Object refund(@RequestBody String body) {
		logger.info("【请求开始】商场管理->订单管理->订单退款,请求参数,body:{}", body);

		return adminOrderService.refund(body);
	}

	/**
	 * 发货
	 *
	 * @param body 订单信息，{ orderId：xxx, shipSn: xxx, shipChannel: xxx }
	 * @return 订单操作结果
	 */
	@RequiresPermissions("admin:order:ship")
	@RequiresPermissionsDesc(menu = { "商场管理", "订单管理" }, button = "订单发货")
	@PostMapping("/ship")
	public Object ship(@RequestBody String body) {
		logger.info("【请求开始】商场管理->订单管理->订单发货,请求参数,body:{}", body);

		return adminOrderService.ship(body);
	}

	/**
	 * 回复订单商品
	 *
	 * @param body 订单信息，{ orderId：xxx }
	 * @return 订单操作结果
	 */
	@RequiresPermissions("admin:order:reply")
	@RequiresPermissionsDesc(menu = { "商场管理", "订单管理" }, button = "订单商品回复")
	@PostMapping("/reply")
	public Object reply(@RequestBody String body) {
		logger.info("【请求开始】商场管理->订单管理->订单商品回复,请求参数,body:{}", body);

		return adminOrderService.reply(body);
	}

}
