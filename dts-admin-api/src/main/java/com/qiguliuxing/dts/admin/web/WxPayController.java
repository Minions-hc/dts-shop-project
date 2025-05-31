package com.qiguliuxing.dts.admin.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.qiguliuxing.dts.db.service.BlindBoxRecordService;
import com.qiguliuxing.dts.db.service.BoxProductService;
import com.qiguliuxing.dts.db.service.WxOrderParameterService;
import com.qiguliuxing.dts.vo.WxOrderParameter;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import com.wechat.pay.contrib.apache.httpclient.util.AesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
@RequestMapping("admin/wxpay")
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
     * 支付回调通知
     * @param requestBody 回调数据
     * @return 处理结果
     */
    @PostMapping("/notify")
    public String payNotify(HttpServletRequest request, @RequestBody String requestBody) {
        // 1. 记录请求基本信息
        logger.info("\n========== 微信支付回调通知开始 ==========");
        logger.info("请求方法: {}", request.getMethod());  // 应该是 POST
        logger.info("请求URL: {}", request.getRequestURL());
        logger.info("Content-Type: {}", request.getContentType());
        logger.info("请求Body数据: \n{}", requestBody);



        System.out.println("********************进入微信回调*****************");
        // 1. 获取并验证请求头
        String signature = request.getHeader("Wechatpay-Signature");
        String timestamp = request.getHeader("Wechatpay-Timestamp");
        String nonce = request.getHeader("Wechatpay-Nonce");
        String serialNo = request.getHeader("Wechatpay-Serial");

        logger.info("微信回调请求头 - Signature: {}", signature);
        logger.info("微信回调请求头 - Timestamp: {}", timestamp);
        logger.info("微信回调请求头 - Nonce: {}", nonce);
        logger.info("微信回调请求头 - SerialNo: {}", serialNo);

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
        System.out.println("**************商户订单号：" + outTradeNo);
        WxOrderParameter wxOrderParameter = wxOrderParameterService.getByOutTradeNo(outTradeNo);
        if (wxOrderParameter == null) {
            logger.error("微信支付参数为空！ outTradeNo：{}", outTradeNo);
            return failResponse("微信支付参数为空");
        }
        System.out.println("**************支付参数：" + wxOrderParameter.getBusinessType());
        // 4. 处理业务逻辑
        if (wxOrderParameter.getBusinessType().equals(1)) {
            // 盲盒抽赏
            blindBoxRecordService.drawBlindBox(wxOrderParameter);
        } else if (wxOrderParameter.getBusinessType().equals(2)) {
            // 提货运费
            boxProductService.shipProducts(wxOrderParameter.getUserId(), wxOrderParameter.getIds());
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
            Map<String, Object> resultMap = (Map<String, Object>) dataMap.get("resource");
            // 获取加密数据
            String cipherText = (String) resultMap.get("ciphertext");
            String associatedData = (String) resultMap.get("associated_data");
            String nonce = (String) resultMap.get("nonce");

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
