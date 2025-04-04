package com.qiguliuxing.dts.db.dao;

import com.qiguliuxing.dts.vo.ProductBoxVO;
import com.qiguliuxing.dts.vo.ProductVO;

import java.util.List;
import java.util.Map;

public interface ProductBoxMapper {

    Integer findMaxBoxId();

    void insertProductBox(ProductBoxVO productBox);

    void deleteProductBox( Integer seriesId, String boxNumber,  Integer productId);

    void updateProductBox(ProductBoxVO productBox);

    ProductBoxVO getProductBoxById( Integer seriesId,  String boxNumber,  Integer productId);

    List<ProductBoxVO> getProductBoxesBySeriesId(Integer seriesId);

    List<ProductBoxVO> getProductBoxByCondition(Map<String, Object> params);
}