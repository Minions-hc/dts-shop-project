package com.qiguliuxing.dts.db.dao;

import com.qiguliuxing.dts.vo.WechatGroupQrcodeVO;

import java.util.List;

public interface WechatGroupQrcodeMapper {

    int insert(WechatGroupQrcodeVO qrcode);
    int deleteById(Integer id);
    int update(WechatGroupQrcodeVO qrcode);
    WechatGroupQrcodeVO selectById(Integer id);
    List<WechatGroupQrcodeVO> selectAll();
}
