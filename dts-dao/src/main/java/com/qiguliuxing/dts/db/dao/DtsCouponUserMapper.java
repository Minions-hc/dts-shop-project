package com.qiguliuxing.dts.db.dao;

import com.qiguliuxing.dts.db.domain.DtsCouponUser;
import com.qiguliuxing.dts.db.domain.DtsCouponUserExample;

import java.math.BigDecimal;
import java.util.List;

import com.qiguliuxing.dts.vo.UserCouponsVO;
import org.apache.ibatis.annotations.Param;

public interface DtsCouponUserMapper {
    List<UserCouponsVO> selectUserCouponsByCondition(
            @Param("userId") String userId,
            @Param("couponId") Integer couponId,
            @Param("status") Integer status);


    /**
     * 新增用户优惠券记录
     * @param userCoupon 用户优惠券信息
     * @return 影响的行数
     */
    int insertUserCoupon(UserCouponsVO userCoupon);


    /**
     * 根据用户ID查询用户优惠券
     * @param userId 用户ID
     * @return 用户优惠券列表
     */
    List<UserCouponsVO> selectByUserId(@Param("userId") String userId);


    /**
     * 查询用户可用优惠券
     * @param userId 用户ID
     * @param orderAmount 订单金额(用于满减券判断)
     * @return 可用优惠券列表
     */
    List<UserCouponsVO> selectAvailableCoupons(
            @Param("userId") String userId,
            @Param("orderAmount") BigDecimal orderAmount);
}
