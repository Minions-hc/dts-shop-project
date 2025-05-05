package com.qiguliuxing.dts.db.service.impl;

import com.qiguliuxing.dts.db.dao.MarketSeriesMapper;
import com.qiguliuxing.dts.db.service.IMarketSeriesService;
import com.qiguliuxing.dts.vo.MarketProductSeriesVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 集市换娃后台管理服务
 */
@Service
public class MarketSeriesServiceImpl implements IMarketSeriesService {

    @Resource
    private MarketSeriesMapper marketSeriesMapper;

    @Override
    public void addMarketSeries(MarketProductSeriesVO marketProductSeries) {
        marketSeriesMapper.insertMarketSeries(marketProductSeries);
    }

    @Override
    public void deleteMarketSeries(Integer seriesId) {
        marketSeriesMapper.deleteMarketSeries(seriesId);
    }

    @Override
    public void updateMarketSeries(MarketProductSeriesVO marketProductSeries) {
        marketSeriesMapper.updateMarketSeries(marketProductSeries);
    }

    @Override
    public MarketProductSeriesVO getMarketSeriesById(Integer seriesId) {
        return marketSeriesMapper.getMarketSeriesById(seriesId);
    }

    @Override
    public List<MarketProductSeriesVO> getMarketSeries(Map<String, Object> params) {
        return marketSeriesMapper.getMarketSeries(params);
    }

    @Override
    public List<MarketProductSeriesVO> getWxMarketSeries() {
        return marketSeriesMapper.getWxMarketSeries();
    }
}