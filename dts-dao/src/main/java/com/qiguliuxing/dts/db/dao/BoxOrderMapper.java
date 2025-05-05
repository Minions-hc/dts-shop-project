package com.qiguliuxing.dts.db.dao;

import com.qiguliuxing.dts.vo.BoxOrderVO;
import org.apache.ibatis.annotations.Param;

/**
 * 盒柜订单支付信息Mapper
 */
public interface  BoxOrderMapper {

    int insert(BoxOrderVO boxOrder);
    int update(BoxOrderVO boxOrder);
    BoxOrderVO selectByRecordId(@Param("recordId") Integer recordId);

    BoxOrderVO selectByOrderId(@Param("orderId") Integer orderId);
}
