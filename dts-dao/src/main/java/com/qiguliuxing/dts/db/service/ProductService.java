package com.qiguliuxing.dts.db.service;

import com.qiguliuxing.dts.db.dao.ProductMapper;
import com.qiguliuxing.dts.vo.ProductVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class ProductService {

    @Resource
    private ProductMapper productMapper;

    @Transactional
    public void addProduct(ProductVO product) {
        productMapper.insertProduct(product);
    }

    @Transactional
    public void deleteProduct(Integer productId) {
        productMapper.deleteProduct(productId);
    }

    @Transactional
    public void updateProduct(ProductVO product) {
        productMapper.updateProduct(product);
    }

    public ProductVO getProductById(Integer productId) {
        return productMapper.getProductById(productId);
    }

    public List<ProductVO> getAllProducts() {
        return productMapper.getAllProducts();
    }

    public List<ProductVO> getProductsByCondition(Map<String, Object> condition) {
        return productMapper.getProductsByCondition(condition);
    }

}
