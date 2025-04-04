package com.qiguliuxing.dts.db.service;
import com.qiguliuxing.dts.vo.BoxProductRelationVO;
import com.qiguliuxing.dts.vo.ProductBoxVO;
import com.qiguliuxing.dts.vo.ProductVO;

import java.util.List;
import java.util.Map;

public interface IBoxProductRelationService {

    void batchInsertBoxProductRelations(List<BoxProductRelationVO> relations);

    List<ProductVO> queryProductsInBox(Map<String, Object> params);
    
}
