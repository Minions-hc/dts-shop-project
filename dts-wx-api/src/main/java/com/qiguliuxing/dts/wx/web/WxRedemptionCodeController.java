package com.qiguliuxing.dts.wx.web;


import com.qiguliuxing.dts.core.util.JacksonUtil;
import com.qiguliuxing.dts.core.util.ResponseUtil;
import com.qiguliuxing.dts.db.service.*;
import com.qiguliuxing.dts.db.util.*;
import com.qiguliuxing.dts.vo.*;
import com.qiguliuxing.dts.wx.annotation.LoginUser;
import com.qiguliuxing.dts.wx.util.WxResponseCode;
import com.qiguliuxing.dts.wx.util.WxResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 优惠券服务
 */
@RestController
@RequestMapping("/wx/redemptionCode")
@Validated
public class WxRedemptionCodeController {

    private static final Logger logger = LoggerFactory.getLogger(WxRedemptionCodeController.class);

    @Autowired
    private RedemptionCodeService redemptionCodeService;

    @Autowired
    private DtsCouponService couponService;

    @Autowired
    private DtsCouponUserService couponUserService;

    @Autowired
    private LuckyDrawPrizeService luckyDrawPrizeService;

    @Autowired
    private BoxProductService boxProductService;

    @Autowired
    private ProductService productService;

    /**
     * 优惠券兑换
     *
     * @param body
     *            请求内容， { code: xxx }
     * @return 操作结果
     */
    @PostMapping("exchange")
    public Object exchange(@RequestBody String body) {
        logger.info("【请求开始】优惠券兑换，请求参数：userId:{},Body:{}", body);

        String userId = JacksonUtil.parseString(body, "userId");

        if (userId == null) {
            logger.error("优惠券兑换:用户未登录！！！");
            return ResponseUtil.unlogin();
        }

        String redemptionCode = JacksonUtil.parseString(body, "redemptionCode");
        if (redemptionCode == null) {
            return ResponseUtil.badArgument();
        }
        RedemptionCodeVo redemptionCodeVo = redemptionCodeService.getRedemptionCode(redemptionCode);

        if (redemptionCodeVo == null) {
            logger.error("兑换码兑换出错:{}", WxResponseCode.COUPON_CODE_INVALID.desc());
            return WxResponseUtil.fail(WxResponseCode.COUPON_CODE_INVALID);
        }

        // 未分配兑换码兑换
        if (RedemptionCodeType.UNASSIGNED.getValue().equals(redemptionCodeVo.getCodeType())){
            logger.error("兑换码兑换出错:{}", WxResponseCode.COUPON_NOT_CHANGE.desc());
            return WxResponseUtil.fail(WxResponseCode.COUPON_NOT_CHANGE);
        }

        // 无效兑换码
        if (!redemptionCodeVo.getAvailable()){
            logger.error("兑换码兑换出错:{}", WxResponseCode.COUPON_NOT_CHANGE.desc());
            return WxResponseUtil.fail(WxResponseCode.COUPON_NOT_CHANGE);
        }

        // 优惠券兑换
        if (RedemptionCodeType.COUPON.getValue().equals(redemptionCodeVo.getCodeType())){
            CouponVO coupon = couponService.selectValidCouponByCode(redemptionCode);
            Integer couponId = coupon.getCouponId();
            if (coupon.getRemainingQuantity().equals(0)) {
                logger.error("优惠券兑换出错:{}", WxResponseCode.COUPON_EXCEED_LIMIT.desc());
                return WxResponseUtil.fail(WxResponseCode.COUPON_EXCEED_LIMIT);
            }
            // 优惠券状态，已下架或者过期不能领取
            Integer status = coupon.getStatus();
            if (status.equals(CouponStatus.INVALID.getCode())) {
                logger.error("优惠券兑换出错:{}", WxResponseCode.COUPON_EXCEED_LIMIT.desc());
                return WxResponseUtil.fail(WxResponseCode.COUPON_EXCEED_LIMIT);
            } else if (status.equals(CouponStatus.EXPIRED.getCode())) {
                logger.error("优惠券兑换出错:{}", WxResponseCode.COUPON_EXPIRED.desc());
                return WxResponseUtil.fail(WxResponseCode.COUPON_EXPIRED);
            }
            UserCouponsVO userCouponsVO = new UserCouponsVO();
            userCouponsVO.setCouponId(couponId);
            userCouponsVO.setStatus(UserCouponStatus.UN_USEED.getCode());
            userCouponsVO.setCouponAmount(coupon.getCouponAmount());
            userCouponsVO.setCouponName(coupon.getCouponName());
            userCouponsVO.setUserId(userId);
            userCouponsVO.setExpireTime(coupon.getExpireTime());
            userCouponsVO.setCouponType(coupon.getCouponType());
            userCouponsVO.setMinOrderAmount(coupon.getMinOrderAmount());

            // 录入用户优惠券记录
            couponUserService.addUserCoupon(userCouponsVO, coupon);
            // 更新兑换码表的状态
            redemptionCodeService.updateRedemptionCodeStatus(redemptionCode, false);
        }

        // 奖品兑换
        if (RedemptionCodeType.PRIZE.getValue().equals(redemptionCodeVo.getCodeType())){
            LuckyDrawPrizeVo prizeByRedemptionCode = luckyDrawPrizeService.getPrizeByRedemptionCode(redemptionCode);
            ProductVO product = productService.getProductById(prizeByRedemptionCode.getProductId());
            BoxProductVO boxProductVO = new BoxProductVO();
            boxProductVO.setActivityType(ActivityType.LUCKY_DRAW.getName());
            boxProductVO.setProductId(prizeByRedemptionCode.getProductId());
            boxProductVO.setProductName(prizeByRedemptionCode.getProductName());
            boxProductVO.setProductImage(product.getProductImage());
            boxProductVO.setProductLevel(product.getProductLevelName());
            boxProductVO.setStatus(StatusType.PENDING.getCode());
            boxProductVO.setObtainTime(new Date());
            boxProductVO.setCreatedTime(new Date());
            boxProductVO.setUpdatedTime(new Date());
            // 兑换奖品写合柜表
            boxProductService.addProduct(boxProductVO);
            // 更新兑换码表的状态
            redemptionCodeService.updateRedemptionCodeStatus(redemptionCode, false);
        }

        logger.info("【请求结束】优惠券兑换成功!");
        return ResponseUtil.ok();
    }

}
