package com.qiguliuxing.dts.db.service;

import com.qiguliuxing.dts.vo.ProductSeriesVO;

import java.util.List;
import java.util.Map;

public interface IProductSeriesService {

    void addProductSeries(ProductSeriesVO productSeries);
    void deleteProductSeries(Integer seriesId);
    void updateProductSeries(ProductSeriesVO productSeries);
    ProductSeriesVO getProductSeriesById(Integer seriesId);


    List<ProductSeriesVO> getProductSeries(Map<String, Object> params);


    /**
     *  查询小程序客户端所有系列数据
     * @param params
     * @return
     */
    List<ProductSeriesVO> getWxProductSeries(Map<String, Object> params);

}
