package com.qiguliuxing.dts.db.dao;

import java.util.List;

import com.qiguliuxing.dts.vo.CouponVO;
import org.apache.ibatis.annotations.Param;

public interface DtsCouponMapper {
    int insertCoupon(CouponVO couponVO);
    int deleteCoupon(Integer couponId);
    int updateCoupon(CouponVO couponVO);
    CouponVO selectCouponById(Integer couponId);
    List<CouponVO> selectCouponsByCondition(@Param("couponName") String couponName, @Param("status") Integer status, @Param("redemptionCode") String redemptionCode);
    List<CouponVO> selectExpiredCoupons();
    List<CouponVO> selectValidCoupons();
    CouponVO selectValidCouponByCode(@Param("redemptionCode") String redemptionCode);
}
