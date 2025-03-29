package com.qiguliuxing.dts.db.service.impl;

import com.qiguliuxing.dts.db.dao.ProductSeriesMapper;
import com.qiguliuxing.dts.db.service.IProductSeriesService;
import com.qiguliuxing.dts.vo.ProductSeriesVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class ProductSeriesServiceImpl implements IProductSeriesService {

    @Resource
    private ProductSeriesMapper productSeriesMapper;

    @Override
    @Transactional
    public void addProductSeries(ProductSeriesVO productSeries) {
        productSeriesMapper.insertProductSeries(productSeries);
    }

    @Override
    @Transactional
    public void deleteProductSeries(Integer seriesId) {
        productSeriesMapper.deleteProductSeries(seriesId);
    }

    @Override
    @Transactional
    public void updateProductSeries(ProductSeriesVO productSeries) {
        productSeriesMapper.updateProductSeries(productSeries);
    }

    @Override
    public ProductSeriesVO getProductSeriesById(Integer seriesId) {
        return productSeriesMapper.getProductSeriesById(seriesId);
    }

    @Override
    public List<ProductSeriesVO> getProductSeries(Map<String, String> params) {
        return productSeriesMapper.getProductSeries(params);
    }
}
