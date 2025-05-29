package com.qiguliuxing.dts.wx.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.cert.CertificatesManager;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信支付工具类
 */
public class WeChatPayUtil {

    private static final String APP_ID = "wx956fbcb39a50db00";
    private static final String MCH_ID = "1717472713";
    private static final String API_V3_KEY = "chillShangShiDuo1717472713666888";
    private static final String MCH_SERIAL_NO = "45D52E94F1332F4D5E0BD68A838BCA2E3C2C9E50";
    private static final String PRIVATE_KEY_PATH = "apiclient_key.pem";
    private static final String NOTIFY_URL = "https://chaoshangshiduo.com/admin/wxpay/notify";

    private static final String API_URL = "https://api.mch.weixin.qq.com";

    private static CloseableHttpClient httpClient;
    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        try {
            // 加载商户私钥
            InputStream inputStream = new ClassPathResource(PRIVATE_KEY_PATH).getInputStream();
            PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(inputStream);

            // 初始化HTTP客户端
            httpClient = WechatPayHttpClientBuilder.create()
                    .withMerchant(MCH_ID, MCH_SERIAL_NO, merchantPrivateKey)
                    .withValidator(new WechatPay2Validator(getVerifier()))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取证书验证器
     */
    private static Verifier getVerifier() throws Exception {
        CertificatesManager certificatesManager = CertificatesManager.getInstance();
        certificatesManager.putMerchant(MCH_ID, new WechatPay2Credentials(MCH_ID,
                        new PrivateKeySigner(MCH_SERIAL_NO, PemUtil.loadPrivateKey(new ClassPathResource(PRIVATE_KEY_PATH).getInputStream()))),
                API_V3_KEY.getBytes(StandardCharsets.UTF_8));
        return certificatesManager.getVerifier(MCH_ID);
    }

    /**
     * 创建JSAPI支付订单
     * @param openid 用户openid
     * @param amount 金额(分)
     * @param description 商品描述
     * @param outTradeNo 商户订单号
     * @return prepay_id和其他支付参数
     */
    public static Map<String, String> createJsapiOrder(String openid, int amount, String description, String outTradeNo) throws Exception {
        String url = API_URL + "/v3/pay/transactions/jsapi";
        // 构建请求参数
        Map<String, Object> params = new HashMap<>();
        params.put("appid", APP_ID);
        params.put("mchid", MCH_ID);
        params.put("description", description);
        params.put("out_trade_no", outTradeNo);
        params.put("notify_url", NOTIFY_URL);

        Map<String, Object> amountMap = new HashMap<>();
        amountMap.put("total", amount);
        amountMap.put("currency", "CNY");
        params.put("amount", amountMap);

        Map<String, String> payerMap = new HashMap<>();
        payerMap.put("openid", openid);
        params.put("payer", payerMap);

        // 创建HTTP请求
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Accept", "application/json");
        httpPost.addHeader("Content-type", "application/json; charset=utf-8");
        httpPost.setEntity(new StringEntity(objectMapper.writeValueAsString(params), "UTF-8"));

        // 执行请求
        CloseableHttpResponse response = httpClient.execute(httpPost);
        String responseBody = EntityUtils.toString(response.getEntity());

        if (response.getStatusLine().getStatusCode() == 200) {
            // 解析返回的prepay_id
            Map<String, String> result = objectMapper.readValue(responseBody, Map.class);
            String prepayId = result.get("prepay_id");

            // 生成小程序端调起支付所需的参数
            return generateMiniProgramPayParams(prepayId);
        } else {
            throw new RuntimeException("微信支付下单失败: " + responseBody);
        }
    }

    /**
     * 生成小程序调起支付所需的参数
     * @param prepayId 预支付ID
     * @return 支付参数
     */
    private static Map<String, String> generateMiniProgramPayParams(String prepayId) throws Exception {
        long timestamp = System.currentTimeMillis() / 1000;
        String nonceStr = generateNonceStr();
        String packageStr = "prepay_id=" + prepayId;

        // 构造签名串
        String message = APP_ID + "\n" + timestamp + "\n" + nonceStr + "\n" + packageStr + "\n";

        // 加载私钥
        InputStream inputStream = new ClassPathResource(PRIVATE_KEY_PATH).getInputStream();
        PrivateKey privateKey = PemUtil.loadPrivateKey(inputStream);

        // 签名
        java.security.Signature signature = java.security.Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(message.getBytes(StandardCharsets.UTF_8));
        byte[] signBytes = signature.sign();
        String paySign = Base64.getEncoder().encodeToString(signBytes);

        // 返回支付参数
        Map<String, String> payParams = new HashMap<>();
        payParams.put("timeStamp", String.valueOf(timestamp));
        payParams.put("nonceStr", nonceStr);
        payParams.put("package", packageStr);
        payParams.put("signType", "RSA");
        payParams.put("paySign", paySign);

        return payParams;
    }

    /**
     * 生成随机字符串
     */
    private static String generateNonceStr() {
        return java.util.UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
    }
}
