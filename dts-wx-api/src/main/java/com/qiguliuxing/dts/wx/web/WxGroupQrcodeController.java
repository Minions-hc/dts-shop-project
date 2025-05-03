package com.qiguliuxing.dts.wx.web;

import com.qiguliuxing.dts.core.util.ResponseUtil;
import com.qiguliuxing.dts.db.service.WechatGroupQrcodeService;
import com.qiguliuxing.dts.vo.WechatGroupQrcodeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/wx/qrcode")
@Validated
public class WxGroupQrcodeController {


    @Autowired
    private WechatGroupQrcodeService qrcodeService;


    @GetMapping("/list")
    public Object getAllQrcodes() {
        List<WechatGroupQrcodeVO> qrcodes = qrcodeService.getAllQrcodes();
        Map<String, Object> data = new HashMap<>();
        data.put("items", qrcodes);
        return ResponseUtil.ok(data);
    }

}
