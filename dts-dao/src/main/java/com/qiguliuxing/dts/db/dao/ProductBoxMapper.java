package com.qiguliuxing.dts.db.dao;

import com.qiguliuxing.dts.vo.ProductBoxVO;

import java.util.List;

public interface ProductBoxMapper {

    void insertProductBox(ProductBoxVO productBox);

    void deleteProductBox( Integer seriesId, String boxNumber,  Integer productId);

    void updateProductBox(ProductBoxVO productBox);

    ProductBoxVO getProductBoxById( Integer seriesId,  String boxNumber,  Integer productId);

    List<ProductBoxVO> getProductBoxesBySeriesId(Integer seriesId);
}