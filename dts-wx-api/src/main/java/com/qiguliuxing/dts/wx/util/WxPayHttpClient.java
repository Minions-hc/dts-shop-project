package com.qiguliuxing.dts.wx.util;

import com.qiguliuxing.dts.wx.config.WxPayConfig;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * HTTP请求工具类
 * 用于与微信支付API交互
 */
public class WxPayHttpClient {

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build();

    /**
     * 发送POST请求
     * @param url 请求URL
     * @param body 请求体
     * @return 响应结果
     */
    public static String post(String url, String body) throws IOException {
        Request request = new Request.Builder()
                .url(WxPayConfig.API_URL + url)
                .post(RequestBody.create(body, MediaType.get("application/json")))
                .header("Authorization", WxPaySignatureUtil.generateAuthorization("POST", url, body))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            return Objects.requireNonNull(response.body()).string();
        }
    }

    /**
     * 发送GET请求
     * @param url 请求URL
     * @return 响应结果
     */
    public static String get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(WxPayConfig.API_URL + url)
                .get()
                .header("Authorization", WxPaySignatureUtil.generateAuthorization("GET", url, null))
                .header("Accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            return Objects.requireNonNull(response.body()).string();
        }
    }
}
