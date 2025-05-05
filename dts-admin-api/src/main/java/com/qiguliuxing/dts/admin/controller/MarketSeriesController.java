package com.qiguliuxing.dts.admin.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.qiguliuxing.dts.core.util.ResponseUtil;
import com.qiguliuxing.dts.db.domain.DtsAdmin;
import com.qiguliuxing.dts.db.service.IMarketSeriesService;
import com.qiguliuxing.dts.vo.MarketProductSeriesVO;
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
 * 集市换娃
 */
@RestController
@RequestMapping("/admin/marketSeries")
public class MarketSeriesController {

    private static final Logger logger = LoggerFactory.getLogger(MarketSeriesController.class);

    @Autowired
    private IMarketSeriesService marketSeriesService;

    @PostMapping("/addMarketSeries")
    public Object addProductSeries(@RequestBody MarketProductSeriesVO marketProductSeries) {
        Subject currentUser = SecurityUtils.getSubject();
        DtsAdmin admin = (DtsAdmin) currentUser.getPrincipal();
        String userName = admin.getUsername();
        marketProductSeries.setCreatedBy(userName);
        marketProductSeries.setUpdatedBy(userName);
        marketSeriesService.addMarketSeries(marketProductSeries);
        return ResponseUtil.ok(marketProductSeries);

    }

    @PostMapping("deleteMarketSeries")
    public Object deleteProductSeries(@RequestBody MarketProductSeriesVO marketProductSeries) {
        marketSeriesService.deleteMarketSeries(marketProductSeries.getSeriesId());
        return ResponseUtil.ok();
    }

    @PostMapping("/updateMarketSeries")
    public Object updateProductSeries(@RequestBody MarketProductSeriesVO marketProductSeries) {
        marketSeriesService.updateMarketSeries(marketProductSeries);
        return ResponseUtil.ok(marketProductSeries);
    }

    @PostMapping("/getMarketSeriesById/{seriesId}")
    public MarketProductSeriesVO getMarketeriesById(@PathVariable Integer seriesId) {
        return marketSeriesService.getMarketSeriesById(seriesId);
    }

    @GetMapping("/getMarketSeries")
    public Object getMarketSeries(String seriesId, String seriesName) {

        Map<String, Object> params = new HashMap<>();
        params.put("seriesId", seriesId);
        params.put("seriesName", seriesName);

        List<MarketProductSeriesVO> productSeries = marketSeriesService.getMarketSeries(params);
        long total = PageInfo.of(productSeries).getTotal();
        Map<String, Object> data = new HashMap<>();
        data.put("total", total);
        data.put("items", productSeries);

        logger.info("【请求结束】商场管理->集市换娃系列管理->查询:total:{}", JSONObject.toJSONString(data));
        return ResponseUtil.ok(data);
    }
}