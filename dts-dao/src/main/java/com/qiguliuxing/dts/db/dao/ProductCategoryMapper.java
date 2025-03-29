package com.qiguliuxing.dts.db.dao;

import com.qiguliuxing.dts.vo.ProductCategoryVO;

import java.util.List;
import java.util.Map;

public interface ProductCategoryMapper {
    void insertProductCategory(ProductCategoryVO productCategory);

    void deleteProductCategory(Integer categoryId);

    void updateProductCategory(ProductCategoryVO productCategory);

    ProductCategoryVO getProductCategoryById(Integer categoryId);

    List<ProductCategoryVO> getProductCategories(Map<String, String> params);
}