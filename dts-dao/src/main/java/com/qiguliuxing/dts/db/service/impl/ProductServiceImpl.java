package com.qiguliuxing.dts.db.service.impl;

import com.qiguliuxing.dts.db.dao.ProductMapper;
import com.qiguliuxing.dts.db.service.IProductService;
import com.qiguliuxing.dts.vo.ProductVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements IProductService {

    @Resource
    private ProductMapper productMapper;

    @Override
    @Transactional
    public void addProduct(ProductVO product) {
        productMapper.insertProduct(product);
    }

    @Override
    @Transactional
    public void deleteProduct(Integer productId) {
        productMapper.deleteProduct(productId);
    }

    @Override
    @Transactional
    public void updateProduct(ProductVO product) {
        productMapper.updateProduct(product);
    }

    @Override

    public ProductVO getProductById(Integer productId) {
        return productMapper.getProductById(productId);
    }

    @Override
    public List<ProductVO> getAllProducts() {
        return productMapper.getAllProducts();
    }
    @Override
    public List<ProductVO> getProductsByCondition(Map<String, Object> condition) {
        return productMapper.getProductsByCondition(condition);
    }

}
