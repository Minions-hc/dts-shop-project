package com.qiguliuxing.dts.wx.config;

/**
 * 微信支付配置类
 * 用于存储微信支付相关的配置信息
 */
public class WxPayConfig {
    // 小程序appid
    public static final String APP_ID = "你的小程序AppID";
    // 商户号
    public static final String MCH_ID = "你的商户号";
    // APIv3密钥
    public static final String API_V3_KEY = "你的APIv3密钥";
    // 商户证书序列号
    public static final String MCH_SERIAL_NO = "你的商户证书序列号";
    // 商户私钥文件路径
    public static final String PRIVATE_KEY_PATH = "/path/to/apiclient_key.pem";
    // 微信支付回调地址
    public static final String NOTIFY_URL = "https://yourdomain.com/api/wxpay/notify";
    // 微信支付API地址
    public static final String API_URL = "https://api.mch.weixin.qq.com";
}
