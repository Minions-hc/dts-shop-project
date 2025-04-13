package com.qiguliuxing.dts.db.service;

import com.github.pagehelper.PageHelper;
import com.qiguliuxing.dts.db.dao.DtsAddressMapper;
import com.qiguliuxing.dts.db.domain.DtsAddress;
import com.qiguliuxing.dts.db.domain.DtsAddressExample;

import com.qiguliuxing.dts.vo.AddressVO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DtsAddressService {
	@Resource
	private DtsAddressMapper addressMapper;

	public List<DtsAddress> queryByUid(Integer uid) {
		DtsAddressExample example = new DtsAddressExample();
		example.or().andUserIdEqualTo(uid).andDeletedEqualTo(false);
		return null;
	}

	public DtsAddress findById(Integer id) {
		return addressMapper.selectByPrimaryKey(id);
	}

	public int add(DtsAddress address) {
		address.setAddTime(LocalDateTime.now());
		address.setUpdateTime(LocalDateTime.now());
		return addressMapper.insertSelective(address);
	}

	public int update(DtsAddress address) {
		address.setUpdateTime(LocalDateTime.now());
		return addressMapper.updateByPrimaryKeySelective(address);
	}

	public void delete(Integer id) {
		addressMapper.logicalDeleteByPrimaryKey(id);
	}

	public DtsAddress findDefault(Integer userId) {
		DtsAddressExample example = new DtsAddressExample();
		example.or().andUserIdEqualTo(userId).andIsDefaultEqualTo(true).andDeletedEqualTo(false);
		return addressMapper.selectOneByExample(example);
	}

	/**
	 * 取消用户的默认地址配置
	 * 
	 * @param userId
	 */
	public void resetDefault(Integer userId) {
		DtsAddress address = new DtsAddress();
		address.setIsDefault(false);
		address.setUpdateTime(LocalDateTime.now());
		DtsAddressExample example = new DtsAddressExample();
		example.or().andUserIdEqualTo(userId).andDeletedEqualTo(false).andIsDefaultEqualTo(true);
		addressMapper.updateByExampleSelective(address, example);
	}

	public List<AddressVO> queryAddressList(String userId, String userName, String receiverName, Integer page, Integer limit) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("userName", userName);
		params.put("receiverName", receiverName);
		PageHelper.startPage(page, limit);
		return addressMapper.queryAddressList(params);
	}
}
