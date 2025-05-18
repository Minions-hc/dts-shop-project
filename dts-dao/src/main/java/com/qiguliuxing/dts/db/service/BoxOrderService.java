package com.qiguliuxing.dts.db.service;

import com.qiguliuxing.dts.db.dao.BoxOrderMapper;
import com.qiguliuxing.dts.vo.BoxOrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 盒柜订单支付信息服务
 */
@Service
public class BoxOrderService {

    @Autowired
    private BoxOrderMapper boxOrderMapper;

    /**
     * 创建盒柜订单支付信息
     */
    @Transactional
    public int createBoxOrder(BoxOrderVO createVO) {
        int result = boxOrderMapper.insert(createVO);
        if (result <= 0) {
            throw new RuntimeException("创建盒柜订单支付信息失败");
        }
        return result;
    }

    /**
     * 更新盒柜订单支付信息
     */
    @Transactional
    public int updateBoxOrder(BoxOrderVO createVO) {

        int result = boxOrderMapper.update(createVO);
        if (result <= 0) {
            throw new RuntimeException("更新盒柜订单支付信息失败");
        }
        return result;
    }

    /**
     * 根据盒柜商品ID查询支付信息
     */
    public BoxOrderVO getBoxOrderByRecordId(Integer recordId) {
        return boxOrderMapper.selectByRecordId(recordId);
    }

    /**
     * 根据盒柜商品ID查询支付信息
     */
    public List<BoxOrderVO> getBoxOrderByRecordIds(List<Integer> recordIds) {
        return boxOrderMapper.selectByRecordIds(recordIds);
    }

    /**
     * 根据业务订单ID查询支付信息
     */
    public BoxOrderVO getBoxOrderByOrderId(Integer orderId) {
        return boxOrderMapper.selectByOrderId(orderId);
    }
}
