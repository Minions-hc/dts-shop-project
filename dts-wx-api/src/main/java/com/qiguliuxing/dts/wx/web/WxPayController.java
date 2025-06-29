package com.qiguliuxing.dts.wx.web;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qiguliuxing.dts.db.service.BlindBoxRecordService;
import com.qiguliuxing.dts.db.service.BoxProductService;
import com.qiguliuxing.dts.db.service.WxOrderParameterService;
import com.qiguliuxing.dts.vo.BlindBoxDrawResultVO;
import com.qiguliuxing.dts.vo.WxOrderParameter;
import com.qiguliuxing.dts.wx.service.WxOrderService;
import com.qiguliuxing.dts.wx.util.WeChatPayUtil;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import com.wechat.pay.contrib.apache.httpclient.util.AesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/wx/wxpay")
public class WxPayController {

    private static final Logger logger = LoggerFactory.getLogger(WxPayController.class);

    @Autowired
    private Verifier verifier; // 微信支付验证器

    @Autowired
    private WxOrderParameterService wxOrderParameterService;

    @Autowired
    private BlindBoxRecordService blindBoxRecordService;

    @Autowired
    private BoxProductService boxProductService;

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
        Integer businessType = data.getInteger("businessType");
        Integer couponId = data.getInteger("couponId");
        Integer point = data.getInteger("point");
        WxOrderParameter wxOrderParameter = new WxOrderParameter();
        String userId = data.getString("userId");
        wxOrderParameter.setUserId(userId);
        wxOrderParameter.setBusinessType(businessType);
        if (businessType.equals(1)){
            List<Integer> numbers = JSON.parseArray(data.getJSONArray("numbers").toJSONString(), Integer.class);
            String boxNumber = data.getString("boxNumber");
            Integer seriesId = data.getInteger("seriesId");
            Integer spiritPower = data.getInteger("spiritPower");
            String activityType = data.getString("activityType");
            BigDecimal pointDeduction = data.getBigDecimal("pointDeduction");
            BigDecimal couponDeduction = data.getBigDecimal("couponDeduction");
            BigDecimal orderAmount = data.getBigDecimal("orderAmount");
            BigDecimal paymentAmount = data.getBigDecimal("paymentAmount");
            wxOrderParameter.setNumbers(numbers);
            wxOrderParameter.setBoxNumber(boxNumber);
            wxOrderParameter.setSeriesId(seriesId);
            wxOrderParameter.setSpiritPower(spiritPower);
            wxOrderParameter.setActivityType(activityType);
            wxOrderParameter.setPointDeduction(pointDeduction);
            wxOrderParameter.setCouponDeduction(couponDeduction);
            wxOrderParameter.setOrderAmount(orderAmount);
            wxOrderParameter.setPaymentAmount(paymentAmount);
            wxOrderParameter.setPoint(point);
            wxOrderParameter.setCouponId(couponId);
        } else if (businessType.equals(2)){
            List<Integer> ids = JSON.parseArray(data.getJSONArray("ids").toJSONString(), Integer.class);
            BigDecimal orderAmount = data.getBigDecimal("orderAmount");
            BigDecimal paymentAmount = data.getBigDecimal("paymentAmount");
            wxOrderParameter.setIds(ids);
            wxOrderParameter.setOrderAmount(orderAmount);
            wxOrderParameter.setPaymentAmount(paymentAmount);
        }
        // 生成商户订单号(实际项目中应该有自己的订单号生成规则)
        String outTradeNo = "ORDER_" + System.currentTimeMillis();
        Map<String, String> jsapiOrder = WeChatPayUtil.createJsapiOrder(openId, amount, description, outTradeNo);
        wxOrderParameter.setOutTradeNo(outTradeNo);
        wxOrderParameter.setWxOrderNo(jsapiOrder.get("nonceStr"));
        int result = wxOrderParameterService.saveOrderParameter(wxOrderParameter);
        if (result < 1) {
            logger.error("微信支付参数写入错误！ outTradeNo：{}", outTradeNo);
        }
        return jsapiOrder;
    }

    /**
     * 支付回调通知
     * @param requestBody 回调数据
     * @return 处理结果
     */
    @PostMapping("/notify")
    public String payNotify(HttpServletRequest request, @RequestBody String requestBody) {

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
        String outTradeNo = String.valueOf(resultMap.get("out_trade_no"));
        WxOrderParameter wxOrderParameter = wxOrderParameterService.getByOutTradeNo(outTradeNo);
        if (wxOrderParameter == null) {
            logger.error("微信支付参数为空！ outTradeNo：{}", outTradeNo);
            return failResponse("微信支付参数为空");
        }

        // 4. 处理业务逻辑
        if (wxOrderParameter.getBusinessType().equals(1)) {
            // 盲盒抽赏
            blindBoxRecordService.drawBlindBox(wxOrderParameter);
        } else if (wxOrderParameter.getBusinessType().equals(2)) {
            // 提货运费
            boxProductService.shipProducts(wxOrderParameter.getUserId(), wxOrderParameter.getIds(), outTradeNo);
        }

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
}
