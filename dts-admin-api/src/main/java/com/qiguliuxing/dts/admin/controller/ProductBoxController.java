package com.qiguliuxing.dts.admin.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.qiguliuxing.dts.core.util.ResponseUtil;
import com.qiguliuxing.dts.db.domain.DtsAdmin;
import com.qiguliuxing.dts.db.service.IBoxProductRelationService;
import com.qiguliuxing.dts.db.service.IProductBoxService;
import com.qiguliuxing.dts.db.service.IProductService;
import com.qiguliuxing.dts.vo.ProductBoxVO;
import com.qiguliuxing.dts.vo.ProductCategoryVO;
import com.qiguliuxing.dts.vo.ProductVO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/productBox")
public class ProductBoxController {


    private static final Logger logger = LoggerFactory.getLogger(ProductBoxController.class);


    @Autowired
    private IProductBoxService productBoxService;

    @Autowired
    private IProductService productService;

    @Autowired
    private IBoxProductRelationService boxProductRelationService;



    @PostMapping("/addProductBox")
    public Object addProductBox(@RequestBody ProductBoxVO productBox) {
        Subject currentUser = SecurityUtils.getSubject();
        DtsAdmin admin = (DtsAdmin) currentUser.getPrincipal();
        String userName = admin.getUsername();
        productBox.setCreatedBy(userName);
        productBox.setUpdatedBy(userName);
        productBoxService.addProductBox(productBox);
        return ResponseUtil.ok(productBox);
    }


    @PostMapping("/deleteProductBox")
    public Object deleteProductBox(@RequestBody ProductBoxVO productBox) {
        Map<String, Object> params = new HashMap<>();
        params.put("boxId", productBox.getBoxId());
        params.put("boxNumber", productBox.getBoxNumber());
        productBoxService.deleteProductBox(params);
        return ResponseUtil.ok();
    }

    @PostMapping("/updateProductBox")
    public Object updateProductBox(@RequestBody ProductBoxVO productBox) {
        Subject currentUser = SecurityUtils.getSubject();
        DtsAdmin admin = (DtsAdmin) currentUser.getPrincipal();
        String userName = admin.getUsername();
        productBox.setCreatedBy(userName);
        productBox.setUpdatedBy(userName);
        productBoxService.updateProductBox(productBox);
        return ResponseUtil.ok();
    }

    @GetMapping("/getProductBoxByCondition")
    public Object getProductBoxByCondition(Integer boxId, String productSeriesName, Integer productSeriesId) {

        Map<String, Object> params = new HashMap<>();
        params.put("boxId", boxId);
        params.put("productSeriesName", productSeriesName);
        params.put("productSeriesId", productSeriesId);

        List<ProductBoxVO> productBoxs = productBoxService.getProductBoxByCondition(params);
        long total = PageInfo.of(productBoxs).getTotal();
        Map<String, Object> data = new HashMap<>();
        data.put("total", total);
        data.put("items", productBoxs);
        logger.info("【请求结束】商场管理->类目管理->查询:total:{}", JSONObject.toJSONString(data));
        return ResponseUtil.ok(data);
    }

    /**
     * 查询箱子底下的产品
     * @param boxId 箱子ID
     * @param boxNumber 箱子编号
     * @return 查询箱子底下的产品列表
     */
    @GetMapping("/queryProductsInBox")
    public Object queryProductsInBox(Integer boxId, Integer boxNumber, Integer seriesId) {
        Map<String, Object> params = new HashMap<>();
        params.put("boxId", boxId);
        params.put("boxNumber", boxNumber);
        List<ProductVO> products = boxProductRelationService.queryProductsInBox(params);
        List<Integer> productIds = products.stream().map(ProductVO::getProductId).collect(Collectors.toList());
        params.put("productSeriesId", seriesId);
        List<ProductVO> baseProducts = productService.getProductsByCondition(params);
        products.addAll(baseProducts.stream().filter(productVO -> !productIds.contains(productVO.getProductId())).collect(Collectors.toList()));
        Map<String, Object> data = new HashMap<>();
        data.put("items", products);
        logger.info("【请求结束】商场管理->类目管理->查询:total:{}", JSONObject.toJSONString(data));
        return ResponseUtil.ok(data);
    }



    @GetMapping("/{seriesId}/{boxNumber}/{productId}")
    public ProductBoxVO getProductBoxById(@PathVariable Integer seriesId, @PathVariable String boxNumber, @PathVariable Integer productId) {
        return productBoxService.getProductBoxById(seriesId, boxNumber, productId);
    }

    @GetMapping("/{seriesId}")
    public List<ProductBoxVO> getProductBoxesBySeriesId(@PathVariable Integer seriesId) {
        return productBoxService.getProductBoxesBySeriesId(seriesId);
    }
}