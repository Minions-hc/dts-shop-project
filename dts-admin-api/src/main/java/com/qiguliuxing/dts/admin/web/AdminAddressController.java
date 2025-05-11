package com.qiguliuxing.dts.admin.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qiguliuxing.dts.vo.AddressVO;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.qiguliuxing.dts.admin.annotation.RequiresPermissionsDesc;
import com.qiguliuxing.dts.core.util.ResponseUtil;
import com.qiguliuxing.dts.db.service.DtsAddressService;

@RestController
@RequestMapping("/admin/address")
@Validated
public class AdminAddressController {
	private static final Logger logger = LoggerFactory.getLogger(AdminAddressController.class);

	@Autowired
	private DtsAddressService addressService;

	@RequiresPermissions("admin:address:list")
	@RequiresPermissionsDesc(menu = { "用户管理", "收货地址" }, button = "查询")
	@GetMapping("/queryAddressList")
	public Object list(String userId, String userName, String receiverName, @RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "10") Integer limit) {
		logger.info("【请求开始】用户管理->收货地址->查询,请求参数:userName:{},userId:{},page:{}", userName, userId, page);

		List<AddressVO> addressList = addressService.queryAddressList(userId, userName, receiverName, page, limit);
		long total = PageInfo.of(addressList).getTotal();

		Map<String, Object> data = new HashMap<>();
		data.put("total", total);
		data.put("items", addressList);

		logger.info("【请求结束】用户管理->收货地址->查询,响应结果:{}", JSONObject.toJSONString(data));
		return ResponseUtil.ok(data);
	}
}
