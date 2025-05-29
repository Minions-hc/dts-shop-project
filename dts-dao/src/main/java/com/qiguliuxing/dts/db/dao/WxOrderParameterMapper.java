package com.qiguliuxing.dts.db.dao;


import com.qiguliuxing.dts.vo.WxOrderParameter;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 微信支付订单参数Mapper接口
 */
@Mapper
public interface WxOrderParameterMapper {

    /**
     * 插入订单参数
     * @param parameter 订单参数对象
     * @return 影响的行数
     */
    int insert(WxOrderParameter parameter);

    /**
     * 根据商户订单号查询订单参数
     * @param outTradeNo 商户订单号
     * @return 订单参数对象
     */
    WxOrderParameter selectByOutTradeNo(String outTradeNo);

    /**
     * 根据用户ID查询订单列表
     * @param userId 用户ID
     * @return 订单参数列表
     */
    List<WxOrderParameter> selectByUserId(String userId);
}
