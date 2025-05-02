package com.qiguliuxing.dts.admin.web;


import com.qiguliuxing.dts.core.util.ResponseUtil;
import com.qiguliuxing.dts.db.service.WechatGroupQrcodeService;
import com.qiguliuxing.dts.vo.WechatGroupQrcodeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/wx/qrcode")
@Validated
public class WechatGroupQrcodeController {

    @Autowired
    private WechatGroupQrcodeService qrcodeService;

    @PostMapping("/create")
    public Object addQrcode(@RequestBody WechatGroupQrcodeVO qrcode) {
        int result = qrcodeService.addQrcode(qrcode);
        if (result == 0) {
            return ResponseUtil.fail();
        }
        return ResponseUtil.ok();
    }

    @PostMapping("/delete")
    public Object deleteQrcode(@RequestBody WechatGroupQrcodeVO qrcode) {
        int result = qrcodeService.deleteQrcode(qrcode.getId());
        return result > 0 ? ResponseUtil.ok() : ResponseUtil.fail();
    }

    @PostMapping("/update")
    public Object updateQrcode(@RequestBody WechatGroupQrcodeVO qrcode) {
        int result = qrcodeService.updateQrcode(qrcode);
        return result > 0 ? ResponseUtil.ok() : ResponseUtil.fail();
    }

    @GetMapping("/list")
    public Object getAllQrcodes() {
        List<WechatGroupQrcodeVO> qrcodes = qrcodeService.getAllQrcodes();
        Map<String, Object> data = new HashMap<>();
        data.put("items", qrcodes);
        return ResponseUtil.ok(data);
    }
}
