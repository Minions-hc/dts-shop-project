package com.qiguliuxing.dts.db.service;

import com.qiguliuxing.dts.db.dao.MarketProductMapper;
import com.qiguliuxing.dts.db.util.ActivityType;
import com.qiguliuxing.dts.db.util.StatusType;
import com.qiguliuxing.dts.vo.BoxProductVO;
import com.qiguliuxing.dts.vo.MarketProductVO;
import com.qiguliuxing.dts.vo.ProductVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 集市换娃产品后台管理服务
 */
@Service
public class MarketProductService {

    @Resource
    private MarketProductMapper marketProductMapper;

    @Resource
    BoxProductService boxProductService;

    @Transactional
    public void addMarketProduct(MarketProductVO marketProduct) {
        marketProductMapper.insertMarketProduct(marketProduct);
    }

    @Transactional
    public void deleteMarketProduct(Integer ProductId) {
        marketProductMapper.deleteMarketProduct(ProductId);
    }

    @Transactional
    public void updateMarketProduct(MarketProductVO marketProduct) {
        marketProductMapper.updateMarketProduct(marketProduct);
    }

    public MarketProductVO getMarketProductById(Integer productId) {
        return marketProductMapper.getMarketProductById(productId);
    }

    public List<MarketProductVO> getAllMarketProducts() {
        return marketProductMapper.getAllMarketProducts();
    }

    /**
     *  查询集市换娃产品数据
     * @return 查询集市换娃产品数据
     */
    public List<MarketProductVO> getMarketProduct(Map<String, Object> params) {
        return marketProductMapper.getMarketProductsByCondition(params);
    }

    /**
     *  查询小程序客户端集市换娃当前系列产品数据
     * @return 集市换娃所有系列数据
     */
    public List<MarketProductVO> getWxMarketProductBySeriesId(Integer seriesId) {
        return marketProductMapper.getWxMarketProductBySeriesId(seriesId);
    }

    public Integer getCurrentBadge(String userId) {
        return marketProductMapper.getCurrentBadge(userId);
    }

    public List<BoxProductVO> getBoxProductList(String userId) {
        return marketProductMapper.getBoxProductList(userId);
    }

    public void redeemProduct(List<BoxProductVO> boxProductList, Integer productId) {
        if (CollectionUtils.isEmpty(boxProductList)){
            return;
        }
        MarketProductVO marketProduct = marketProductMapper.getMarketProductById(productId);
        String userId = boxProductList.get(0).getUserId();
        int totalBadges = boxProductList.stream()
                .mapToInt(BoxProductVO::getProductBadge)
                .sum();
        if (totalBadges < marketProduct.getProductBadge()){
            return;
        }
        BoxProductVO boxProductVO = new BoxProductVO();
        boxProductVO.setActivityType(ActivityType.MARKET_EXCHANGE.getName());
        boxProductVO.setProductId(marketProduct.getProductId());
        boxProductVO.setProductName(marketProduct.getProductName());
        boxProductVO.setProductImage(marketProduct.getProductImage());
        boxProductVO.setProductLevel(marketProduct.getProductLevelName());
        boxProductVO.setStatus(StatusType.PENDING.getCode());
        boxProductVO.setObtainTime(new Date());
        boxProductVO.setCreatedTime(new Date());
        boxProductVO.setUpdatedTime(new Date());
        boxProductVO.setUserId(userId);
        boxProductVO.setProductBadge(0);
        // 兑换产品入盒柜表
        boxProductService.addProduct(boxProductVO);

        for (BoxProductVO boxProduct : boxProductList) {
            boxProduct.setUserId("admin");
        }
        marketProductMapper.batchUpdateUserId(boxProductList);
    }
}
