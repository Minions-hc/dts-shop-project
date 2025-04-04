package com.qiguliuxing.dts.db.service.impl;

import com.qiguliuxing.dts.db.dao.BoxProductRelationMapper;
import com.qiguliuxing.dts.db.dao.ProductBoxMapper;
import com.qiguliuxing.dts.db.service.IBoxProductRelationService;
import com.qiguliuxing.dts.db.service.IProductBoxService;
import com.qiguliuxing.dts.vo.BoxProductRelationVO;
import com.qiguliuxing.dts.vo.ProductBoxVO;
import com.qiguliuxing.dts.vo.ProductVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class BoxProductRelationServiceImpl implements IBoxProductRelationService {



    @Resource
    private BoxProductRelationMapper boxProductRelationMapper;

    private static final Integer BASE_BOX_ID = 1000;


    @Override
    public void batchInsertBoxProductRelations(List<BoxProductRelationVO> relations) {
        boxProductRelationMapper.batchInsertBoxProductRelations(relations);
    }

    @Override
    public List<ProductVO> queryProductsInBox(Map<String, Object> params) {
        return boxProductRelationMapper.queryProductsInBox(params);
    }
}
