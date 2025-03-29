package com.qiguliuxing.dts.admin.controller;

import com.alibaba.fastjson.JSONObject;
import com.qiguliuxing.dts.core.util.ResponseUtil;
import com.qiguliuxing.dts.db.service.IProductLevelService;
import com.qiguliuxing.dts.db.service.IProductService;
import com.qiguliuxing.dts.vo.ProductLevelVO;
import com.qiguliuxing.dts.vo.ProductVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/productLevel")
public class ProductLevelController {

    private static final Logger logger = LoggerFactory.getLogger(ProductLevelController.class);

    @Autowired
    private IProductLevelService productLevelService;

    @PostMapping("/insertProductLevel")
    public void insertProductLevel(@RequestBody ProductLevelVO productLevelVO) {
        productLevelService.insertProductLevel(productLevelVO);
    }

    @PostMapping("/deleteProductLevel")
    public void deleteProductLevel(@RequestBody ProductLevelVO productLevelVO) {
        productLevelService.deleteProductLevel(productLevelVO.getLevelId());
    }

    @PostMapping("/updateProductLevel")
    public void updateProductLevel(@RequestBody ProductLevelVO productLevelVO) {
        productLevelService.updateProductLevel(productLevelVO);
    }

    @GetMapping("/getAllProductLevels")
    public Object getAllProductLevels() {
        List<ProductLevelVO> productLevelVOS = productLevelService.selectAllProductLevels();
        Map<String, Object> data = new HashMap<>();
        data.put("items", productLevelVOS);
        logger.info("【请求结束】商品管理->商品等级管理->查询:total:{}", JSONObject.toJSONString(data));
        return ResponseUtil.ok(data);
    }
}