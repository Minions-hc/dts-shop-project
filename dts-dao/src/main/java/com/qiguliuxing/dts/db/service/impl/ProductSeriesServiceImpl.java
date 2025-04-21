package com.qiguliuxing.dts.db.service.impl;

import com.qiguliuxing.dts.db.dao.ProductMapper;
import com.qiguliuxing.dts.db.dao.ProductSeriesMapper;
import com.qiguliuxing.dts.db.service.IProductSeriesService;
import com.qiguliuxing.dts.vo.ProductSeriesVO;
import com.qiguliuxing.dts.vo.ProductVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductSeriesServiceImpl implements IProductSeriesService {

    @Resource
    private ProductSeriesMapper productSeriesMapper;

    @Resource
    private ProductMapper productMapper;

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
    public List<ProductSeriesVO> getProductSeries(Map<String, Object> params) {
        return productSeriesMapper.getProductSeries(params);
    }


    @Override
    public List<ProductSeriesVO> getWxProductSeries(Map<String, Object> params) {
        // 根据条件查询对应的系列
        List<ProductSeriesVO> productSeriesVOS = productSeriesMapper.getProductSeries(params);
        List<ProductSeriesVO> sumSeriesQuantityList = productSeriesMapper.sumSeriesQuantity();
        Map<Integer, Integer> seriesIdToQuantityMap = sumSeriesQuantityList.stream().collect(Collectors.toMap(ProductSeriesVO ::getSeriesId, ProductSeriesVO::getPurchaseCount));
        Integer purchaseCount;
        for (ProductSeriesVO productSeriesVO : productSeriesVOS) {
            purchaseCount = seriesIdToQuantityMap.get(productSeriesVO.getSeriesId()) == null ? 0 : seriesIdToQuantityMap.get(productSeriesVO.getSeriesId());
            productSeriesVO.setPurchaseCount(purchaseCount);
        }
        return productSeriesVOS;
    }
}
