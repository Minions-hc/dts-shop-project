package com.qiguliuxing.dts.db.dao;

import com.qiguliuxing.dts.vo.MarketProductVO;

import java.util.List;
import java.util.Map;

public interface MarketProductMapper {
    void insertMarketProduct(MarketProductVO marketProduct);

    void deleteMarketProduct(Integer productId);

    void updateMarketProduct(MarketProductVO marketProduct);

    MarketProductVO getMarketProductById(Integer productId);

    List<MarketProductVO> getAllMarketProducts();

    List<MarketProductVO> getMarketProductsByCondition(Map<String, Object> condition);

    List<MarketProductVO> getWxMarketProductBySeriesId(Integer seriesId);
}