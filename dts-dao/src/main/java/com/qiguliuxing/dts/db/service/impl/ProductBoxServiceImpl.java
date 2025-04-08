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
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductBoxServiceImpl implements IProductBoxService {

    @Resource
    private ProductBoxMapper productBoxMapper;

    @Resource
    private IBoxProductRelationService boxProductRelationService;

    private static final Integer BASE_BOX_ID = 1000;

    @Override
    @Transactional
    public void addProductBox(ProductBoxVO productBox) {
        Integer maxBoxId = productBoxMapper.findMaxBoxId();
        if(maxBoxId > 0) {
            productBox.setBoxId(maxBoxId + 1);
        } else {
            productBox.setBoxId(BASE_BOX_ID);
        }
        productBoxMapper.insertProductBox(productBox);
        List<BoxProductRelationVO> relations = new ArrayList<>();
        BoxProductRelationVO relationVO = null;
        for (ProductVO product : productBox.getProducts()) {
            relationVO = new BoxProductRelationVO();
            relationVO.setBoxNumber(productBox.getBoxNumber());
            relationVO.setBoxId(productBox.getBoxId());
            relationVO.setQuantity(product.getQuantity());
            relationVO.setProductId(product.getProductId());
            relationVO.setSoldQuantity(0);
            relationVO.setCreatedBy(productBox.getCreatedBy());
            relationVO.setUpdatedBy(productBox.getUpdatedBy());
            relations.add(relationVO);
        }
        boxProductRelationService.batchInsertBoxProductRelations(relations);
    }

    @Override
    @Transactional
    public void deleteProductBox(Map<String, Object> params) {
        productBoxMapper.deleteProductBox(params);
        boxProductRelationService.deleteProductBoxRelation(params);
    }

    @Override
    @Transactional
    public void updateProductBox(ProductBoxVO productBox) {

        Map<String, Object> params = new HashMap<>();
        params.put("boxId", productBox.getBoxId());
        params.put("boxNumber", productBox.getBoxNumber());
        // 查询已有数据
        List<ProductVO> existingProducts = boxProductRelationService.queryProductsInBox(params);
        List<Integer> existingProductIds = existingProducts.stream().map(ProductVO::getProductId).collect(Collectors.toList());
        List<BoxProductRelationVO> needUpdateRelations = new ArrayList<>();
        List<ProductVO> needUpdateProducts = productBox.getProducts().stream().filter(productVO -> existingProductIds.contains(productVO.getProductId())).collect(Collectors.toList());
        setProductBoxRelations(productBox, needUpdateProducts, needUpdateRelations);
        if(!needUpdateRelations.isEmpty()){
            boxProductRelationService.batchUpdateProductBoxRelation(needUpdateRelations);
        }
        List<ProductVO> needInsertProducts = productBox.getProducts().stream()
                .filter(productVO -> (productVO.getQuantity() != null && productVO.getQuantity() > 0) && !existingProductIds.contains(productVO.getProductId()))
                .collect(Collectors.toList());
        List<BoxProductRelationVO> needInsertRelations = new ArrayList<>();
        setProductBoxRelations(productBox, needInsertProducts, needInsertRelations);
        if(!needInsertRelations.isEmpty()){
            boxProductRelationService.batchInsertBoxProductRelations(needInsertRelations);
        }
    }

    private void setProductBoxRelations(ProductBoxVO productBox, List<ProductVO> products, List<BoxProductRelationVO> relations) {
        BoxProductRelationVO relationVO;
        for (ProductVO needInsertProduct : products) {
            relationVO = new BoxProductRelationVO();
            relationVO.setBoxNumber(productBox.getBoxNumber());
            relationVO.setBoxId(productBox.getBoxId());
            relationVO.setQuantity(needInsertProduct.getQuantity());
            relationVO.setProductId(needInsertProduct.getProductId());
            relationVO.setSoldQuantity(0);
            relationVO.setUpdatedBy(productBox.getUpdatedBy());
            relationVO.setCreatedBy(productBox.getCreatedBy());
            relations.add(relationVO);
        }
    }

    @Override
    public ProductBoxVO getProductBoxById(Integer seriesId, String boxNumber, Integer productId) {
        return productBoxMapper.getProductBoxById(seriesId, boxNumber, productId);
    }

    @Override
    public List<ProductBoxVO> getProductBoxesBySeriesId(Integer seriesId) {
        return productBoxMapper.getProductBoxesBySeriesId(seriesId);
    }

    @Override
    public List<ProductBoxVO> getProductBoxByCondition(Map<String, Object> params) {
        return productBoxMapper.getProductBoxByCondition(params);
    }
}
