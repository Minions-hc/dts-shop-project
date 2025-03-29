package com.qiguliuxing.dts.admin.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.qiguliuxing.dts.core.util.ResponseUtil;
import com.qiguliuxing.dts.db.domain.DtsAdmin;
import com.qiguliuxing.dts.db.service.IProductService;
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

@RestController
@RequestMapping("/admin/product")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private IProductService productService;

    @PostMapping("/addProduct")
    public Object addProduct(@RequestBody ProductVO product) {
        Subject currentUser = SecurityUtils.getSubject();
        DtsAdmin admin = (DtsAdmin) currentUser.getPrincipal();
        String userName = admin.getUsername();
        product.setCreatedBy(userName);
        product.setUpdatedBy(userName);
        productService.addProduct(product);
        return ResponseUtil.ok(product);
    }

    @PostMapping("/deleteProduct")
    public Object deleteProduct(@RequestBody ProductVO product) {
        productService.deleteProduct(product.getProductId());
        return ResponseUtil.ok();
    }

    @PostMapping("/updateProduct")
    public Object updateProduct(@RequestBody ProductVO product) {
        productService.updateProduct(product);
        return ResponseUtil.ok(product);
    }


    @GetMapping("/getProductsByCondition")
    public Object getProductsByCondition(Integer productId, String productName, Integer productSeriesId, Integer productLevelId) {
        Map<String, Object> params = new HashMap<>();
        params.put("productId", productId);
        params.put("productName", productName);
        params.put("productSeriesId", productSeriesId);
        params.put("productLevelId", productLevelId);

        List<ProductVO> productList = productService.getProductsByCondition(params);
        long total = PageInfo.of(productList).getTotal();
        Map<String, Object> data = new HashMap<>();
        data.put("total", total);
        data.put("items", productList);
        logger.info("【请求结束】商场管理->类目管理->查询:total:{}", JSONObject.toJSONString(data));
        return ResponseUtil.ok(data);

    }

    @GetMapping("/getProductById")
    public Object getProductById(Integer productId) {
        ProductVO product = productService.getProductById(productId);
        return ResponseUtil.ok(product);
    }

    @GetMapping("/getAllProducts")
    public List<ProductVO> getAllProducts() {
        return productService.getAllProducts();
    }
}