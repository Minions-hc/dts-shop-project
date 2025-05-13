package com.qiguliuxing.dts.db.service;

import java.util.List;

import javax.annotation.Resource;

import com.qiguliuxing.dts.db.util.RedemptionCodeType;
import com.qiguliuxing.dts.vo.CouponVO;
import org.springframework.stereotype.Service;

import com.qiguliuxing.dts.db.dao.DtsCouponMapper;
import com.qiguliuxing.dts.db.dao.DtsCouponUserMapper;

@Service
public class DtsCouponService {
	@Resource
	private DtsCouponMapper couponMapper;
	@Resource
	private DtsCouponUserMapper couponUserMapper;
	@Resource
	private RedemptionCodeService redemptionCodeService;

	public int createCoupon(CouponVO couponVO) {
		// 更新兑换码类型为优惠券
		redemptionCodeService.updateRedemptionCodeType(couponVO.getRedemptionCode(), RedemptionCodeType.COUPON.getValue());
		return couponMapper.insertCoupon(couponVO);
	}

	public int deleteCoupon(Integer couponId) {
		CouponVO couponVO = couponMapper.selectCouponById(couponId);
		// 更新兑换码类型为未分配
		redemptionCodeService.updateRedemptionCodeType(couponVO.getRedemptionCode(), RedemptionCodeType.UNASSIGNED.getValue());
		return couponMapper.deleteCoupon(couponId);
	}

	public int updateCoupon(CouponVO couponVO) {
		return couponMapper.updateCoupon(couponVO);
	}

	public CouponVO getCouponById(Integer couponId) {
		return couponMapper.selectCouponById(couponId);
	}

	public List<CouponVO> selectCouponsByCondition(String couponName, Integer status, String redemptionCode) {
		return couponMapper.selectCouponsByCondition(couponName, status, redemptionCode);
	}

	public List<CouponVO> selectExpiredCoupons(){
		return couponMapper.selectExpiredCoupons();
	}

	public List<CouponVO> getValidCoupons() {
		return couponMapper.selectValidCoupons();
	}

	public CouponVO selectValidCouponByCode(String redemptionCode){
		return couponMapper.selectValidCouponByCode(redemptionCode);
	}
}
