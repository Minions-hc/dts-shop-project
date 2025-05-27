package com.qiguliuxing.dts.wx.config;

import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import com.wechat.pay.contrib.apache.httpclient.cert.CertificatesManager;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;

@Configuration
public class WeChatPayConfig {

    // 从配置文件中读取这些值
    private final String mchId = "1717472713";
    private final String mchSerialNo = "45D52E94F1332F4D5E0BD68A838BCA2E3C2C9E50";
    private final String apiV3Key = "chillShangShiDuo1717472713666888";
    private final String privateKeyPath = "apiclient_key.pem"; // 私钥文件路径

    @Bean
    public Verifier verifier() throws Exception {
        // 1. 加载商户私钥
        InputStream inputStream = new ClassPathResource(privateKeyPath).getInputStream();
        PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(inputStream);

        // 2. 初始化证书管理器
        CertificatesManager certificatesManager = CertificatesManager.getInstance();

        // 3. 添加商户凭证
        certificatesManager.putMerchant(mchId,
                new WechatPay2Credentials(mchId,
                        new PrivateKeySigner(mchSerialNo, merchantPrivateKey)),
                apiV3Key.getBytes(StandardCharsets.UTF_8));

        // 4. 获取验证器
        return certificatesManager.getVerifier(mchId);
    }

    @Bean
    public CloseableHttpClient wechatPayHttpClient(Verifier verifier) throws Exception {
        // 加载商户私钥
        InputStream inputStream = new ClassPathResource(privateKeyPath).getInputStream();
        PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(inputStream);

        return WechatPayHttpClientBuilder.create()
                .withMerchant(mchId, mchSerialNo, merchantPrivateKey)
                .withValidator(new WechatPay2Validator(verifier))
                .build();
    }
}
