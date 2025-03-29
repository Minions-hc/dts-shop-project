package com.qiguliuxing.dts.db.service;

import com.qiguliuxing.dts.vo.ProductCategoryVO;

import java.util.List;
import java.util.Map;

public interface IProductCategoryService {

    void addProductCategory(ProductCategoryVO productCategory);
    void deleteProductCategory(Integer categoryId);
    void updateProductCategory(ProductCategoryVO productCategory);
    ProductCategoryVO getProductCategoryById(Integer categoryId);
    List<ProductCategoryVO> getProductCategories( Map<String, String> params);
}
