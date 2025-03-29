package com.qiguliuxing.dts.admin.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.qiguliuxing.dts.core.util.ResponseUtil;
import com.qiguliuxing.dts.db.domain.DtsAdmin;
import com.qiguliuxing.dts.db.service.IProductSeriesService;
import com.qiguliuxing.dts.vo.ProductCategoryVO;
import com.qiguliuxing.dts.vo.ProductSeriesVO;
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
@RequestMapping("/admin/productSeries")
public class ProductSeriesController {

    private static final Logger logger = LoggerFactory.getLogger(ProductSeriesController.class);

    @Autowired
    private IProductSeriesService productSeriesService;

    @PostMapping("/addProductSeries")
    public Object addProductSeries(@RequestBody ProductSeriesVO productSeries) {
        Subject currentUser = SecurityUtils.getSubject();
        DtsAdmin admin = (DtsAdmin) currentUser.getPrincipal();
        String userName = admin.getUsername();
        productSeries.setCreatedBy(userName);
        productSeries.setUpdatedBy(userName);
        productSeriesService.addProductSeries(productSeries);
        return ResponseUtil.ok(productSeries);

    }

    @PostMapping("deleteProductSeries")
    public Object deleteProductSeries(@RequestBody ProductSeriesVO productSeriesVO) {
        productSeriesService.deleteProductSeries(productSeriesVO.getSeriesId());
        return ResponseUtil.ok();
    }

    @PostMapping("/updateProductSeries")
    public Object updateProductSeries(@RequestBody ProductSeriesVO productSeries) {
        productSeriesService.updateProductSeries(productSeries);
        return ResponseUtil.ok(productSeries);
    }

    @PostMapping("/getProductSeriesById/{seriesId}")
    public ProductSeriesVO getProductSeriesById(@PathVariable Integer seriesId) {
        return productSeriesService.getProductSeriesById(seriesId);
    }

    @GetMapping("/getProductSeries")
    public Object getProductSeries(String seriesId, String seriesName) {

        Map<String, String> params = new HashMap<>();
        params.put("seriesId", seriesId);
        params.put("seriesName", seriesName);

        List<ProductSeriesVO> productSeries = productSeriesService.getProductSeries(params);
        long total = PageInfo.of(productSeries).getTotal();
        Map<String, Object> data = new HashMap<>();
        data.put("total", total);
        data.put("items", productSeries);

        logger.info("【请求结束】商场管理->类目管理->查询:total:{}", JSONObject.toJSONString(data));
        return ResponseUtil.ok(data);
    }
}