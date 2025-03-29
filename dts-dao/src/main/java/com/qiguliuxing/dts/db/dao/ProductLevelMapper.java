package com.qiguliuxing.dts.db.dao;

import com.qiguliuxing.dts.vo.ProductLevelVO;

import java.util.List;

public interface ProductLevelMapper {
    void insertProductLevel(ProductLevelVO productLevelVO);

    void deleteProductLevel(Integer levelId);

    void updateProductLevel(ProductLevelVO productLevelVO);

    List<ProductLevelVO> selectAllProductLevels();
}