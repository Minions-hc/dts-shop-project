package com.qiguliuxing.dts.db.service;

import com.qiguliuxing.dts.db.dao.WechatGroupQrcodeMapper;
import com.qiguliuxing.dts.vo.WechatGroupQrcodeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WechatGroupQrcodeService {


    @Autowired
    private WechatGroupQrcodeMapper qrcodeMapper;

    public int addQrcode(WechatGroupQrcodeVO qrcode) {
        return qrcodeMapper.insert(qrcode);
    }

    public int deleteQrcode(Integer id) {
        return qrcodeMapper.deleteById(id);
    }

    public int updateQrcode(WechatGroupQrcodeVO qrcode) {
        return qrcodeMapper.update(qrcode);
    }

    public WechatGroupQrcodeVO getQrcodeById(Integer id) {
        return qrcodeMapper.selectById(id);
    }

    public List<WechatGroupQrcodeVO> getAllQrcodes() {
        return qrcodeMapper.selectAll();
    }
}
