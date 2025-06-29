package com.qiguliuxing.dts.wx.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qiguliuxing.dts.vo.OrderVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qiguliuxing.dts.db.service.DtsOrderService;

/**
 * 订单服务
 *
 * <p>
 * 订单状态： 101 订单生成，未支付；102，下单后未支付用户取消；103，下单后未支付超时系统自动取消 201
 * 支付完成，商家未发货；202，订单生产，已付款未发货，但是退款取消； 301 商家发货，用户未确认； 401 用户确认收货； 402
 * 用户没有确认收货超过一定时间，系统自动确认收货；
 *
 * <p>
 * 用户操作： 当101用户未付款时，此时用户可以进行的操作是取消订单，或者付款操作 当201支付完成而商家未发货时，此时用户可以取消订单并申请退款
 * 当301商家已发货时，此时用户可以有确认收货的操作 当401用户确认收货以后，此时用户可以进行的操作是删除订单，评价商品，或者再次购买
 * 当402系统自动确认收货以后，此时用户可以删除订单，评价商品，或者再次购买
 *
 * <p>
 * 注意：目前不支持订单退货和售后服务
 */
@Service
public class WxOrderService {
	private static final Logger logger = LoggerFactory.getLogger(WxOrderService.class);


	@Autowired
	DtsOrderService dtsOrderService;


	public List<OrderVO> queryOrderList(String userId, Integer orderId, List<String> orderStatusList) {
		return dtsOrderService.queryOrderList(userId, orderId, orderStatusList);
	}

	public OrderVO queryOrderByWxOrderNo(String wxOrderNo) {
		return dtsOrderService.queryOrderByWxOrderNo(wxOrderNo);
	}
}
