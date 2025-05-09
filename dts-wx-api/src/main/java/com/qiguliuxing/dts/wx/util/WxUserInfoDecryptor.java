package com.qiguliuxing.dts.wx.util;

import com.alibaba.fastjson.JSONObject;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.security.Security;
import java.util.Arrays;

/**
 * 微信用户信息解密工具类
 */
public class WxUserInfoDecryptor {

    static {
        // 添加BouncyCastleProvider支持
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * 解密用户信息
     * @param encryptedData 加密数据
     * @param sessionKey 会话密钥
     * @param iv 加密算法的初始向量
     * @return 解密后的用户信息
     */
    public static JSONObject decrypt(String encryptedData, String sessionKey, String iv) {
        try {
            // Base64解码
            byte[] dataBytes = Base64Utils.decode(encryptedData.getBytes());
            byte[] keyBytes = Base64Utils.decode(sessionKey.getBytes());
            byte[] ivBytes = Base64Utils.decode(iv.getBytes());

            // 处理密钥
            byte[] keySpec = Arrays.copyOf(keyBytes, 16);
            SecretKeySpec key = new SecretKeySpec(keySpec, "AES");

            // 初始化算法参数
            AlgorithmParameters params = AlgorithmParameters.getInstance("AES", "SunJCE");
            params.init(new IvParameterSpec(ivBytes));

            // 解密
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, params);
            byte[] result = cipher.doFinal(dataBytes);

            // 解析JSON
            return JSONObject.parseObject(new String(result));
        } catch (Exception e) {
            throw new RuntimeException("解密用户信息失败", e);
        }
    }
}
