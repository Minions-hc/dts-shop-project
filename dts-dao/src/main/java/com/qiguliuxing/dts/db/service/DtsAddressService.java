package com.qiguliuxing.dts.db.service;

import com.github.pagehelper.PageHelper;
import com.qiguliuxing.dts.db.dao.DtsAddressMapper;


import com.qiguliuxing.dts.vo.AddressVO;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DtsAddressService {
	@Resource
	private DtsAddressMapper addressMapper;


	public List<AddressVO> queryAddressList(String userId){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		return addressMapper.queryAddressList(params);
	}


	public AddressVO findById(Integer addressId) {
		return addressMapper.findById(addressId);
	}


	/**
	 * 保存或更新地址
	 * 1. 处理默认地址状态
	 * 2. 处理自提地址状态
	 */
	@Transactional
	public int saveOrUpdate(AddressVO addressVO) {
		// 处理默认地址状态
		if (addressVO.getDefault()) {
			addressMapper.resetDefault(addressVO.getUserId());
		}

		// 处理自提地址状态
		if (addressVO.getPickup()) {
			addressMapper.resetPickup(addressVO.getUserId());
		}

		if (addressVO.getAddressId() == null) {
			// 新增地址
			return addressMapper.add(addressVO);
		} else {
			// 更新地址
			return addressMapper.update(addressVO);
		}
	}

	/**
	 * 删除用户地址
	 * @param userId 用户ID
	 * @param addressId 地址ID
	 * @return 删除影响的行数
	 */
	@Transactional
	public int delete(String userId, Integer addressId) {
		return addressMapper.deleteByUserIdAndId(userId, addressId);
	}


	public List<AddressVO> queryAddressList(String userId, String userName, String receiverName, Integer page, Integer limit) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("userName", userName);
		params.put("receiverName", receiverName);
		PageHelper.startPage(page, limit);
		return addressMapper.queryAddressList(params);
	}

	/**
	 * 获取用户默认收货地址
	 * @param userId 用户ID
	 * @return 默认收货地址信息，没有则返回null
	 */
	public AddressVO getUserDefaultAddress(String userId) {
		return addressMapper.selectDefaultAddressByUserId(userId);
	}
}
