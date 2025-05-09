package com.qiguliuxing.dts.wx.util;

import com.qiguliuxing.dts.wx.config.WxPayConfig;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

/**
 * 微信支付签名工具类
 * 用于生成各种签名和验证签名
 */
public class WxPaySignatureUtil {

    /**
     * 生成随机字符串
     * @return 随机字符串
     */
    public static String generateNonceStr() {
        char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new SecureRandom();
        for (int i = 0; i < 32; i++) {
            sb.append(chars[random.nextInt(chars.length)]);
        }
        return sb.toString();
    }

    /**
     * SHA256签名
     * @param message 待签名消息
     * @param key 密钥
     * @return 签名结果
     */
    public static String signSHA256(String message, String key) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] bytes = sha256_HMAC.doFinal(message.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            throw new RuntimeException("生成签名失败", e);
        }
    }

    /**
     * 生成请求头Authorization
     * @param method 请求方法（GET/POST等）
     * @param url 请求URL（不包含域名）
     * @param body 请求体（GET请求可为空）
     * @return 认证头信息
     */
    public static String generateAuthorization(String method, String url, String body) {
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String nonceStr = generateNonceStr();

        // 构建签名串
        String signatureStr = method + "\n" +
                url + "\n" +
                timestamp + "\n" +
                nonceStr + "\n" +
                (body != null ? body : "") + "\n";

        // 使用商户私钥对签名串进行SHA256 with RSA签名
        String signature = ""; // 这里需要实现RSA签名，实际项目中可以使用Security库完成

        return "WECHATPAY2-SHA256-RSA2048 " +
                "mchid=\"" + WxPayConfig.MCH_ID + "\"," +
                "nonce_str=\"" + nonceStr + "\"," +
                "timestamp=\"" + timestamp + "\"," +
                "serial_no=\"" + WxPayConfig.MCH_SERIAL_NO + "\"," +
                "signature=\"" + signature + "\"";
    }
}
