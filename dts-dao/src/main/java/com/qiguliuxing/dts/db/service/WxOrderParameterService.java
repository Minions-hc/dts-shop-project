package com.qiguliuxing.dts.db.service;

import com.qiguliuxing.dts.db.dao.WxOrderParameterMapper;
import com.qiguliuxing.dts.vo.WxOrderParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 微信支付订单参数服务
 */
@Service
public class WxOrderParameterService {

    @Autowired
    private  WxOrderParameterMapper wxOrderParameterMapper;

    /**
     * 保存订单参数
     * @param parameter 订单参数对象
     * @return 是否保存成功
     */
    @Transactional
    public int saveOrderParameter(WxOrderParameter parameter) {
        return wxOrderParameterMapper.insert(parameter);
    }

    /**
     * 根据商户订单号获取订单参数
     * @param outTradeNo 商户订单号
     * @return 订单参数对象
     */
    public WxOrderParameter getByOutTradeNo(String outTradeNo) {
        return wxOrderParameterMapper.selectByOutTradeNo(outTradeNo);
    }

    /**
     * 根据用户ID获取订单列表
     * @param userId 用户ID
     * @return 订单参数列表
     */
    public List<WxOrderParameter> getByUserId(String userId) {
        return wxOrderParameterMapper.selectByUserId(userId);
    }
}
