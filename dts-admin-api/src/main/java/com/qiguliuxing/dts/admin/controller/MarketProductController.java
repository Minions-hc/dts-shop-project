package com.qiguliuxing.dts.admin.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.qiguliuxing.dts.core.util.ResponseUtil;
import com.qiguliuxing.dts.db.domain.DtsAdmin;
import com.qiguliuxing.dts.db.service.MarketProductService;
import com.qiguliuxing.dts.vo.MarketProductVO;
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
@RequestMapping("/admin/marketProduct")
public class MarketProductController {

    private static final Logger logger = LoggerFactory.getLogger(MarketProductController.class);

    @Autowired
    private MarketProductService marketProductService;

    @PostMapping("/addMarketProduct")
    public Object addMarketProduct(@RequestBody MarketProductVO marketProduct) {
        Subject currentUser = SecurityUtils.getSubject();
        DtsAdmin admin = (DtsAdmin) currentUser.getPrincipal();
        String userName = admin.getUsername();
        marketProduct.setCreatedBy(userName);
        marketProduct.setUpdatedBy(userName);
        marketProductService.addMarketProduct(marketProduct);
        return ResponseUtil.ok(marketProduct);
    }

    @PostMapping("/deleteMarketProduct")
    public Object deleteProduct(@RequestBody MarketProductVO marketProduct) {
        marketProductService.deleteMarketProduct(marketProduct.getProductId());
        return ResponseUtil.ok();
    }

    @PostMapping("/updateMarketProduct")
    public Object updateMarketProduct(@RequestBody MarketProductVO marketProduct) {
        marketProductService.updateMarketProduct(marketProduct);
        return ResponseUtil.ok(marketProduct);
    }


    @GetMapping("/getMarketProductsByCondition")
    public Object getMarketProductsByCondition(Integer productId, String productName, Integer productSeriesId, Integer productLevelId) {
        Map<String, Object> params = new HashMap<>();
        params.put("productId", productId);
        params.put("productName", productName);
        params.put("productSeriesId", productSeriesId);
        params.put("productLevelId", productLevelId);

        List<MarketProductVO> productList = marketProductService.getMarketProduct(params);
        long total = PageInfo.of(productList).getTotal();
        Map<String, Object> data = new HashMap<>();
        data.put("total", total);
        data.put("items", productList);
        logger.info("【请求结束】商场管理->类目管理->查询:total:{}", JSONObject.toJSONString(data));
        return ResponseUtil.ok(data);

    }

    @GetMapping("/getMarketProductById")
    public Object getMarketProductById(Integer productId) {
        MarketProductVO product = marketProductService.getMarketProductById(productId);
        return ResponseUtil.ok(product);
    }

    @GetMapping("/getAllMarketProducts")
    public List<MarketProductVO> getAllProducts() {
        return marketProductService.getAllMarketProducts();
    }
}