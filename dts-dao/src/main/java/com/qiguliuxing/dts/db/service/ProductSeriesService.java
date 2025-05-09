package com.qiguliuxing.dts.db.service;

import com.qiguliuxing.dts.db.dao.ProductMapper;
import com.qiguliuxing.dts.db.dao.ProductSeriesMapper;
import com.qiguliuxing.dts.vo.ProductSeriesVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductSeriesService {

    @Resource
    private ProductSeriesMapper productSeriesMapper;

    @Resource
    private ProductMapper productMapper;

    /**
     * 新增系列（后台管理系统）
     * @param productSeries 产品系列
     */
    @Transactional
    public void addProductSeries(ProductSeriesVO productSeries) {
        productSeriesMapper.insertProductSeries(productSeries);
    }

    /**
     * 删除系列（后台管理系统）
     * @param seriesId 系列ID
     */
    @Transactional
    public void deleteProductSeries(Integer seriesId) {
        productSeriesMapper.deleteProductSeries(seriesId);
    }

    /**
     * 更新系列（后台管理系统）
     *
     * @param productSeries 产品系列
     */
    @Transactional
    public void updateProductSeries(ProductSeriesVO productSeries) {
        productSeriesMapper.updateProductSeries(productSeries);
    }

    /**
     * 根据系列ID查询系列（后台管理系统）
     *
     * @param seriesId 系列ID
     * @return 系列信息
     */
    public ProductSeriesVO getProductSeriesById(Integer seriesId) {
        return productSeriesMapper.getProductSeriesById(seriesId);
    }

    /**
     * 动态查询系列（后台管理系统）
     *
     * @param params 动态参数
     * @return 系列信息
     */
    public List<ProductSeriesVO> getProductSeries(Map<String, Object> params) {
        return productSeriesMapper.getProductSeries(params);
    }


    /**
     * 动态查询系列（微信客户端）
     *
     * @param params params 动态参数
     * @return 系列信息
     */
    public List<ProductSeriesVO> getWxProductSeries(Map<String, Object> params) {
        // 根据条件查询对应的系列
        List<ProductSeriesVO> productSeriesVOS = productSeriesMapper.getProductSeries(params);
        // 汇总系列的购买次数
        List<ProductSeriesVO> sumSeriesQuantityList = productSeriesMapper.sumSeriesQuantity();
        Map<Integer, Integer> seriesIdToQuantityMap = sumSeriesQuantityList.stream().collect(Collectors.toMap(ProductSeriesVO ::getSeriesId, ProductSeriesVO::getPurchaseCount));
        Integer purchaseCount;
        for (ProductSeriesVO productSeriesVO : productSeriesVOS) {
            purchaseCount = seriesIdToQuantityMap.get(productSeriesVO.getSeriesId()) == null ? 0 : seriesIdToQuantityMap.get(productSeriesVO.getSeriesId());
            productSeriesVO.setPurchaseCount(purchaseCount);
        }
        return productSeriesVOS;
    }

    public List<ProductSeriesVO> getProductSeriesByCategoryId(Integer categoryId) {
        return productSeriesMapper.getProductSeriesByCategoryId(categoryId);
    }
}
