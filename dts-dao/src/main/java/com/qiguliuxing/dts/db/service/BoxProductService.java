package com.qiguliuxing.dts.db.service;

import com.qiguliuxing.dts.db.dao.BoxProductMapper;
import com.qiguliuxing.dts.db.dao.DtsAddressMapper;
import com.qiguliuxing.dts.db.dao.DtsOrderMapper;
import com.qiguliuxing.dts.db.util.OrderNoGenerator;
import com.qiguliuxing.dts.db.util.StatusType;
import com.qiguliuxing.dts.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class BoxProductService {

    @Autowired
    private BoxProductMapper boxProductMapper;

    @Autowired
    private DtsOrderMapper dtsOrderMapper;

    @Autowired
    private DtsAddressMapper dtsAddressMapper;

    @Autowired
    private BoxOrderService boxOrderService;

    public List<BoxProductVO> getProductsByUserId(String userId, List<String> statusList) {
        return boxProductMapper.selectByUserId(userId, statusList);
    }

    @Transactional
    public int addProduct(BoxProductVO boxProduct) {
        return boxProductMapper.insert(boxProduct);
    }

    @Transactional
    public int updateProductStatus(BoxProductVO boxProduct) {
        return boxProductMapper.updateSelective(boxProduct);
    }


    /**
     * 盒柜产品提货
     */
    @Transactional
    public void shipProducts(String userId, List<Integer> boxProductIds) {

        if (CollectionUtils.isEmpty(boxProductIds)){
            throw new RuntimeException("参数错误");
        }

        List<String> statusList = new ArrayList<>();
        statusList.add("pending");
        List<BoxProductVO> boxProductVOS = boxProductMapper.selectByUserId(userId, statusList);
        List<BoxProductVO> matchedProducts = boxProductVOS.stream().filter(boxProductVO -> boxProductIds.contains(boxProductVO.getId())).collect(Collectors.toList());

        // 4. 验证筛选结果
        if (matchedProducts.size() != boxProductIds.size()) {
            throw new RuntimeException("部分商品不存在或不属于该用户");
        }
        AddressVO addressVO = dtsAddressMapper.selectDefaultAddressByUserId(userId);
        Map<Integer, OrderItemVO> orderItemVOMap = new HashMap<>();
        for (BoxProductVO boxProductVO : matchedProducts) {
            OrderItemVO orderItemVO = null;
            if(orderItemVOMap.containsKey(boxProductVO.getProductId())) {
                orderItemVO = orderItemVOMap.get(boxProductVO.getProductId());
                orderItemVO.setQuantity(orderItemVO.getQuantity() + 1);
            } else {
                orderItemVO = new OrderItemVO();
                orderItemVO.setProductId(boxProductVO.getProductId());
                orderItemVO.setProductName(boxProductVO.getProductName());
                orderItemVO.setProductImg(boxProductVO.getProductImage());
                orderItemVO.setCreateBy(userId);
                orderItemVO.setUpdateBy(userId);
                orderItemVO.setCreateTime(new Date());
                orderItemVO.setUpdateTime(new Date());
            }
            orderItemVOMap.put(boxProductVO.getProductId(), orderItemVO);
        }

        int shippingFee = boxProductIds.size() < 3 ? 12 : 0;
        // 2. 创建订单
        OrderVO order = new OrderVO();
        order.setUserId(userId);
        order.setOrderNo(OrderNoGenerator.generate());
        order.setAddressId(addressVO.getAddressId());
        order.setShippingFee(BigDecimal.valueOf(shippingFee));
        order.setCreateBy(order.getUserId());
        order.setUpdateBy(order.getUserId());
        order.setCreateTime(new Date());
        order.setUpdateTime(new Date());
        order.setOrderStatus("3");
        int orderResult = dtsOrderMapper.insertOrder(order);
        if (orderResult <= 0) {
            throw new RuntimeException("订单创建失败");
        }

        for (Integer productId : orderItemVOMap.keySet()) {
            OrderItemVO orderItem = orderItemVOMap.get(productId);
            int itemResult = dtsOrderMapper.insertOrderItem(orderItem);
            if (itemResult <= 0) {
                throw new RuntimeException("订单商品添加失败");
            }
        }


        for (BoxProductVO boxProductVO : boxProductVOS){
            // 4. 更新盒柜产品状态
            // 更新盒柜表状态
            boxProductVO.setStatus(StatusType.SHIPPED.getCode());
            int updateResult = boxProductMapper.updateSelective(boxProductVO);
            if (updateResult <= 0) {
                throw new RuntimeException("盒柜产品状态更新失败");
            }
        }
    }
}
