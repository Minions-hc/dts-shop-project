package com.qiguliuxing.dts.wx.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qiguliuxing.dts.wx.config.WxMiniProgramConfig;
import com.qiguliuxing.dts.wx.util.WxUserInfoDecryptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 微信小程序登录服务
 */
@Service
public class WxLoginService {


    /**
     * 根据code获取微信开放数据
     * @param code 前端传来的code
     * @return 包含openid和session_key的JSON对象
     * @throws Exception 如果获取失败
     */
    public JSONObject code2Session(String code) throws Exception {
        // 构造请求URL
        String url = String.format(
                WxMiniProgramConfig.JS_CODE_2_SESSION_URL,
                WxMiniProgramConfig.APP_ID,
                WxMiniProgramConfig.APP_SECRET,
                code
        );

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        JSONObject result = null;

        try (Response response = client.newCall(request).execute()) {
            String responseStr = Objects.requireNonNull(response.body()).string();
            // 解析响应
            result = JSON.parseObject(responseStr);
        }

        // 检查是否有错误
        if (result.containsKey("errcode")) {
            throw new RuntimeException("微信登录失败: " + result.getString("errmsg"));
        }
        return result;
    }

    /**
     * 解密用户加密数据
     * @param encryptedData 加密数据
     * @param sessionKey 会话密钥
     * @param iv 加密算法的初始向量
     * @return 解密后的用户信息
     */
    public JSONObject decryptUserInfo(String encryptedData, String sessionKey, String iv) {
        JSONObject userInfo = WxUserInfoDecryptor.decrypt(encryptedData, sessionKey, iv);

        // 验证解密数据是否有效
        JSONObject watermark = userInfo.getJSONObject("watermark");
        if (watermark == null || !WxMiniProgramConfig.APP_ID.equals(watermark.getString("appid"))) {
            throw new RuntimeException("解密数据校验失败: 无效的水印信息或AppID不匹配");
        }
        return userInfo;
    }

}
