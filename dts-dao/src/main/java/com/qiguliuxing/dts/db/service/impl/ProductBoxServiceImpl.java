package com.qiguliuxing.dts.db.service.impl;

import com.qiguliuxing.dts.db.dao.ProductBoxMapper;
import com.qiguliuxing.dts.db.service.IProductBoxService;
import com.qiguliuxing.dts.vo.ProductBoxVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ProductBoxServiceImpl implements IProductBoxService {

    @Resource
    private ProductBoxMapper productBoxMapper;

    @Override
    @Transactional
    public void addProductBox(ProductBoxVO productBox) {
        productBoxMapper.insertProductBox(productBox);
    }

    @Override
    @Transactional
    public void deleteProductBox(Integer seriesId, String boxNumber, Integer productId) {
        productBoxMapper.deleteProductBox(seriesId, boxNumber, productId);
    }

    @Override
    @Transactional
    public void updateProductBox(ProductBoxVO productBox) {
        productBoxMapper.updateProductBox(productBox);
    }

    @Override
    public ProductBoxVO getProductBoxById(Integer seriesId, String boxNumber, Integer productId) {
        return productBoxMapper.getProductBoxById(seriesId, boxNumber, productId);
    }

    @Override
    public List<ProductBoxVO> getProductBoxesBySeriesId(Integer seriesId) {
        return productBoxMapper.getProductBoxesBySeriesId(seriesId);
    }

}
