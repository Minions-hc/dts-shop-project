package com.qiguliuxing.dts.db.service.impl;

import com.qiguliuxing.dts.db.dao.ProductLevelMapper;
import com.qiguliuxing.dts.db.service.IProductLevelService;
import com.qiguliuxing.dts.vo.ProductLevelVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ProductLevelServiceImpl implements IProductLevelService {

    @Resource
    private ProductLevelMapper productLevelMapper;

    @Override
    public void insertProductLevel(ProductLevelVO productLevelVO) {
        productLevelMapper.insertProductLevel(productLevelVO);
    }

    @Override
    public void deleteProductLevel(Integer levelId) {
        productLevelMapper.deleteProductLevel(levelId);
    }

    @Override
    public void updateProductLevel(ProductLevelVO productLevelVO) {
        productLevelMapper.updateProductLevel(productLevelVO);
    }

    @Override
    public List<ProductLevelVO> selectAllProductLevels() {
        return productLevelMapper.selectAllProductLevels();
    }
}
