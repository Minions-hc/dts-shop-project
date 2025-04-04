package com.qiguliuxing.dts.db.dao;

import com.qiguliuxing.dts.vo.BoxProductRelationVO;
import com.qiguliuxing.dts.vo.ProductVO;

import java.util.List;
import java.util.Map;

public interface BoxProductRelationMapper {

    void batchInsertBoxProductRelations(List<BoxProductRelationVO> relations);


    List<ProductVO> queryProductsInBox (Map<String, Object> params);

}