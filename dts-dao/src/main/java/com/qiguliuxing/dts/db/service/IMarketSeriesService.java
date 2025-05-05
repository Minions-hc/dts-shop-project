package com.qiguliuxing.dts.db.service;

import com.qiguliuxing.dts.vo.MarketProductSeriesVO;

import java.util.List;
import java.util.Map;

/**
 * 集市换娃产品系列后台管理服务
 */
public interface IMarketSeriesService {

    void addMarketSeries(MarketProductSeriesVO marketProductSeries);
    void deleteMarketSeries(Integer seriesId);
    void updateMarketSeries(MarketProductSeriesVO marketProductSeries);
    MarketProductSeriesVO getMarketSeriesById(Integer seriesId);


    List<MarketProductSeriesVO> getMarketSeries(Map<String, Object> params);


    /**
     *  查询小程序客户端集市换娃所有系列数据
     * @return 集市换娃所有系列数据
     */
    List<MarketProductSeriesVO> getWxMarketSeries();

}