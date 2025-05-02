package com.qiguliuxing.dts.wx.web;

import java.util.*;
import java.util.stream.Collectors;

import com.qiguliuxing.dts.db.util.UserCouponStatus;
import com.qiguliuxing.dts.vo.UserCouponsVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.qiguliuxing.dts.core.util.ResponseUtil;
import com.qiguliuxing.dts.db.service.DtsCouponUserService;
import com.qiguliuxing.dts.wx.annotation.LoginUser;

/**
 * 优惠券服务
 */
@RestController
@RequestMapping("/wx/coupon")
@Validated
public class WxCouponController {
	private static final Logger logger = LoggerFactory.getLogger(WxCouponController.class);

	@Autowired
	private DtsCouponUserService couponUserService;

	/**
	 * 个人优惠券列表
	 *
	 * @param userId
	 * @return
	 */
	@GetMapping("mylist")
	public Object mylist(@LoginUser String userId) {
		logger.info("【请求开始】个人优惠券列表,请求参数,userId:{}", userId);
		if (userId == null) {
			logger.error("个人优惠券列表失败:用户未登录！！！");
			return ResponseUtil.unlogin();
		}
		List<UserCouponsVO> couponUserList = couponUserService.getUserCoupons(userId);
		List<UserCouponsVO> unUseedCoupons = couponUserList.stream().filter(userCouponsVO -> userCouponsVO.getStatus().equals(UserCouponStatus.UN_USEED.getCode())).collect(Collectors.toList());
		List<UserCouponsVO> useedCoupons = couponUserList.stream().filter(userCouponsVO -> userCouponsVO.getStatus().equals(UserCouponStatus.USEED.getCode())).collect(Collectors.toList());
		List<UserCouponsVO> expiredCoupons = couponUserList.stream().filter(userCouponsVO -> userCouponsVO.getExpireTime().after(new Date())).collect(Collectors.toList());
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("couponUserList", couponUserList);
		data.put("unUseedCoupons", unUseedCoupons);
		data.put("useedCoupons", useedCoupons);
		data.put("expiredCoupons", expiredCoupons);

		logger.info("【请求结束】个人优惠券列表,响应内容:{}", JSONObject.toJSONString(data));
		return ResponseUtil.ok(data);
	}

}
