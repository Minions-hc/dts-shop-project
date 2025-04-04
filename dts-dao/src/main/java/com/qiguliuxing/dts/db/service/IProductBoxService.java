package com.qiguliuxing.dts.db.service;
import com.qiguliuxing.dts.vo.ProductBoxVO;
import com.qiguliuxing.dts.vo.ProductVO;

import java.util.List;
import java.util.Map;

public interface IProductBoxService {

    void addProductBox(ProductBoxVO productBox);
    void deleteProductBox(Integer seriesId, String boxNumber, Integer productId);
    void updateProductBox(ProductBoxVO productBox);
    ProductBoxVO getProductBoxById(Integer seriesId, String boxNumber, Integer productId);
    List<ProductBoxVO> getProductBoxesBySeriesId(Integer seriesId);

    List<ProductBoxVO> getProductBoxByCondition(Map<String, Object> params);

}
