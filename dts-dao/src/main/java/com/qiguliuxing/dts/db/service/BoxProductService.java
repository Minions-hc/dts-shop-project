package com.qiguliuxing.dts.db.service;

import com.qiguliuxing.dts.db.dao.BoxOrderMapper;
import com.qiguliuxing.dts.db.dao.BoxProductMapper;
import com.qiguliuxing.dts.db.dao.DtsAddressMapper;
import com.qiguliuxing.dts.db.dao.DtsOrderMapper;
import com.qiguliuxing.dts.db.util.OrderNoGenerator;
import com.qiguliuxing.dts.db.util.OrderUtil;
import com.qiguliuxing.dts.db.util.StatusType;
import com.qiguliuxing.dts.vo.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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
    private DtsOrderService dtsOrderService;

    @Autowired
    private DtsAddressMapper dtsAddressMapper;

    @Autowired
    private BoxOrderService boxOrderService;

    @Autowired
    private WxOrderParameterService wxOrderParameterService;

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
    public void shipProducts(String userId, List<Integer> ids, String outTradeNo) {
        WxOrderParameter wxOrderParameter = null;
        if (!StringUtils.isEmpty(outTradeNo)) {
            wxOrderParameter = wxOrderParameterService.getByOutTradeNo(outTradeNo);
        }

        if (CollectionUtils.isEmpty(ids)){
            throw new RuntimeException("参数错误");
        }
        // 只有提交少于三个且没有给运费的才需要抛异常
        if (ids.size() < 3 && StringUtils.isEmpty(outTradeNo)) {
            throw new RuntimeException("提货低于3个，需要调用支付接口支付运费");
        }
        List<String> statusList = new ArrayList<>();
        statusList.add("pending");
        List<BoxProductVO> boxProductVOS = boxProductMapper.selectByUserId(userId, statusList);
        List<BoxProductVO> matchedProducts = boxProductVOS.stream().filter(boxProductVO -> ids.contains(boxProductVO.getId())).collect(Collectors.toList());

        List<BoxOrderVO> boxOrders = boxOrderService.getBoxOrderByRecordIds(ids);

        // 4. 验证筛选结果
        if (matchedProducts.size() != ids.size()) {
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
                orderItemVO.setQuantity(1);
                orderItemVO.setCreateTime(new Date());
                orderItemVO.setUpdateTime(new Date());
            }
            orderItemVOMap.put(boxProductVO.getProductId(), orderItemVO);
        }
        // 2. 创建订单
        OrderVO order = new OrderVO();
        order.setUserId(userId);
        order.setOrderNo(OrderNoGenerator.generate());
        order.setAddressId(addressVO.getAddressId());
        order.setCreateBy(order.getUserId());
        order.setUpdateBy(order.getUserId());
        order.setOrderAmount(BigDecimal.valueOf(0));
        order.setPaymentAmount(BigDecimal.valueOf(0));
        order.setCreateTime(new Date());
        order.setUpdateTime(new Date());
        order.setOrderStatus(OrderUtil.WAIT_SHIPPING);
        if (wxOrderParameter != null) {
            order.setShippingFee(wxOrderParameter.getPaymentAmount());
            order.setOrderAmount(wxOrderParameter.getOrderAmount());
            order.setPaymentAmount(wxOrderParameter.getPaymentAmount());
            order.setWxOrderNo(wxOrderParameter.getWxOrderNo());
        } else {
            order.setShippingFee(BigDecimal.valueOf(0));
            order.setOrderAmount(BigDecimal.valueOf(0));
            order.setPaymentAmount(BigDecimal.valueOf(0));
        }

        int result = dtsOrderService.insertOrder(order);
        if (result == 0) {
            throw new RuntimeException("订单创建失败");
        }

        for (Integer productId : orderItemVOMap.keySet()) {
            OrderItemVO orderItem = orderItemVOMap.get(productId);
            orderItem.setOrderId(order.getOrderId());
            int itemResult = dtsOrderMapper.insertOrderItem(orderItem);
            if (itemResult <= 0) {
                throw new RuntimeException("订单商品添加失败");
            }
        }


        for (BoxProductVO boxProductVO : matchedProducts){
            // 4. 更新盒柜产品状态
            // 更新盒柜表状态
            boxProductVO.setStatus(StatusType.SHIPPED.getCode());
            boxProductVO.setOrderId(order.getOrderId());
            int updateResult = boxProductMapper.updateSelective(boxProductVO);
            if (updateResult <= 0) {
                throw new RuntimeException("盒柜产品状态更新失败");
            }
        }
    }

    /**
     * 动态查询盒柜商品
     * @param userId 用户ID
     * @param activityType 活动类型
     * @param status 状态
     * @return 盒柜商品列表
     */
    public List<BoxProductVO> queryBoxProducts(String userId, String activityType, String status) {
        // 将逗号分隔的字符串转换为List
        List<String> activityTypeList = null;
        if (StringUtils.hasText(activityType)) {
            activityTypeList = Arrays.asList(activityType.split(","));
        }

        List<String> statusList = null;
        if (StringUtils.hasText(status)) {
            statusList = Arrays.asList(status.split(","));
        }
        return boxProductMapper.selectBoxProducts(userId, activityTypeList, statusList);
    }

    public BoxProductVO selectByIdAndUserId( Integer id, String userId){
        return boxProductMapper.selectByIdAndUserId(id, userId);
    }

    public List<BoxProductVO> selectBoxProductsByWxOrderNo(String wxOrderNo){
        return boxProductMapper.selectBoxProductsByWxOrderNo(wxOrderNo);
    }

    // 查询用户最后的消费记录
    public BoxProductVO queryLastConsumption(String userId){
        return boxProductMapper.queryLastConsumption(userId);
    }
}
