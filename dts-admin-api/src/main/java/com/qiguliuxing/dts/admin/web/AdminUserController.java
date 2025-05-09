package com.qiguliuxing.dts.admin.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import com.qiguliuxing.dts.vo.UserVO;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.qiguliuxing.dts.core.qcode.QCodeService;
import com.qiguliuxing.dts.core.util.JacksonUtil;
import com.qiguliuxing.dts.core.util.ResponseUtil;
import com.qiguliuxing.dts.core.validator.Order;
import com.qiguliuxing.dts.core.validator.Sort;
import com.qiguliuxing.dts.db.domain.DtsUser;
import com.qiguliuxing.dts.db.domain.DtsUserAccount;
import com.qiguliuxing.dts.db.service.DtsUserService;

@RestController
@RequestMapping("/admin/user")
@Validated
public class AdminUserController {
	private static final Logger logger = LoggerFactory.getLogger(AdminUserController.class);

	@Autowired
	private DtsUserService userService;

	@RequiresPermissions("admin:user:list")
	@RequiresPermissionsDesc(menu = { "用户管理", "会员管理" }, button = "查询")
	@GetMapping("/queryUserList")
	public Object queryUserList(String userId, String userName, String phone, @RequestParam(defaultValue = "1") Integer page,
					   @RequestParam(defaultValue = "10") Integer size) {
		logger.info("【请求开始】用户管理->会员管理->查询,请求参数,userId:{}, userName:{}, phone:{}, page:{}", userId, userName, phone, page);

		List<UserVO> userList = userService.queryUserList(userId, userName, phone, page, size);
		long total = PageInfo.of(userList).getTotal();
		Map<String, Object> data = new HashMap<>();
		data.put("total", total);
		data.put("items", userList);

		logger.info("【请求结束】用户管理->会员管理->查询:响应结果:{}", JSONObject.toJSONString(data));
		return ResponseUtil.ok(data);
	}

}
