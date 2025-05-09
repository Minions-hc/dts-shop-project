package com.qiguliuxing.dts.db.dao;

import com.qiguliuxing.dts.vo.BoxProductVO;
import com.qiguliuxing.dts.vo.MarketProductVO;
import org.apache.ibatis.annotations.Param;

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

    Integer getCurrentBadge(String userId);

    List<BoxProductVO> getBoxProductList(String userId);

    /**
     * 批量更新用户ID
     * @param list 包含id和userId的实体列表
     */
    void batchUpdateUserId(@Param("list") List<Integer> list);
}
