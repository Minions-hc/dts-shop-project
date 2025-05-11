package com.qiguliuxing.dts.db.service;

import com.github.pagehelper.PageHelper;
import com.qiguliuxing.dts.db.dao.DtsOrderMapper;
import com.qiguliuxing.dts.db.domain.DtsOrder;
import com.qiguliuxing.dts.db.domain.DtsOrderExample;

import com.qiguliuxing.dts.vo.OrderDetailVO;
import com.qiguliuxing.dts.vo.OrderItemVO;
import com.qiguliuxing.dts.vo.OrderVO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DtsOrderService {
	@Resource
	private DtsOrderMapper dtsOrderMapper;

	public int count(Integer userId) {
		DtsOrderExample example = new DtsOrderExample();
		example.or().andUserIdEqualTo(userId).andDeletedEqualTo(false);
		return (int) dtsOrderMapper.countByExample(example);
	}

	public DtsOrder findById(Integer orderId) {
		return dtsOrderMapper.selectByPrimaryKey(orderId);
	}

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


	public List<DtsOrder> querySelective(Integer userId, String orderSn, List<Short> orderStatusArray, Integer page,
										 Integer size, String sort, String order) {
		DtsOrderExample example = new DtsOrderExample();
		DtsOrderExample.Criteria criteria = example.createCriteria();

		if (userId != null) {
			criteria.andUserIdEqualTo(userId);
		}
		if (!StringUtils.isEmpty(orderSn)) {
			criteria.andOrderSnEqualTo(orderSn);
		}
		if (orderStatusArray != null && orderStatusArray.size() != 0) {
			criteria.andOrderStatusIn(orderStatusArray);
		}
		criteria.andDeletedEqualTo(false);

		if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
			example.setOrderByClause(sort + " " + order);
		}

		PageHelper.startPage(page, size);
		return dtsOrderMapper.selectByExample(example);
	}



	public void deleteById(Integer id) {
		dtsOrderMapper.logicalDeleteByPrimaryKey(id);
	}

	public int count() {
		DtsOrderExample example = new DtsOrderExample();
		example.or().andDeletedEqualTo(false);
		return (int) dtsOrderMapper.countByExample(example);
	}

}
