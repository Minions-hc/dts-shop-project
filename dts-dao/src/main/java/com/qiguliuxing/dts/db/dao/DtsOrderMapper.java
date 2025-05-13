package com.qiguliuxing.dts.db.dao;

import java.util.List;
import java.util.Map;

import com.qiguliuxing.dts.vo.OrderDetailVO;
import com.qiguliuxing.dts.vo.OrderItemVO;
import com.qiguliuxing.dts.vo.OrderVO;
import org.apache.ibatis.annotations.Param;

public interface DtsOrderMapper {


    List<OrderVO> queryOrderList(Map<String,Object> params);

    OrderDetailVO queryOrderDetail(@Param("orderNo") String orderNo);

    List<OrderItemVO> queryOrderItems(@Param("orderId") Integer orderId);

    OrderVO queryOrderByOrderNo(@Param("orderNo") String orderNo);

    void updateShippingInfo(OrderVO order);

    /**
     * 插入订单商品
     */
    int insertOrderItem(OrderItemVO orderItemVO);

    /**
     * 插入订单
     */
    int insertOrder(OrderVO order);
}
