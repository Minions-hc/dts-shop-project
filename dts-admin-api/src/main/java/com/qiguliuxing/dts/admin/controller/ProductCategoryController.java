package com.qiguliuxing.dts.admin.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.qiguliuxing.dts.core.util.ResponseUtil;
import com.qiguliuxing.dts.db.domain.DtsAdmin;
import com.qiguliuxing.dts.db.service.IProductCategoryService;
import com.qiguliuxing.dts.vo.ProductCategoryVO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品类目
 */
@RestController
@RequestMapping("/admin/productCategory")
public class ProductCategoryController {

    private static final Logger logger = LoggerFactory.getLogger(ProductCategoryController.class);

    @Autowired
    private IProductCategoryService productCategoryService;

    @PostMapping("/addProductCategory")
    public Object addProductCategory(@RequestBody ProductCategoryVO productCategory) {
        Subject currentUser = SecurityUtils.getSubject();
        DtsAdmin admin = (DtsAdmin) currentUser.getPrincipal();
        String userName = admin.getUsername();
        productCategory.setCreatedBy(userName);
        productCategory.setUpdatedBy(userName);
        productCategoryService.addProductCategory(productCategory);
        return ResponseUtil.ok(productCategory);
    }

    @PostMapping("/deleteProductCategory")
    public Object deleteProductCategory(@RequestBody ProductCategoryVO productCategory) {
        productCategoryService.deleteProductCategory(productCategory.getCategoryId());
        return ResponseUtil.ok();
    }

    @PostMapping("/updateProductCategory")
    public Object updateProductCategory(@RequestBody ProductCategoryVO productCategory) {
        productCategoryService.updateProductCategory(productCategory);
        return ResponseUtil.ok(productCategory);
    }

    @GetMapping("/{categoryId}")
    public ProductCategoryVO getProductCategoryById(@PathVariable Integer categoryId) {
        return productCategoryService.getProductCategoryById(categoryId);
    }

    @GetMapping("/getProductCategories")
    public Object getProductCategories(String categoryId, String categoryName) {

        Map<String, String> params = new HashMap<>();
        params.put("categoryId", categoryId);
        params.put("categoryName", categoryName);

        List<ProductCategoryVO> productCategoryVOS = productCategoryService.getProductCategories(params);
        long total = PageInfo.of(productCategoryVOS).getTotal();
        Map<String, Object> data = new HashMap<>();
        data.put("total", total);
        data.put("items", productCategoryVOS);

        logger.info("【请求结束】商场管理->类目管理->查询:total:{}", JSONObject.toJSONString(data));
        return ResponseUtil.ok(data);
    }
}