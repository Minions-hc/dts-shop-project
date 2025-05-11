package com.qiguliuxing.dts.wx.web;

import java.util.List;

import com.qiguliuxing.dts.vo.AddressVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSONObject;
import com.qiguliuxing.dts.core.util.ResponseUtil;
import com.qiguliuxing.dts.db.service.DtsAddressService;
import com.qiguliuxing.dts.wx.annotation.LoginUser;
import com.qiguliuxing.dts.wx.service.GetRegionService;

/**
 * 用户收货地址服务
 */
@RestController
@RequestMapping("/wx/address")
@Validated
public class WxAddressController extends GetRegionService {
	private static final Logger logger = LoggerFactory.getLogger(WxAddressController.class);

	@Autowired
	private DtsAddressService addressService;


	/**
	 * 用户收货地址列表
	 *
	 * @param userId
	 *            用户ID
	 * @return 收货地址列表
	 */
	@GetMapping("list")
	public Object list(@LoginUser String userId) {
		logger.info("【请求开始】获取收货地址列表,请求参数,userId：{}", userId);
		if (StringUtils.isEmpty(userId)) {
			return ResponseUtil.unlogin();
		}
		List<AddressVO> addressList = addressService.queryAddressList(userId);
		return ResponseUtil.ok(addressList);
	}



	/**
	 * 添加或更新收货地址
	 *
	 * @param addressVO
	 *            用户收货地址
	 * @return 添加或更新操作结果
	 */
	@PostMapping("save")
	public Object save(@RequestBody AddressVO addressVO) {
		logger.info("【请求开始】添加或更新收货地址,请求参数,address:{}", JSONObject.toJSONString(addressVO));
		if (StringUtils.isEmpty(addressVO.getUserId())) {
			return ResponseUtil.unlogin();
		}

		// 更新时校验地址是否属于该用户
		if (addressVO.getAddressId() != null) {
			AddressVO checkAddress = addressService.findById(addressVO.getAddressId());
			if (checkAddress == null || !checkAddress.getUserId().equals(addressVO.getUserId())) {
				return ResponseUtil.badArgumentValue();
			}
		}
		// 保存或更新地址
		int result = addressService.saveOrUpdate(addressVO);
		if (result == 0) {
			return ResponseUtil.updatedDataFailed();
		}
		logger.info("【请求结束】添加或更新收货地址,响应结果：{}", addressVO.getAddressId());
		return ResponseUtil.ok(addressVO.getAddressId());
	}

	/**
	 * 删除用户地址
	 * @param addressVO
	 * @return 删除结果
	 */
	@PostMapping("/delete")
	public Object delete(@RequestBody AddressVO addressVO) {
		if (StringUtils.isEmpty(addressVO.getUserId())) {
			return ResponseUtil.unlogin();
		}
		// 校验地址是否属于该用户
		AddressVO checkAddress = addressService.findById(addressVO.getAddressId());
		if (checkAddress == null || !checkAddress.getUserId().equals(addressVO.getUserId())) {
			return ResponseUtil.badArgumentValue();
		}

		// 删除地址
		int deleted = addressService.delete(addressVO.getUserId(), addressVO.getAddressId());
		if (deleted == 0) {
			return ResponseUtil.updatedDataFailed();
		}
		return ResponseUtil.ok();
	}
}
