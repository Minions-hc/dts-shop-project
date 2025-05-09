package com.qiguliuxing.dts.wx.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qiguliuxing.dts.wx.config.WxPayConfig;
import com.qiguliuxing.dts.wx.service.WxPayService;
import com.qiguliuxing.dts.wx.util.WxPayHttpClient;
import com.qiguliuxing.dts.wx.util.WxPaySignatureUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信支付服务实现类
 */
@Service
public class WxPayServiceImpl implements WxPayService {

    @Override
    public JSONObject createJsapiOrder(String openid, String orderId, int amount, String description) throws Exception {
        // 1. 构建请求参数
        Map<String, Object> params = new HashMap<>();
        params.put("appid", WxPayConfig.APP_ID);
        params.put("mchid", WxPayConfig.MCH_ID);
        params.put("description", description);
        params.put("out_trade_no", orderId);
        params.put("notify_url", WxPayConfig.NOTIFY_URL);

        // 2. 设置金额信息
        Map<String, Integer> amountInfo = new HashMap<>();
        amountInfo.put("total", amount);
        params.put("amount", amountInfo);

        // 3. 设置支付者信息
        Map<String, String> payerInfo = new HashMap<>();
        payerInfo.put("openid", openid);
        params.put("payer", payerInfo);

        // 4. 发送请求
        String response = WxPayHttpClient.post("/v3/pay/transactions/jsapi", JSON.toJSONString(params));

        // 5. 解析响应并生成小程序支付参数
        JSONObject result = JSON.parseObject(response);
        return generatePaymentParams(result.getString("prepay_id"));
    }

    /**
     * 生成小程序支付参数
     * @param prepayId 预支付ID
     * @return 支付参数
     */
    private JSONObject generatePaymentParams(String prepayId) {
        JSONObject params = new JSONObject();
        params.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
        params.put("nonceStr", WxPaySignatureUtil.generateNonceStr());
        params.put("package", "prepay_id=" + prepayId);
        params.put("signType", "RSA");

        // 生成签名
        String signStr = params.getString("appId") + "\n" +
                params.getString("timeStamp") + "\n" +
                params.getString("nonceStr") + "\n" +
                params.getString("package") + "\n";
        String sign = ""; // 这里需要实现RSA签名

        params.put("paySign", sign);
        return params;
    }

    @Override
    public JSONObject queryOrder(String orderId) throws Exception {
        String url = "/v3/pay/transactions/out-trade-no/" + orderId + "?mchid=" + WxPayConfig.MCH_ID;
        String response = WxPayHttpClient.get(url);
        return JSON.parseObject(response);
    }

    @Override
    public JSONObject closeOrder(String orderId) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("mchid", WxPayConfig.MCH_ID);

        String url = "/v3/pay/transactions/out-trade-no/" + orderId + "/close";
        String response = WxPayHttpClient.post(url, JSON.toJSONString(params));
        return JSON.parseObject(response);
    }

    @Override
    public String handleNotify(String notifyData) {
        // 1. 解析回调数据
        JSONObject data = JSON.parseObject(notifyData);

        // 2. 验证签名（实际项目中需要实现）
        boolean verifyResult = verifySignature(data);
        if (!verifyResult) {
            return buildFailResponse("签名验证失败");
        }

        // 3. 处理业务逻辑
        String orderId = data.getString("out_trade_no");
        String transactionId = data.getString("transaction_id");
        int amount = data.getJSONObject("amount").getInteger("total");
        String tradeState = data.getString("trade_state");

        if ("SUCCESS".equals(tradeState)) {
            // TODO: 更新订单状态等业务处理
            return buildSuccessResponse();
        } else {
            return buildFailResponse("支付未成功");
        }
    }

    private boolean verifySignature(JSONObject data) {
        // 实际项目中需要实现签名验证逻辑
        return true;
    }

    private String buildSuccessResponse() {
        return "{\"code\":\"SUCCESS\",\"message\":\"成功\"}";
    }

    private String buildFailResponse(String message) {
        return String.format("{\"code\":\"FAIL\",\"message\":\"%s\"}", message);
    }
}
