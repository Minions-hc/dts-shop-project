package com.qiguliuxing.dts.wx.web;

import static com.qiguliuxing.dts.wx.util.WxResponseCode.AUTH_CAPTCHA_FREQUENCY;
import static com.qiguliuxing.dts.wx.util.WxResponseCode.AUTH_CAPTCHA_UNMATCH;
import static com.qiguliuxing.dts.wx.util.WxResponseCode.AUTH_CAPTCHA_UNSUPPORT;
import static com.qiguliuxing.dts.wx.util.WxResponseCode.AUTH_INVALID_ACCOUNT;
import static com.qiguliuxing.dts.wx.util.WxResponseCode.AUTH_INVALID_MOBILE;
import static com.qiguliuxing.dts.wx.util.WxResponseCode.AUTH_MOBILE_REGISTERED;
import static com.qiguliuxing.dts.wx.util.WxResponseCode.AUTH_MOBILE_UNREGISTERED;
import static com.qiguliuxing.dts.wx.util.WxResponseCode.AUTH_NAME_REGISTERED;
import static com.qiguliuxing.dts.wx.util.WxResponseCode.AUTH_OPENID_BINDED;
import static com.qiguliuxing.dts.wx.util.WxResponseCode.AUTH_OPENID_UNACCESS;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.qiguliuxing.dts.vo.UserVO;
import com.qiguliuxing.dts.wx.service.WxLoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.qiguliuxing.dts.core.consts.CommConsts;
import com.qiguliuxing.dts.core.notify.NotifyService;
import com.qiguliuxing.dts.core.notify.NotifyType;
import com.qiguliuxing.dts.core.type.UserTypeEnum;
import com.qiguliuxing.dts.core.util.CharUtil;
import com.qiguliuxing.dts.core.util.JacksonUtil;
import com.qiguliuxing.dts.core.util.RegexUtil;
import com.qiguliuxing.dts.core.util.ResponseUtil;
import com.qiguliuxing.dts.core.util.bcrypt.BCryptPasswordEncoder;
import com.qiguliuxing.dts.db.domain.DtsUser;
import com.qiguliuxing.dts.db.service.DtsUserService;
import com.qiguliuxing.dts.wx.annotation.LoginUser;
import com.qiguliuxing.dts.wx.dao.UserInfo;
import com.qiguliuxing.dts.wx.dao.UserToken;
import com.qiguliuxing.dts.wx.dao.WxLoginInfo;
import com.qiguliuxing.dts.wx.service.CaptchaCodeManager;
import com.qiguliuxing.dts.wx.service.UserTokenManager;
import com.qiguliuxing.dts.wx.util.IpUtil;
import com.qiguliuxing.dts.wx.util.WxResponseUtil;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;

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
	 * 注销登录
	 *
	 * @param userId
	 * @return
	 */
	@PostMapping("logout")
	public Object logout(@LoginUser Integer userId) {
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
