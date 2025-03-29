package com.qiguliuxing.dts.db.service.impl;

import com.qiguliuxing.dts.db.dao.ProductCategoryMapper;
import com.qiguliuxing.dts.db.service.IProductCategoryService;
import com.qiguliuxing.dts.vo.ProductCategoryVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class ProductCategoryServiceImpl implements IProductCategoryService {


    @Resource
    private ProductCategoryMapper productCategoryMapper;

    @Override
    @Transactional
    public void addProductCategory(ProductCategoryVO productCategory) {
        productCategoryMapper.insertProductCategory(productCategory);
    }

    @Override
    @Transactional
    public void deleteProductCategory(Integer categoryId) {
        productCategoryMapper.deleteProductCategory(categoryId);
    }

    @Override
    @Transactional
    public void updateProductCategory(ProductCategoryVO productCategory) {
        productCategoryMapper.updateProductCategory(productCategory);
    }

    @Override
    public ProductCategoryVO getProductCategoryById(Integer categoryId) {
        return productCategoryMapper.getProductCategoryById(categoryId);
    }

    @Override
    public List<ProductCategoryVO> getProductCategories(Map<String, String> params) {
        return productCategoryMapper.getProductCategories(params);
    }

}
