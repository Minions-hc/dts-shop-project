package com.qiguliuxing.dts.admin.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.qiguliuxing.dts.admin.util.*;
import com.qiguliuxing.dts.db.service.DtsPermissionService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSONObject;
import com.qiguliuxing.dts.core.util.JacksonUtil;
import com.qiguliuxing.dts.core.util.ResponseUtil;
import com.qiguliuxing.dts.db.domain.DtsAdmin;
import com.qiguliuxing.dts.db.service.DtsRoleService;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/admin/auth")
@Validated
public class AdminAuthController {
	private static final Logger logger = LoggerFactory.getLogger(AdminAuthController.class);

	@Autowired
	private DtsRoleService roleService;
	@Autowired
	private DtsPermissionService permissionService;

	// 存储验证码
	private Map<String, String> captchaCache = new ConcurrentHashMap<>();

	@GetMapping("/captchaImage")
	public ResponseEntity<byte[]> getCaptchaImage(@RequestParam String uuid) throws IOException {
		// 生成验证码
		CaptchaUtil.Captcha captcha = CaptchaUtil.generateCaptcha();
		String captchaCode = captcha.getText();

		// 存储验证码（实际项目中建议使用Redis等缓存，设置过期时间）
		captchaCache.put(uuid, captchaCode);

		// 将验证码图片转换为字节数组
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(captcha.getImage(), "png", baos);
		byte[] imageBytes = baos.toByteArray();

		// 设置响应头
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_PNG);
		headers.setCacheControl("no-store, no-cache, must-revalidate");
		headers.setPragma("no-cache");

		return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
	}

	@PostMapping("/login")
	public Object login(@RequestBody String body) {
		logger.info("【请求开始】系统管理->用户登录,请求参数:body:{}", body);

		String username = JacksonUtil.parseString(body, "username");
		String password = JacksonUtil.parseString(body, "password");
		String captchaCode = JacksonUtil.parseString(body, "code");
		String uuid  = JacksonUtil.parseString(body, "uuid");

		// 验证基本参数
		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password) || StringUtils.isEmpty(captchaCode)) {
			return ResponseUtil.badArgument();
		}
		String storedCaptcha = captchaCache.get(uuid);

		if (storedCaptcha == null || !storedCaptcha.equalsIgnoreCase(captchaCode)) {
			logger.error("系统管理->用户登录 错误:验证码错误");
			return AdminResponseUtil.fail(AdminResponseCode.ADMIN_INVALID_CAPTCHA);
		}
		// 验证通过后移除验证码
		captchaCache.remove(uuid);

		Subject currentUser = SecurityUtils.getSubject();
		try {
			currentUser.login(new UsernamePasswordToken(username, password));
		} catch (UnknownAccountException uae) {
			logger.error("系统管理->用户登录 错误:{}", AdminResponseCode.ADMIN_INVALID_ACCOUNT_OR_PASSWORD.desc());
			return AdminResponseUtil.fail(AdminResponseCode.ADMIN_INVALID_ACCOUNT_OR_PASSWORD);
		} catch (LockedAccountException lae) {
			logger.error("系统管理->用户登录 错误:{}", AdminResponseCode.ADMIN_LOCK_ACCOUNT.desc());
			return AdminResponseUtil.fail(AdminResponseCode.ADMIN_LOCK_ACCOUNT);
		} catch (AuthenticationException ae) {
			logger.error("系统管理->用户登录 错误:{}", AdminResponseCode.ADMIN_LOCK_ACCOUNT.desc());
			return AdminResponseUtil.fail(AdminResponseCode.ADMIN_INVALID_AUTH);
		}

		logger.info("【请求结束】系统管理->用户登录,响应结果:{}", JSONObject.toJSONString(currentUser.getSession().getId()));
		return ResponseUtil.ok(currentUser.getSession().getId());
	}

	/*
	 *
	 */
	@RequiresAuthentication
	@PostMapping("/logout")
	public Object login() {
		Subject currentUser = SecurityUtils.getSubject();
		currentUser.logout();

		logger.info("【请求结束】系统管理->用户注销,响应结果:{}", JSONObject.toJSONString(currentUser.getSession().getId()));
		return ResponseUtil.ok();
	}

	@RequiresAuthentication
	@GetMapping("/info")
	public Object info() {
		Subject currentUser = SecurityUtils.getSubject();
		DtsAdmin admin = (DtsAdmin) currentUser.getPrincipal();

		Map<String, Object> data = new HashMap<>();
		data.put("name", admin.getUsername());
		data.put("avatar", admin.getAvatar());

		Integer[] roleIds = admin.getRoleIds();
		Set<String> roles = roleService.queryByIds(roleIds);
		Set<String> permissions = permissionService.queryByRoleIds(roleIds);
		data.put("roles", roles);
		// NOTE
		// 这里需要转换perms结构，因为对于前端而已API形式的权限更容易理解
		data.put("perms", toAPI(permissions));

		logger.info("【请求结束】系统管理->用户信息获取,响应结果:{}", JSONObject.toJSONString(data));
		return ResponseUtil.ok(data);
	}

	@Autowired
	private ApplicationContext context;
	private HashMap<String, String> systemPermissionsMap = null;

	private Collection<String> toAPI(Set<String> permissions) {
		if (systemPermissionsMap == null) {
			systemPermissionsMap = new HashMap<>();
			final String basicPackage = "com.qiguliuxing.dts.admin";
			List<Permission> systemPermissions = PermissionUtil.listPermission(context, basicPackage);
			for (Permission permission : systemPermissions) {
				String perm = permission.getRequiresPermissions().value()[0];
				String api = permission.getApi();
				systemPermissionsMap.put(perm, api);
			}
		}

		Collection<String> apis = new HashSet<>();
		for (String perm : permissions) {
			String api = systemPermissionsMap.get(perm);
			apis.add(api);

			if (perm.equals("*")) {
				apis.clear();
				apis.add("*");
				return apis;
				// return systemPermissionsMap.values();

			}
		}
		return apis;
	}

	@GetMapping("/401")
	public Object page401() {
		return ResponseUtil.unlogin();
	}

	@GetMapping("/index")
	public Object pageIndex() {
		return ResponseUtil.ok();
	}

	@GetMapping("/403")
	public Object page403() {
		return ResponseUtil.unauthz();
	}
}
