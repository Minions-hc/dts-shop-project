package com.qiguliuxing.dts.db.service;

import com.qiguliuxing.dts.db.dao.BoxProductRelationMapper;
import com.qiguliuxing.dts.vo.BoxProductRelationVO;
import com.qiguliuxing.dts.vo.ProductVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class BoxProductRelationService {



    @Resource
    private BoxProductRelationMapper boxProductRelationMapper;

    private static final Integer BASE_BOX_ID = 1000;


    public void batchInsertBoxProductRelations(List<BoxProductRelationVO> relations) {
        boxProductRelationMapper.batchInsertBoxProductRelations(relations);
    }

    public List<ProductVO> queryProductsInBox(Map<String, Object> params) {
        return boxProductRelationMapper.queryProductsInBox(params);
    }

    public void deleteProductBoxRelation(Map<String, Object> params) {
        boxProductRelationMapper.deleteProductBoxRelation(params);
    }

    public void batchUpdateProductBoxRelation(List<BoxProductRelationVO> relations) {
        boxProductRelationMapper.batchUpdateProductBoxRelation(relations);
    }
}
