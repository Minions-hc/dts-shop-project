package com.qiguliuxing.dts.db.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.qiguliuxing.dts.vo.InvitationRecordVO;
import com.qiguliuxing.dts.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.github.pagehelper.PageHelper;
import com.qiguliuxing.dts.db.dao.DtsUserAccountMapper;
import com.qiguliuxing.dts.db.dao.DtsUserMapper;
import com.qiguliuxing.dts.db.domain.DtsUser;
import com.qiguliuxing.dts.db.domain.DtsUserAccount;
import com.qiguliuxing.dts.db.domain.DtsUserAccountExample;
import com.qiguliuxing.dts.db.domain.DtsUserExample;
import com.qiguliuxing.dts.db.domain.UserVo;

@Service
public class DtsUserService {

	@Resource
	private DtsUserMapper userMapper;

	@Resource
	private DtsUserAccountMapper userAccountMapper;
    @Autowired
    private DtsUserMapper dtsUserMapper;

	public DtsUser findById(Integer userId) {
		return userMapper.selectByPrimaryKey(userId);
	}

	public UserVo findUserVoById(Integer userId) {
		DtsUser user = findById(userId);
		UserVo userVo = new UserVo();
		userVo.setNickname(user.getNickname());
		userVo.setAvatar(user.getAvatar());
		return userVo;
	}

	public DtsUser queryByOid(String openId) {
		DtsUserExample example = new DtsUserExample();
		example.or().andWeixinOpenidEqualTo(openId).andDeletedEqualTo(false);
		return userMapper.selectOneByExample(example);
	}

	public void add(DtsUser user) {
		user.setAddTime(LocalDateTime.now());
		user.setUpdateTime(LocalDateTime.now());
		userMapper.insertSelective(user);
	}

	public int updateById(DtsUser user) {
		user.setUpdateTime(LocalDateTime.now());
		return userMapper.updateByPrimaryKeySelective(user);
	}

	public List<DtsUser> querySelective(String username, String mobile, Integer page, Integer size, String sort,
			String order) {
		DtsUserExample example = new DtsUserExample();
		DtsUserExample.Criteria criteria = example.createCriteria();

		if (!StringUtils.isEmpty(username)) {
			criteria.andUsernameLike("%" + username + "%");
		}
		if (!StringUtils.isEmpty(mobile)) {
			criteria.andMobileEqualTo(mobile);
		}
		criteria.andDeletedEqualTo(false);

		if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
			example.setOrderByClause(sort + " " + order);
		}

		PageHelper.startPage(page, size);
		return userMapper.selectByExample(example);
	}


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

	public List<DtsUser> queryByUsername(String username) {
		DtsUserExample example = new DtsUserExample();
		example.or().andUsernameEqualTo(username).andDeletedEqualTo(false);
		return userMapper.selectByExample(example);
	}

	public List<DtsUser> queryByMobile(String mobile) {
		DtsUserExample example = new DtsUserExample();
		example.or().andMobileEqualTo(mobile).andDeletedEqualTo(false);
		return userMapper.selectByExample(example);
	}

	public List<DtsUser> queryByOpenid(String openid) {
		DtsUserExample example = new DtsUserExample();
		example.or().andWeixinOpenidEqualTo(openid).andDeletedEqualTo(false);
		return userMapper.selectByExample(example);
	}

	public void deleteById(Integer id) {
		userMapper.logicalDeleteByPrimaryKey(id);
	}

	/**
	 * 审批代理申请
	 * @param userAccount
	 */
	public void approveAgency(Integer userId,Integer settlementRate,String shareUrl) {
		//获取账户数据
		DtsUserAccountExample example = new DtsUserAccountExample();
		example.or().andUserIdEqualTo(userId);

		DtsUserAccount dbAccount = userAccountMapper.selectOneByExample(example);
		if (dbAccount == null) {
			throw new RuntimeException("申请账户不存在");
		}
		dbAccount.setShareUrl(shareUrl);
		if (!StringUtils.isEmpty(settlementRate)) {
			dbAccount.setSettlementRate(settlementRate);
		}
		dbAccount.setModifyTime(LocalDateTime.now());
		userAccountMapper.updateByPrimaryKey(dbAccount);

		//更新会员状态和类型
		DtsUser user = findById(userId);
		user.setUserLevel((byte) 2);//区域代理用户
		user.setStatus((byte) 0);//正常状态
		updateById(user);
	}

	public DtsUserAccount detailApproveByUserId(Integer userId) {
		// 获取账户数据
		DtsUserAccountExample example = new DtsUserAccountExample();
		example.or().andUserIdEqualTo(userId);

		DtsUserAccount dbAccount = userAccountMapper.selectOneByExample(example);
		return dbAccount;
	}


	/**
	 * 用户注册
	 * @return 注册成功的用户ID
	 */
	@Transactional
	public String register(String wxOpenId, String inviterId, String userName, String avatar, String phone) {

		// 1. 检查微信用户是否已注册
		if (dtsUserMapper.existsByWxOpenId(wxOpenId)) {
			throw new RuntimeException("该微信用户已注册");
		}
		// 2. 获取下一个用户ID
		String userId = dtsUserMapper.getNextUserId();
		if (userId == null) {
			userId = "U1000"; // 第一个用户
		}
		// 4. 构建用户实体
		UserVO user = new UserVO();
		user.setWxOpenId(wxOpenId);
		user.setUserId(userId);
		user.setUserName(userName);
		user.setAvatar(avatar);
		user.setPoints(0); // 初始积分
		user.setInviterId(inviterId);
		user.setPhone(phone);
		user.setCreateBy("system"); // 系统创建
		// 5. 保存用户
		int result = dtsUserMapper.insertUser(user);
		if (result <= 0) {
			throw new RuntimeException("用户注册失败");
		}
		return user.getUserId();
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
}
