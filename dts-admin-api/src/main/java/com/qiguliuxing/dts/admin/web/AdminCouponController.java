package com.qiguliuxing.dts.admin.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qiguliuxing.dts.vo.CouponVO;
import com.qiguliuxing.dts.vo.UserCouponsVO;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.qiguliuxing.dts.admin.annotation.RequiresPermissionsDesc;
import com.qiguliuxing.dts.core.util.ResponseUtil;
import com.qiguliuxing.dts.core.validator.Order;
import com.qiguliuxing.dts.core.validator.Sort;

import com.qiguliuxing.dts.db.service.DtsCouponService;
import com.qiguliuxing.dts.db.service.DtsCouponUserService;


@RestController
@RequestMapping("/admin/coupon")
@Validated
public class AdminCouponController {
	private static final Logger logger = LoggerFactory.getLogger(AdminCouponController.class);

	@Autowired
	private DtsCouponService couponService;
	@Autowired
	private DtsCouponUserService couponUserService;

	@RequiresPermissions("admin:coupon:list")
	@RequiresPermissionsDesc(menu = { "推广管理", "优惠券管理" }, button = "查询")
	@GetMapping("/list")
	public Object list(String couponName, Integer status, String redemptionCode) {
		logger.info("【请求开始】推广管理->优惠券管理->查询,请求参数:name:{},page:{}", couponName);

		List<CouponVO> couponList = couponService.selectCouponsByCondition(couponName, status, redemptionCode);
		long total = PageInfo.of(couponList).getTotal();
		Map<String, Object> data = new HashMap<>();
		data.put("total", total);
		data.put("items", couponList);

		logger.info("【请求结束】推广管理->优惠券管理->查询:响应结果:{}", "成功!");
		return ResponseUtil.ok(data);
	}

	@RequiresPermissions("admin:coupon:listuser")
	@RequiresPermissionsDesc(menu = { "推广管理", "优惠券管理" }, button = "查询用户")
	@GetMapping("/listuser")
	public Object listuser(String userId, Integer couponId, Integer status,
			@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit,
			@Sort @RequestParam(defaultValue = "add_time") String sort,
			@Order @RequestParam(defaultValue = "desc") String order) {
		logger.info("【请求开始】推广管理->优惠券管理->查询用户,请求参数:userId:{},couponId:{}", userId, couponId);

		List<UserCouponsVO> couponList = couponUserService.selectUserCouponsByCondition(userId, couponId, status);
		long total = PageInfo.of(couponList).getTotal();
		Map<String, Object> data = new HashMap<>();
		data.put("total", total);
		data.put("items", couponList);

		logger.info("【请求结束】推广管理->优惠券管理->查询用户:响应结果:{}", JSONObject.toJSONString(data));
		return ResponseUtil.ok(data);
	}

	@RequiresPermissions("admin:coupon:read")
	@RequiresPermissionsDesc(menu = { "推广管理", "优惠券管理" }, button = "详情")
	@GetMapping("/read")
	public Object read(Integer couponId) {
		logger.info("【请求开始】推广管理->优惠券管理->详情,请求参数,id:{}", couponId);

		CouponVO coupon = couponService.getCouponById(couponId);

		logger.info("【请求结束】推广管理->优惠券管理->详情,响应结果:{}", JSONObject.toJSONString(coupon));
		return ResponseUtil.ok(coupon);
	}

	@RequiresPermissions("admin:coupon:create")
	@RequiresPermissionsDesc(menu = { "推广管理", "优惠券管理" }, button = "添加")
	@PostMapping("/create")
	public Object create(@RequestBody CouponVO coupon) {
		logger.info("【请求开始】推广管理->优惠券管理->添加,请求参数:{}", JSONObject.toJSONString(coupon));

		if (StringUtils.isEmpty(coupon.getCouponName())) {
			return ResponseUtil.badArgument();
		}
		couponService.createCoupon(coupon);
		logger.info("【请求结束】推广管理->优惠券管理->添加,响应结果:{}", JSONObject.toJSONString(coupon));
		return ResponseUtil.ok(coupon);
	}

	@RequiresPermissions("admin:coupon:update")
	@RequiresPermissionsDesc(menu = { "推广管理", "优惠券管理" }, button = "编辑")
	@PostMapping("/update")
	public Object update(@RequestBody CouponVO coupon) {
		logger.info("【请求开始】推广管理->优惠券管理->编辑,请求参数:{}", JSONObject.toJSONString(coupon));

		if (StringUtils.isEmpty(coupon.getCouponName())) {
			return ResponseUtil.badArgument();
		}

		if (couponService.updateCoupon(coupon) == 0) {
			return ResponseUtil.updatedDataFailed();
		}

		logger.info("【请求结束】推广管理->优惠券管理->编辑,响应结果:{}", JSONObject.toJSONString(coupon));
		return ResponseUtil.ok(coupon);
	}

	@RequiresPermissions("admin:coupon:delete")
	@RequiresPermissionsDesc(menu = { "推广管理", "优惠券管理" }, button = "删除")
	@PostMapping("/delete")
	public Object delete(@RequestBody CouponVO coupon) {
		logger.info("【请求开始】推广管理->优惠券管理->删除,请求参数:{}", JSONObject.toJSONString(coupon));

		if (couponService.deleteCoupon(coupon.getCouponId()) == 0) {
			return ResponseUtil.updatedDataFailed();
		}

		logger.info("【请求结束】推广管理->优惠券管理->删除,响应结果:{}", "成功!");
		return ResponseUtil.ok();
	}

}
