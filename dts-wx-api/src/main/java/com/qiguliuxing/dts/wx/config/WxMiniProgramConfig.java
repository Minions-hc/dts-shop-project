package com.qiguliuxing.dts.wx.config;

/**
 * 微信小程序配置类
 */
public class WxMiniProgramConfig {
    // 小程序appId
    public static final String APP_ID = "wx956fbcb39a50db00";
    // 小程序appSecret
    public static final String APP_SECRET = "46db2d47508d79aed62c2905fd41573c";
    // 登录接口地址
    public static final String JS_CODE_2_SESSION_URL =
            "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";
}
