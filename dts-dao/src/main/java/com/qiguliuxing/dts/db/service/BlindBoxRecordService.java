package com.qiguliuxing.dts.db.service;

import com.qiguliuxing.dts.db.dao.BlindBoxRecordMapper;
import com.qiguliuxing.dts.db.dao.DtsUserMapper;
import com.qiguliuxing.dts.db.util.ActivityType;
import com.qiguliuxing.dts.db.util.StatusType;
import com.qiguliuxing.dts.vo.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * 盲盒记录服务
 */
@Service
public class BlindBoxRecordService {

    @Autowired
    private BlindBoxRecordMapper blindBoxRecordMapper;

    @Autowired
    private ProductService productService;

    @Autowired
    private BoxProductService boxProductService;

    @Autowired
    private BoxOrderService boxOrderService;
    @Autowired
    private DtsUserMapper dtsUserMapper;
    @Autowired
    private DtsUserService dtsUserService;

    /**
     * 获取指定系列和箱子的开赏记录
     * @param seriesId 系列ID
     * @param boxNumber 箱子编号
     * @return 开赏记录列表，如果没有记录则返回空列表
     */
    public List<BlindBoxRecordVO> getOpenRecords(Integer seriesId, String boxNumber) {
        List<BlindBoxRecordVO> records = blindBoxRecordMapper.selectOpenRecordsBySeriesAndBox(seriesId, boxNumber);
        return CollectionUtils.isEmpty(records) ? Collections.emptyList() : records;
    }



    /**
     * 查询指定系列所有的产品列表
     * @param seriesId 系列ID
     * @param boxNumber 箱子编号
     * @return 可用产品列表
     */
    public List<ProductBoxResultVo> selectAllProducts(Integer seriesId, String boxNumber){
        return blindBoxRecordMapper.selectAllProducts(seriesId, boxNumber);
    }


    /**
     * 抽取盲盒(魂力值抽奖)
     * @param request 请求参数
     * @return
     */
    @Transactional
    public List<BlindBoxDrawResultVO> drawBlindBoxBySpiritPower(BlindBoxDrawRequestVO request) {
        // 1. 查询可用的产品列表
        List<ProductBoxResultVo> spiritPowerProducts = blindBoxRecordMapper.selectSpiritPowerProducts(
                request.getSeriesId(), request.getBoxNumber());
        if (spiritPowerProducts.isEmpty()) {
            throw new RuntimeException("该盲盒已售罄或不存在");
        }
        // 2. 准备抽取的产品池（考虑库存数量）
        List<ProductBoxResultVo> productPool = new ArrayList<>();
        for (ProductBoxResultVo product : spiritPowerProducts) {
            int availableCount = product.getQuantity() - product.getSoldQuantity();
            for (int i = 0; i < availableCount; i++) {
                productPool.add(product);
            }
        }
        if (productPool.isEmpty()) {
            throw new RuntimeException("该盲盒已售罄");
        }
        // 3. 随机抽取产品
        List<BlindBoxDrawResultVO> results = new ArrayList<>();
        Random random = new Random();
        for (Integer number : request.getNumbers()) {
            // 随机选择一个产品
            ProductBoxResultVo selectedProduct = productPool.get(random.nextInt(productPool.size()));
            // 4. 更新库存
            int updated = blindBoxRecordMapper.updateSoldQuantity(
                    selectedProduct.getBoxId(),
                    selectedProduct.getProductId(),
                    1);
            if (updated == 0) {
                throw new RuntimeException("产品库存更新失败，可能已被其他用户购买");
            }


            ProductVO product = productService.getProductById(selectedProduct.getProductId());
            BoxProductVO boxProductVO = new BoxProductVO();
            boxProductVO.setActivityType(ActivityType.SOUL_POWER.getName());
            boxProductVO.setProductId(selectedProduct.getProductId());
            boxProductVO.setProductName(selectedProduct.getProductName());
            boxProductVO.setProductImage(product.getProductImage());
            boxProductVO.setProductLevel(product.getProductLevelName());
            boxProductVO.setStatus(StatusType.PENDING.getCode());
            boxProductVO.setProductLevel(product.getProductLevelName());
            boxProductVO.setBoxNumber(request.getBoxNumber());
            boxProductVO.setObtainTime(new Date());
            boxProductVO.setCreatedTime(new Date());
            boxProductVO.setUpdatedTime(new Date());
            boxProductVO.setUserId(request.getUserId());
            boxProductVO.setProductBadge(product.getProductBadge());
            // 抽中的产品入盒柜表
            int id = boxProductService.addProduct(boxProductVO);

            // 5. 记录抽取记录
            blindBoxRecordMapper.insertDrawRecord(
                    id,
                    request.getUserId(),
                    number,
                    request.getSeriesId(),
                    request.getBoxNumber(),
                    selectedProduct.getProductId());

            //写子表数据
            BoxOrderVO boxOrderVO = new BoxOrderVO();
            boxOrderVO.setOrderAmount(BigDecimal.valueOf(request.getSpiritPower()));
            boxOrderVO.setCouponDeduction(BigDecimal.valueOf(0));
            boxOrderVO.setShippingFee(BigDecimal.valueOf(0));
            boxOrderVO.setPointDeduction(BigDecimal.valueOf(0));
            boxOrderVO.setPaymentAmount(BigDecimal.valueOf(request.getSpiritPower()));
            boxOrderVO.setRecordId(boxProductVO.getId());
            boxOrderVO.setCreatedTime(new Date());
            boxOrderVO.setPaymentTime(new Date());
            boxOrderVO.setUpdatedTime(new Date());
            boxOrderService.createBoxOrder(boxOrderVO);

            // 6. 构建返回结果
            BlindBoxDrawResultVO result = new BlindBoxDrawResultVO();
            result.setBoxId(selectedProduct.getBoxId());
            result.setBoxNumber(selectedProduct.getBoxNumber());
            result.setProductId(selectedProduct.getProductId());
            result.setProductName(selectedProduct.getProductName());
            result.setProductImage(selectedProduct.getProductImage());
            result.setProductPrice(selectedProduct.getProductPrice());
            result.setLevelName(selectedProduct.getLevelName());
            result.setNumber(number);
            results.add(result);
        }
        // 更新用户魂力值
        dtsUserService.updateSpiritPower(request.getUserId(), request.getSpiritPower(), true);
        return results;
    }

