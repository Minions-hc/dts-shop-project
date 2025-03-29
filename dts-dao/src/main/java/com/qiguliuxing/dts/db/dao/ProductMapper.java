package com.qiguliuxing.dts.db.dao;

import com.qiguliuxing.dts.vo.ProductVO;

import java.util.List;
import java.util.Map;

public interface ProductMapper {
    void insertProduct(ProductVO product);

    void deleteProduct(Integer productId);

    void updateProduct(ProductVO product);

    ProductVO getProductById(Integer productId);

    List<ProductVO> getAllProducts();

    List<ProductVO> getProductsByCondition(Map<String, Object> condition);
}