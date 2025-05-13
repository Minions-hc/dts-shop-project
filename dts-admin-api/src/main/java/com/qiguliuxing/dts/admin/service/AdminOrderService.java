package com.qiguliuxing.dts.admin.service;

import com.qiguliuxing.dts.db.util.OrderUtil;
import com.qiguliuxing.dts.vo.OrderVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.qiguliuxing.dts.core.util.JacksonUtil;
import com.qiguliuxing.dts.core.util.ResponseUtil;
import com.qiguliuxing.dts.db.service.DtsOrderService;

@Service
public class AdminOrderService {
	private static final Logger logger = LoggerFactory.getLogger(AdminOrderService.class);


	@Autowired
	private DtsOrderService orderService;

	/**
	 * 发货 1. 检测当前订单是否能够发货 2. 设置订单发货状态
	 *
	 * @param body 订单信息，{ orderId：xxx, shipSn: xxx, shipChannel: xxx }
	 * @return 订单操作结果 成功则 { errno: 0, errmsg: '成功' } 失败则 { errno: XXX, errmsg: XXX }
	 */
	public Object ship(String body) {
		String orderNo = JacksonUtil.parseString(body, "orderNo");
		String shipSn = JacksonUtil.parseString(body, "shipSn");
		String shipChannel = JacksonUtil.parseString(body, "shipChannel");
		if (StringUtils.isEmpty(orderNo) || StringUtils.isEmpty(shipSn) || StringUtils.isEmpty(shipChannel)) {
			return ResponseUtil.badArgument();
		}

		OrderVO order = orderService.queryOrderByOrderNo(orderNo);
		if (order == null) {
			return ResponseUtil.badArgument();
		}
		order.setOrderStatus(OrderUtil.SHIPPED);
		order.setShippingChannel(shipChannel);
		order.setTrackingNumber(shipSn);
		orderService.updateShippingInfo(order);
		// TODO 发送邮件和短信通知，这里采用异步发送
		// 发货会发送通知短信给用户: *
		// "您的订单已经发货，快递公司 {1}，快递单 {2} ，请注意查收"
//		notifyService.notifySmsTemplate(order.get(), NotifyType.SHIP, new String[] { shipChannel, shipSn });

		logger.info("【请求结束】商场管理->订单管理->订单发货,响应结果:{}", "成功!");
		return ResponseUtil.ok();
	}



}
