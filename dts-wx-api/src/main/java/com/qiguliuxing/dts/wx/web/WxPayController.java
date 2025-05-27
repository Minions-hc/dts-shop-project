package com.qiguliuxing.dts.wx.web;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qiguliuxing.dts.wx.util.WeChatPayUtil;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import com.wechat.pay.contrib.apache.httpclient.util.AesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
@RequestMapping("/wx/wxpay")
public class WxPayController {

    private static final Logger logger = LoggerFactory.getLogger(WxPayController.class);

    @Autowired
    private Verifier verifier; // 微信支付验证器

    private static final String API_V3_KEY = "chillShangShiDuo1717472713666888";
    /**
     * 创建JSAPI支付订单
     * body
     * @return 支付参数
     */
    @PostMapping("/create")
    public Map<String, String> createOrder(@RequestBody String body ) throws Exception {
        JSONObject data = JSON.parseObject(body);
        String openId = data.getString("openId");
        Integer amount = data.getInteger("amount");
        String description = data.getString("description");
        // 生成商户订单号(实际项目中应该有自己的订单号生成规则)
        String outTradeNo = "ORDER_" + System.currentTimeMillis();
        return WeChatPayUtil.createJsapiOrder(openId, amount, description, outTradeNo);
    }

    /**
     * 支付回调通知
     * @param requestBody 回调数据
     * @return 处理结果
     */
    @PostMapping("/notify")
    public String payNotify(HttpServletRequest request,
                            @RequestBody String requestBody) {

        // 1. 获取并验证请求头
        String signature = request.getHeader("Wechatpay-Signature");
        String timestamp = request.getHeader("Wechatpay-Timestamp");
        String nonce = request.getHeader("Wechatpay-Nonce");
        String serialNo = request.getHeader("Wechatpay-Serial");
        if (signature == null || timestamp == null || nonce == null || serialNo == null) {
            logger.error("微信支付回调通知头信息不完整");
            return failResponse("通知头信息不完整");
        }

        // 2. 验证签名
        String message = timestamp + "\n" + nonce + "\n" + requestBody + "\n";
        if (!verifier.verify(serialNo, message.getBytes(StandardCharsets.UTF_8), signature)) {
            logger.error("微信支付回调签名验证失败");
            return failResponse("签名验证失败");
        }

        // 3. 解析通知数据
        Map<String, Object> resultMap = parseNotifyData(requestBody);
        if (resultMap == null) {
            return failResponse("数据解析失败");
        }
        // 4. 处理业务逻辑
//        boolean processResult = paymentService.processPaymentNotify(resultMap);

        // 5. 返回处理结果
        return successResponse();
    }


    /**
     * 解析通知数据
     */
    private Map<String, Object> parseNotifyData(String requestBody) {
        try {
            // 示例: 使用Jackson解析JSON
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> dataMap = mapper.readValue(requestBody, Map.class);

            // 获取加密数据
            String cipherText = (String) dataMap.get("resource.ciphertext");
            String associatedData = (String) dataMap.get("resource.associated_data");
            String nonce = (String) dataMap.get("resource.nonce");

            // 使用APIv3密钥解密
            AesUtil aesUtil = new AesUtil(API_V3_KEY.getBytes(StandardCharsets.UTF_8));
            String decryptData = aesUtil.decryptToString(
                    associatedData.getBytes(StandardCharsets.UTF_8),
                    nonce.getBytes(StandardCharsets.UTF_8),
                    cipherText
            );

            // 解析解密后的数据
            return mapper.readValue(decryptData, Map.class);
        } catch (Exception e) {
            logger.error("解析微信支付通知数据失败", e);
            return null;
        }
    }

    /**
     * 成功响应
     */
    private String successResponse() {
        return "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
    }

    /**
     * 失败响应
     */
    private String failResponse(String errMsg) {
        return String.format("<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[%s]]></return_msg></xml>",
                errMsg != null ? errMsg : "");
    }

//    /**
//     * 查询订单状态
//     * @param orderId 商户订单号
//     * @return 订单状态
//     */
//    @GetMapping("/query")
//    public JSONObject queryOrder(@RequestParam String orderId) throws Exception {
//        return wxPayService.queryOrder(orderId);
//    }
//
//    /**
//     * 关闭订单
//     * @param orderId 商户订单号
//     * @return 关闭结果
//     */
//    @PostMapping("/close")
//    public JSONObject closeOrder(@RequestParam String orderId) throws Exception {
//        return wxPayService.closeOrder(orderId);
//    }
}
