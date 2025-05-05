package com.qiguliuxing.dts.db.service;

import com.qiguliuxing.dts.db.dao.MarketProductMapper;
import com.qiguliuxing.dts.vo.MarketProductVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 集市换娃产品后台管理服务
 */
@Service
public class MarketProductService {

    @Resource
    private MarketProductMapper marketProductMapper;

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
}