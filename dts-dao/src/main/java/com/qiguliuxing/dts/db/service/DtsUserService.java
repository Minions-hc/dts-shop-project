package com.qiguliuxing.dts.db.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import com.qiguliuxing.dts.vo.InvitationRecordVO;
import com.qiguliuxing.dts.vo.UserVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.qiguliuxing.dts.db.dao.DtsUserMapper;

@Service
public class DtsUserService {

	@Resource
	private DtsUserMapper userMapper;

	public List<UserVO> queryUserList(String userId, String userName, String phone, Integer page, Integer size) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("userName", userName);
		params.put("phone", phone);
		PageHelper.startPage(page, size);
		return userMapper.queryUserList(params);
	}

	public List<InvitationRecordVO> getInvitationRecords(String inviterId) {
		return userMapper.selectInvitationRecords(inviterId);
	}

	public int count() {
		return userMapper.countUser();
	}


	/**
	 * 更新用户魂力值
	 * @param userId 用户ID
	 * @param value 变动值（必须为正数）
	 * @param isAdd true=增加, false=扣减
	 * @return 更新后的魂力值
	 */
	@Transactional
	public void updateSpiritPower(String userId, int value, boolean isAdd) {
		if (value <= 0) {
			throw new IllegalArgumentException("变动值必须为正数");
		}
		// 执行更新
		userMapper.updateSpiritPower(userId, value, isAdd);
	}

	public int currentSpiritPower(String userId) {
		return userMapper.selectSpiritPower(userId);
	}

	/**
	 * 处理用户登录逻辑
	 * @param openId 微信openid
	 * @param wxUserInfo 微信用户信息
	 * @return 用户对象
	 */
	@Transactional
	public UserVO handleUserLogin(String openId, JSONObject wxUserInfo) {
		// 1. 检查用户是否已存在
		UserVO user = userMapper.selectByWxOpenId(openId);

		if (user == null) {
			// 2. 新用户注册
			user = new UserVO();
			user.setWxOpenId(openId);
			user.setUserId(generateUniqueUserId()); // 生成唯一用户ID
			user.setUserName(wxUserInfo.getString("nickName"));
			user.setNickName(wxUserInfo.getString("nickName"));
			user.setPhone(wxUserInfo.getString("phoneNumber"));
			user.setAvatar(wxUserInfo.getString("avatarUrl"));
			user.setPoints(0); // 初始积分
			user.setProductSpiritPower(0);
			user.setInviterId(wxUserInfo.getString("inviterId"));
			user.setInviteCode(generateInviteCode()); // 生成邀请码
			user.setCreateBy("system"); // 系统创建

			// 3. 插入新用户
			userMapper.insertUser(user);
		}

		return user;
	}

	/**
	 * 生成唯一的用户ID (U+6位随机数字)
	 * @return 唯一的用户ID
	 */
	private String generateUniqueUserId() {
		String userId;
		do {
			userId = "U" + String.format("%06d", new Random().nextInt(999999));
		} while (userMapper.checkUserIdExists(userId) > 0);

		return userId;
	}

	/**
	 * 生成邀请码 (8位随机字母数字)
	 * @return 邀请码
	 */
	private String generateInviteCode() {
		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		StringBuilder sb = new StringBuilder();
		Random random = new Random();

		for (int i = 0; i < 8; i++) {
			sb.append(chars.charAt(random.nextInt(chars.length())));
		}

		return sb.toString();
	}

	public boolean updateUserInfo(UserVO userVO) {
		return userMapper.updateUserInfo(userVO) > 0;
	}

	/**
	 * 用户注销
	 * @param userId 用户ID
	 * @return
	 */
	public boolean deleteUser(String userId) {
		return userMapper.deleteUser(userId) > 0;
	}

	public UserVO getUserById(String userId) {
		return userMapper.selectUserById(userId);
	}
}
