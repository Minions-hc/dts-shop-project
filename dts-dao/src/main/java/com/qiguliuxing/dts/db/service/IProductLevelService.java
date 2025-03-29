package com.qiguliuxing.dts.db.service;

import com.qiguliuxing.dts.vo.ProductLevelVO;
import com.qiguliuxing.dts.vo.ProductVO;

import java.util.List;

public interface IProductLevelService {

    void insertProductLevel(ProductLevelVO productLevelVO);

    void deleteProductLevel(Integer levelId);

    void updateProductLevel(ProductLevelVO productLevelVO);

    List<ProductLevelVO> selectAllProductLevels();
}
