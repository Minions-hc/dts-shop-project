package com.qiguliuxing.dts.db.dao;

import com.qiguliuxing.dts.vo.MarketProductSeriesVO;

import java.util.List;
import java.util.Map;

public interface MarketSeriesMapper {

    void insertMarketSeries(MarketProductSeriesVO marketProductSeries);

    void deleteMarketSeries(Integer seriesId);

    void updateMarketSeries(MarketProductSeriesVO marketProductSeries);

    MarketProductSeriesVO getMarketSeriesById(Integer seriesId);

    List<MarketProductSeriesVO> getMarketSeries(Map<String, Object> params);

    List<MarketProductSeriesVO> getWxMarketSeries();
}