    /**
     * 抽取盲盒
     * @param request 请求参数
     * @return
     */
    @Transactional
    public List<BlindBoxDrawResultVO> drawBlindBox(BlindBoxDrawRequestVO request) {
        // 1. 查询可用的产品列表
        List<ProductBoxResultVo> availableProducts = blindBoxRecordMapper.selectAvailableProducts(
                request.getSeriesId(), request.getBoxNumber());
        if (availableProducts.isEmpty()) {
            throw new RuntimeException("该盲盒已售罄或不存在");
        }
        // 2. 准备抽取的产品池（考虑库存数量）
        List<ProductBoxResultVo> productPool = new ArrayList<>();
        for (ProductBoxResultVo product : availableProducts) {
            int availableCount = product.getQuantity() - product.getSoldQuantity();
            for (int i = 0; i < availableCount; i++) {
                productPool.add(product);
            }
        }
        if (productPool.isEmpty()) {
            throw new RuntimeException("该盲盒已售罄");
        }
        // 3. 随机抽取产品
        List<BlindBoxDrawResultVO> results = new ArrayList<>();
        Random random = new Random();
        int spiritPower = 0;
        for (Integer number : request.getNumbers()) {
            // 随机选择一个产品
            ProductBoxResultVo selectedProduct = productPool.get(random.nextInt(productPool.size()));
            // 4. 更新库存
            int updated = blindBoxRecordMapper.updateSoldQuantity(
                    selectedProduct.getBoxId(),
                    selectedProduct.getProductId(),
                    1);
            if (updated == 0) {
                throw new RuntimeException("产品库存更新失败，可能已被其他用户购买");
            }

            ProductVO product = productService.getProductById(selectedProduct.getProductId());
            spiritPower += product.getProductSpiritPower();
            BoxProductVO boxProductVO = new BoxProductVO();
            boxProductVO.setActivityType(request.getActivityType());
            boxProductVO.setProductId(selectedProduct.getProductId());
            boxProductVO.setProductName(selectedProduct.getProductName());
            boxProductVO.setProductImage(product.getProductImage());
            boxProductVO.setProductLevel(product.getProductLevelName());
            boxProductVO.setStatus(StatusType.PENDING.getCode());
            boxProductVO.setProductLevel(product.getProductLevelName());
            boxProductVO.setBoxNumber(request.getBoxNumber());
            boxProductVO.setObtainTime(new Date());
            boxProductVO.setCreatedTime(new Date());
            boxProductVO.setUpdatedTime(new Date());
            boxProductVO.setUserId(request.getUserId());
            boxProductVO.setProductBadge(product.getProductBadge());
            // 抽中的产品入盒柜表
            int id = boxProductService.addProduct(boxProductVO);

            // 5. 记录抽取记录
            blindBoxRecordMapper.insertDrawRecord(
                    id,
                    request.getUserId(),
                    number,
                    request.getSeriesId(),
                    request.getBoxNumber(),
                    selectedProduct.getProductId());

            //写子表数据
            BoxOrderVO boxOrderVO = new BoxOrderVO();
            boxOrderVO.setOrderAmount(request.getOrderAmount());
            boxOrderVO.setCouponDeduction(request.getCouponDeduction());
            boxOrderVO.setShippingFee(BigDecimal.valueOf(0));
            boxOrderVO.setPointDeduction(request.getPointDeduction());
            boxOrderVO.setPaymentAmount(request.getPaymentAmount());
            boxOrderVO.setRecordId(boxProductVO.getId());
            boxOrderVO.setCreatedTime(new Date());
            boxOrderVO.setPaymentTime(new Date());
            boxOrderVO.setUpdatedTime(new Date());
            boxOrderService.createBoxOrder(boxOrderVO);

            // 6. 构建返回结果
            BlindBoxDrawResultVO result = new BlindBoxDrawResultVO();
            result.setBoxId(selectedProduct.getBoxId());
            result.setBoxNumber(selectedProduct.getBoxNumber());
            result.setProductId(selectedProduct.getProductId());
            result.setProductName(selectedProduct.getProductName());
            result.setProductImage(selectedProduct.getProductImage());
            result.setProductPrice(selectedProduct.getProductPrice());
            result.setLevelName(selectedProduct.getLevelName());
            result.setNumber(number);
            results.add(result);
        }
        // 更新用户魂力值
        dtsUserService.updateSpiritPower(request.getUserId(), spiritPower, true);
        return results;
    }
}
