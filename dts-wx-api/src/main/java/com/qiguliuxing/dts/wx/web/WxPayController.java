package com.qiguliuxing.dts.wx.web;
import com.alibaba.fastjson.JSONObject;
import com.qiguliuxing.dts.wx.service.WxPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wx/wxpay")
public class WxPayController {

    @Autowired
    private WxPayService wxPayService;


    /**
     * 创建JSAPI支付订单
     * @param openid 用户openid
     * @param orderId 商户订单号
     * @param amount 金额（分）
     * @param description 商品描述
     * @return 支付参数
     */
    @PostMapping("/create")
    public JSONObject createOrder(
            @RequestParam String openid,
            @RequestParam String orderId,
            @RequestParam int amount,
            @RequestParam String description) throws Exception {
        return wxPayService.createJsapiOrder(openid, orderId, amount, description);
    }

    /**
     * 支付回调通知
     * @param notifyData 回调数据
     * @return 处理结果
     */
    @PostMapping("/notify")
    public String payNotify(@RequestBody String notifyData) {
        return wxPayService.handleNotify(notifyData);
    }

    /**
     * 查询订单状态
     * @param orderId 商户订单号
     * @return 订单状态
     */
    @GetMapping("/query")
    public JSONObject queryOrder(@RequestParam String orderId) throws Exception {
        return wxPayService.queryOrder(orderId);
    }

    /**
     * 关闭订单
     * @param orderId 商户订单号
     * @return 关闭结果
     */
    @PostMapping("/close")
    public JSONObject closeOrder(@RequestParam String orderId) throws Exception {
        return wxPayService.closeOrder(orderId);
    }
}
