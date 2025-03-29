package com.qiguliuxing.dts.db.service;
import com.qiguliuxing.dts.vo.ProductBoxVO;

import java.util.List;

public interface IProductBoxService {

    void addProductBox(ProductBoxVO productBox);
    void deleteProductBox(Integer seriesId, String boxNumber, Integer productId);
    void updateProductBox(ProductBoxVO productBox);
    ProductBoxVO getProductBoxById(Integer seriesId, String boxNumber, Integer productId);
    List<ProductBoxVO> getProductBoxesBySeriesId(Integer seriesId);

}
