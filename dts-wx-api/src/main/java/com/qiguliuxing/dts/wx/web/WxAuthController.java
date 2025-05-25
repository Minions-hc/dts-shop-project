package com.qiguliuxing.dts.wx.web;

import com.qiguliuxing.dts.vo.UserVO;
import com.qiguliuxing.dts.wx.service.WxLoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSONObject;
import com.qiguliuxing.dts.core.util.ResponseUtil;
import com.qiguliuxing.dts.db.service.DtsUserService;
import com.qiguliuxing.dts.wx.annotation.LoginUser;
import com.qiguliuxing.dts.wx.dao.UserToken;
import com.qiguliuxing.dts.wx.service.UserTokenManager;

/**
 * 鉴权服务
 */
@RestController
@RequestMapping("/wx/auth")
@Validated
public class WxAuthController {
	private static final Logger logger = LoggerFactory.getLogger(WxAuthController.class);

	@Autowired
	private DtsUserService userService;

	@Autowired
	private WxLoginService wxLoginService;

	/**
	 * 更新用户信息
	 * @param params
	 * @return 更新结果
	 */
	@PostMapping("/update")
	public Object updateUserInfo(@RequestBody JSONObject params) {
		String userId = params.getString("userId");
		String userName = params.getString("userName");
		String phone = params.getString("phone");
		String avatar = params.getString("avatar");
		UserVO userVO = new UserVO();
		userVO.setUserId(userId);
		userVO.setUserName(userName);
		userVO.setPhone(phone);
		userVO.setAvatar(avatar);
		userVO.setUpdateBy(userId);
		boolean success = userService.updateUserInfo(userVO);
		if (success) {
			return ResponseUtil.ok();
		} else {
			return ResponseUtil.fail();
		}
	}


	/**
	 * 用户注销接口
	 * @param userId 用户ID
	 * @return 注销结果
	 */
	@PostMapping("/delete/{userId}")
	public Object deleteUser(@PathVariable String userId) {
		boolean success = userService.deleteUser(userId);
		if (success) {
			return ResponseUtil.ok();
		} else {
			return ResponseUtil.fail();
		}
	}

	/**
	 * 注销登录
	 *
	 * @param params
	 * @return
	 */
	@PostMapping("/logout")
	public Object logout(@RequestBody JSONObject params) {
		String userId = params.getString("userId");
		logger.info("【请求开始】注销登录,请求参数，userId:{}", userId);
		if (userId == null) {
			return ResponseUtil.unlogin();
		}
		try {
			UserTokenManager.removeToken(userId);
		} catch (Exception e) {
			logger.error("注销登录出错：userId:{}", userId);
			e.printStackTrace();
			return ResponseUtil.fail();
		}
		logger.info("【请求结束】注销登录成功!");
		return ResponseUtil.ok();
	}



	/**
	 * 微信小程序登录接口
	 * @param params 包含code和加密数据的JSON对象
	 * @return 登录结果
	 */
	@PostMapping("/wxLogin")
	public JSONObject wxLogin(@RequestBody JSONObject params) {
		JSONObject result = new JSONObject();

		try {
			// 1. 获取code
			String code = params.getString("code");

			// 2. 调用code2session获取openid和session_key
			JSONObject sessionInfo = wxLoginService.code2Session(code);
			String openId = sessionInfo.getString("openid");
			String sessionKey = sessionInfo.getString("session_key");

			JSONObject userInfo = null;
			// 3. 如果需要解密用户信息
			if (params.containsKey("encryptedData") && params.containsKey("iv")) {
				String encryptedData = params.getString("encryptedData");
				String iv = params.getString("iv");
				userInfo = wxLoginService.decryptUserInfo(encryptedData, sessionKey, iv);
			}
			if (userInfo == null) {
				result.put("code", 500);
				result.put("message", "用户信息解析失败");
				return result;
			}
			String phoneNumber = null;
			// 4. 处理手机号（如果存在）
			if (params.containsKey("phoneEncryptedData") && params.containsKey("phoneIv")) {
				JSONObject phoneResult = wxLoginService.decryptUserInfo(
						params.getString("phoneEncryptedData"),
						sessionKey,
						params.getString("phoneIv")
				);
				phoneNumber = phoneResult.getString("purePhoneNumber");
				result.put("phoneNumber", phoneNumber);
			}
			userInfo.put("phoneNumber", phoneNumber);
			// 处理用户信息入库
			UserVO user = userService.handleUserLogin(openId, userInfo);
			result.put("userInfo", user);

			// 4. 生成自定义登录态（实际项目中应该生成token）
			UserToken userToken = UserTokenManager.generateToken(user.getUserId());
			userToken.setSessionKey(sessionKey);

			// 5. 返回结果
			result.put("code", 200);
			result.put("message", "登录成功");
			result.put("token", userToken.getToken());
			result.put("sessionKey", sessionKey);
			result.put("openId", openId);
		} catch (Exception e) {
			result.put("code", 500);
			result.put("message", "登录失败: " + e.getMessage());
		}
		return result;
	}



}
