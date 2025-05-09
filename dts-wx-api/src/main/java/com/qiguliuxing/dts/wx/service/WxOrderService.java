package com.qiguliuxing.dts.wx.service;

import static com.qiguliuxing.dts.wx.util.WxResponseCode.AUTH_OPENID_UNACCESS;
import static com.qiguliuxing.dts.wx.util.WxResponseCode.GROUPON_EXPIRED;
import static com.qiguliuxing.dts.wx.util.WxResponseCode.ORDER_COMMENTED;
import static com.qiguliuxing.dts.wx.util.WxResponseCode.ORDER_COMMENT_EXPIRED;
import static com.qiguliuxing.dts.wx.util.WxResponseCode.ORDER_CONFIRM_OPERATION;
import static com.qiguliuxing.dts.wx.util.WxResponseCode.ORDER_DEL_OPERATION;
import static com.qiguliuxing.dts.wx.util.WxResponseCode.ORDER_INVALID;
import static com.qiguliuxing.dts.wx.util.WxResponseCode.ORDER_INVALID_OPERATION;
import static com.qiguliuxing.dts.wx.util.WxResponseCode.ORDER_NOT_COMMENT;
import static com.qiguliuxing.dts.wx.util.WxResponseCode.ORDER_PAY_FAIL;
import static com.qiguliuxing.dts.wx.util.WxResponseCode.ORDER_UNKNOWN;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import com.qiguliuxing.dts.vo.OrderVO;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.BaseWxPayResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.pagehelper.PageInfo;
import com.qiguliuxing.dts.core.consts.CommConsts;
import com.qiguliuxing.dts.core.express.ExpressService;
import com.qiguliuxing.dts.core.express.dao.ExpressInfo;
import com.qiguliuxing.dts.core.express.dao.Traces;
import com.qiguliuxing.dts.core.notify.NotifyService;
import com.qiguliuxing.dts.core.notify.NotifyType;
import com.qiguliuxing.dts.core.qcode.QCodeService;
import com.qiguliuxing.dts.core.system.SystemConfig;
import com.qiguliuxing.dts.core.util.DateTimeUtil;
import com.qiguliuxing.dts.core.util.JacksonUtil;
import com.qiguliuxing.dts.core.util.ResponseUtil;
import com.qiguliuxing.dts.db.domain.DtsAccountTrace;
import com.qiguliuxing.dts.db.domain.DtsAddress;
import com.qiguliuxing.dts.db.domain.DtsCart;
import com.qiguliuxing.dts.db.domain.DtsComment;
import com.qiguliuxing.dts.db.domain.DtsGoodsProduct;
import com.qiguliuxing.dts.db.domain.DtsGroupon;
import com.qiguliuxing.dts.db.domain.DtsGrouponRules;
import com.qiguliuxing.dts.db.domain.DtsOrder;
import com.qiguliuxing.dts.db.domain.DtsOrderGoods;
import com.qiguliuxing.dts.db.domain.DtsUser;
import com.qiguliuxing.dts.db.domain.DtsUserAccount;
import com.qiguliuxing.dts.db.domain.DtsUserFormid;
import com.qiguliuxing.dts.db.service.DtsAccountService;
import com.qiguliuxing.dts.db.service.DtsAddressService;
import com.qiguliuxing.dts.db.service.DtsCouponUserService;
import com.qiguliuxing.dts.db.service.DtsGoodsProductService;
import com.qiguliuxing.dts.db.service.DtsGrouponRulesService;
import com.qiguliuxing.dts.db.service.DtsGrouponService;
import com.qiguliuxing.dts.db.service.DtsOrderGoodsService;
import com.qiguliuxing.dts.db.service.DtsOrderService;
import com.qiguliuxing.dts.db.service.DtsRegionService;
import com.qiguliuxing.dts.db.service.DtsUserFormIdService;
import com.qiguliuxing.dts.db.service.DtsUserService;
import com.qiguliuxing.dts.db.util.OrderHandleOption;
import com.qiguliuxing.dts.db.util.OrderUtil;
import com.qiguliuxing.dts.wx.dao.BrandCartGoods;
import com.qiguliuxing.dts.wx.dao.BrandOrderGoods;
import com.qiguliuxing.dts.wx.util.IpUtil;
import com.qiguliuxing.dts.wx.util.WxResponseCode;
import com.qiguliuxing.dts.wx.util.WxResponseUtil;

/**
 * 订单服务
 *
 * <p>
 * 订单状态： 101 订单生成，未支付；102，下单后未支付用户取消；103，下单后未支付超时系统自动取消 201
 * 支付完成，商家未发货；202，订单生产，已付款未发货，但是退款取消； 301 商家发货，用户未确认； 401 用户确认收货； 402
 * 用户没有确认收货超过一定时间，系统自动确认收货；
 *
 * <p>
 * 用户操作： 当101用户未付款时，此时用户可以进行的操作是取消订单，或者付款操作 当201支付完成而商家未发货时，此时用户可以取消订单并申请退款
 * 当301商家已发货时，此时用户可以有确认收货的操作 当401用户确认收货以后，此时用户可以进行的操作是删除订单，评价商品，或者再次购买
 * 当402系统自动确认收货以后，此时用户可以删除订单，评价商品，或者再次购买
 *
 * <p>
 * 注意：目前不支持订单退货和售后服务
 */
@Service
public class WxOrderService {
	private static final Logger logger = LoggerFactory.getLogger(WxOrderService.class);


	@Autowired
	DtsOrderService dtsOrderService;


	public List<OrderVO> queryOrderList(String userId, String orderNo, List<String> orderStatusList) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("orderNo", orderNo);
		params.put("orderStatusList", orderStatusList);
		return dtsOrderService.queryOrderList(userId, orderNo, orderStatusList);
	}
}
