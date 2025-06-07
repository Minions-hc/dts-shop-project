package com.qiguliuxing.dts.db.service;

import com.qiguliuxing.dts.db.dao.ProductBoxMapper;
import com.qiguliuxing.dts.vo.BoxProductRelationVO;
import com.qiguliuxing.dts.vo.ProductBoxResultVo;
import com.qiguliuxing.dts.vo.ProductBoxVO;
import com.qiguliuxing.dts.vo.ProductVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductBoxService {

    @Resource
    private ProductBoxMapper productBoxMapper;

    @Resource
    private BoxProductRelationService boxProductRelationService;

    private static final Integer BASE_BOX_ID = 1000;

    /**
     * 新增箱子（后台管理系统）
     *
     * @param productBox 箱子数据
     */
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
        List<ProductVO> filterProducts = productBox.getProducts().stream().filter(productVO -> productVO.getQuantity() != null).collect(Collectors.toList());
        for (ProductVO product : filterProducts) {
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
        if (!relations.isEmpty()) {
            boxProductRelationService.batchInsertBoxProductRelations(relations);
        }
    }

    /**
     * 删除箱子（后台管理系统）
     *
     * @param params 参数列表
     */
    @Transactional
    public void deleteProductBox(Map<String, Object> params) {
        productBoxMapper.deleteProductBox(params);
        boxProductRelationService.deleteProductBoxRelation(params);
    }

    /**
     * 更新箱子（后台管理系统）
     *
     * @param productBox 箱子数据
     */
    @Transactional
    public void updateProductBox(ProductBoxVO productBox) {

        Map<String, Object> params = new HashMap<>();
        params.put("boxId", productBox.getBoxId());
        params.put("boxNumber", productBox.getBoxNumber());
        // 查询已有数据
        List<ProductVO> existingProducts = boxProductRelationService.queryProductsInBox(params);
        Map<Integer, ProductVO> existingProductsMap = existingProducts.stream().collect(Collectors.toMap(ProductVO::getProductId, p -> p));
        List<Integer> existingProductIds = existingProducts.stream().map(ProductVO::getProductId).collect(Collectors.toList());
        List<BoxProductRelationVO> needUpdateRelations = new ArrayList<>();
        List<ProductVO> needUpdateProducts = productBox.getProducts().stream().filter(productVO -> existingProductIds.contains(productVO.getProductId())).collect(Collectors.toList());
        setProductBoxRelations(productBox, needUpdateProducts, needUpdateRelations, true, existingProductsMap);
        if(!needUpdateRelations.isEmpty()){
            boxProductRelationService.batchUpdateProductBoxRelation(needUpdateRelations);
        }
        List<ProductVO> needInsertProducts = productBox.getProducts().stream()
                .filter(productVO -> (productVO.getQuantity() != null && productVO.getQuantity() > 0) && !existingProductIds.contains(productVO.getProductId()))
                .collect(Collectors.toList());
        List<BoxProductRelationVO> needInsertRelations = new ArrayList<>();
        setProductBoxRelations(productBox, needInsertProducts, needInsertRelations, false, existingProductsMap);
        if(!needInsertRelations.isEmpty()){
            boxProductRelationService.batchInsertBoxProductRelations(needInsertRelations);
        }
    }

    /**
     *  设置箱子与产品关联关系
     * @param productBox 箱子数据
     * @param products 产品数据
     * @param relations 关系数据
     */
    private void setProductBoxRelations(ProductBoxVO productBox, List<ProductVO> products, List<BoxProductRelationVO> relations, boolean isUpdate,  Map<Integer, ProductVO> existingProductsMap) {
        BoxProductRelationVO relationVO;
        for (ProductVO productVO : products) {
            relationVO = new BoxProductRelationVO();
            relationVO.setBoxNumber(productBox.getBoxNumber());
            relationVO.setBoxId(productBox.getBoxId());
            relationVO.setQuantity(productVO.getQuantity());
            relationVO.setProductId(productVO.getProductId());
            if (isUpdate) {
                relationVO.setSoldQuantity(existingProductsMap.get(productVO.getProductId()).getSoldQuantity());
            } else {
                relationVO.setSoldQuantity(0);
            }
            relationVO.setUpdatedBy(productBox.getUpdatedBy());
            relationVO.setCreatedBy(productBox.getCreatedBy());
            relations.add(relationVO);
        }
    }

    /**
     * 查询箱子底下的产品（后台管理系统）
     *
     * @param seriesId 系列ID
     * @param boxNumber 箱子编号
     * @param productId 产品ID
     * @return
     */
    public ProductBoxVO getProductBoxById(Integer seriesId, String boxNumber, Integer productId) {
        return productBoxMapper.getProductBoxById(seriesId, boxNumber, productId);
    }

    /**
     * 根据系列ID查询箱子数据（微信客户端）
     *
     * @param seriesId 系列ID
     * @return
     */
    public List<ProductBoxResultVo> getProductBoxBySeriesId(Integer seriesId) {
        return productBoxMapper.getProductBoxBySeriesId(seriesId);
    }

    /**
     * 动态查询箱子数据（后台管理系统）
     *
     * @param params
     * @return
     */
    public List<ProductBoxVO> getProductBoxByCondition(Map<String, Object> params) {
        return productBoxMapper.getProductBoxByCondition(params);
    }

}
