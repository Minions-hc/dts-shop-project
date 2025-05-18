package com.qiguliuxing.dts.db.service;

import com.github.pagehelper.PageHelper;
import com.qiguliuxing.dts.db.dao.DtsOrderMapper;

import com.qiguliuxing.dts.vo.OrderDetailVO;
import com.qiguliuxing.dts.vo.OrderItemVO;
import com.qiguliuxing.dts.vo.OrderVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DtsOrderService {
	@Resource
	private DtsOrderMapper dtsOrderMapper;


	public List<OrderVO> queryOrderList(String userId, String orderNo, List<String> orderStatusList, Integer page,
										Integer size) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("orderNo", orderNo);
		params.put("orderStatusList", orderStatusList);
		PageHelper.startPage(page, size);
		return dtsOrderMapper.queryOrderList(params);
	}

	public List<OrderVO> queryOrderList(String userId, String orderNo, List<String> orderStatusList) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("orderNo", orderNo);
		params.put("orderStatusList", orderStatusList);
		return dtsOrderMapper.queryOrderList(params);
	}

	public OrderVO queryOrderByOrderNo(String orderNo) {
		return dtsOrderMapper.queryOrderByOrderNo(orderNo);
	}

	public void updateShippingInfo(OrderVO order){
		dtsOrderMapper.updateShippingInfo(order);
	}

	public OrderDetailVO queryOrderDetail(String orderNo) {
		OrderDetailVO orderDetail = dtsOrderMapper.queryOrderDetail(orderNo);
		if (orderDetail == null) {
			return null;
		}
		Integer orderId = orderDetail.getOrderId();
		List<OrderItemVO> orderItems = dtsOrderMapper.queryOrderItems(orderId);
		orderDetail.setOrderItems(orderItems);
		return orderDetail;
	}

	@Transactional
	public int insertOrder(OrderVO order){
		return dtsOrderMapper.insertOrder(order);
	}
}
