package com.qiguliuxing.dts.wx.service;

import com.alibaba.fastjson.JSONObject;

/**
 * 微信支付服务接口
 */
public interface WxPayService {

    /**
     * 小程序支付统一下单
     * @param openid 用户openid
     * @param orderId 商户订单号
     * @param amount 金额（分）
     * @param description 商品描述
     * @return 支付参数
     */
    JSONObject createJsapiOrder(String openid, String orderId, int amount, String description) throws Exception;

    /**
     * 查询订单状态
     * @param orderId 商户订单号
     * @return 订单状态
     */
    JSONObject queryOrder(String orderId) throws Exception;

    /**
     * 关闭订单
     * @param orderId 商户订单号
     * @return 关闭结果
     */
    JSONObject closeOrder(String orderId) throws Exception;

    /**
     * 处理支付回调
     * @param notifyData 回调数据
     * @return 处理结果
     */
    String handleNotify(String notifyData);
}
