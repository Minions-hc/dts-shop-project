package com.qiguliuxing.dts.admin.web;

import java.util.*;

import javax.validation.constraints.NotNull;

import com.github.pagehelper.PageInfo;
import com.mysql.jdbc.StringUtils;
import com.qiguliuxing.dts.admin.util.AdminResponseUtil;
import com.qiguliuxing.dts.core.util.ResponseUtil;
import com.qiguliuxing.dts.db.service.DtsOrderService;
import com.qiguliuxing.dts.db.service.LogisticsService;

import com.qiguliuxing.dts.vo.OrderDetailVO;
import com.qiguliuxing.dts.vo.OrderVO;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qiguliuxing.dts.admin.annotation.RequiresPermissionsDesc;
import com.qiguliuxing.dts.admin.service.AdminOrderService;

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

	@Autowired
	private LogisticsService logisticsService;

	/**
	 * 查询订单
	 *
	 * @param userId
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

}
