package com.qiguliuxing.dts.db.service;

import com.qiguliuxing.dts.db.dao.DtsCouponMapper;
import com.qiguliuxing.dts.db.dao.DtsCouponUserMapper;

import com.qiguliuxing.dts.vo.CouponVO;
import com.qiguliuxing.dts.vo.UserCouponsVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;

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
}
