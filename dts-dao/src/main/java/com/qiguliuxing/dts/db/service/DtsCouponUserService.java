package com.qiguliuxing.dts.db.service;

import com.qiguliuxing.dts.db.dao.DtsCouponMapper;
import com.qiguliuxing.dts.db.dao.DtsCouponUserMapper;

import com.qiguliuxing.dts.vo.CouponVO;
import com.qiguliuxing.dts.vo.UserCouponsVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DtsCouponUserService {
	@Resource
	private DtsCouponUserMapper couponUserMapper;

	@Resource
	private DtsCouponMapper couponMapper;

	public List<UserCouponsVO> selectUserCouponsByCondition( String userId, Integer couponId, Integer status){
		return couponUserMapper.selectUserCouponsByCondition(userId, couponId, status);
	}

	@Transactional
	public int addUserCoupon(UserCouponsVO userCoupon, CouponVO couponVO) {
		int result = couponUserMapper.insertUserCoupon(userCoupon);
		if ( result > 0 ){
			couponVO.setRemainingQuantity(couponVO.getRemainingQuantity() - 1);
			return couponMapper.updateCoupon(couponVO);
		}
		return 0;
	}

	/**
	 * 获取用户所有优惠券
	 * @param userId 用户ID
	 * @return 优惠券列表
	 */
	public List<UserCouponsVO> getUserCoupons(String userId) {
		return couponUserMapper.selectByUserId(userId);
	}

	/**
	 * 查询用户可用优惠券
	 * @param userId 用户ID
	 * @param orderAmount 订单金额(用于满减券判断)
	 * @return 可用优惠券列表
	 */
	public List<UserCouponsVO> getAvailableCoupons(String userId, BigDecimal orderAmount) {
		if (orderAmount == null) {
			orderAmount = BigDecimal.ZERO; // 如果没有订单金额，默认0，只返回无门槛券
		}
		return couponUserMapper.selectAvailableCoupons(userId, orderAmount);
	}

	/**
	 * 更新优惠券使用状态
	 * @param couponId 优惠券ID
	 * @param userId 用户ID
	 * @param orderId 订单ID
	 * @return 更新的记录数
	 */
	public int updateCouponStatusToUsed(Integer couponId, String userId,  String orderId){
		return couponUserMapper.updateCouponStatusToUsed(couponId, userId, orderId);
	}
}
