package com.qiguliuxing.dts.wx.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import com.qiguliuxing.dts.db.service.*;
import com.qiguliuxing.dts.vo.ProductCategoryVO;
import com.qiguliuxing.dts.vo.ProductSeriesVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qiguliuxing.dts.core.util.ResponseUtil;

/**
 * 首页服务
 */
@RestController
@RequestMapping("/wx/home")
@Validated
public class WxHomeController {
	private static final Logger logger = LoggerFactory.getLogger(WxHomeController.class);


	@Autowired
	private ProductSeriesService productSeriesService;

	@Autowired
	private IProductCategoryService productCategoryService;

	private final static ArrayBlockingQueue<Runnable> WORK_QUEUE = new ArrayBlockingQueue<>(9);

	private final static RejectedExecutionHandler HANDLER = new ThreadPoolExecutor.CallerRunsPolicy();

	@SuppressWarnings("unused")
	private static ThreadPoolExecutor executorService = new ThreadPoolExecutor(9, 9, 1000, TimeUnit.MILLISECONDS,
			WORK_QUEUE, HANDLER);


	@GetMapping("/getWxCategoryNames")
	public Object getWxCategoryNames() {
		List<ProductCategoryVO> productCategories = productCategoryService.getProductCategories(new HashMap<>());
		Map<String, Object> data = new HashMap<>();
		data.put("items", productCategories);
		return ResponseUtil.ok(data);
	}


	@GetMapping("/getWxProductSeries")
	public Object getProductSeries(String seriesId, Integer isHot, Integer isAvoid, Integer isPopularNew, Integer isHotRecommend) {
		Map<String, Object> params = new HashMap<>();
		params.put("seriesId", seriesId);
		params.put("isHot", isHot);
		params.put("isAvoid", isAvoid);
		params.put("isPopularNew", isPopularNew);
		params.put("isHotRecommend", isHotRecommend);
		params.put("isSpiritPower", 0);
		List<ProductSeriesVO> productSeries = productSeriesService.getWxProductSeries(params);
		Map<String, Object> data = new HashMap<>();
		data.put("items", productSeries);
		Map<String, List<ProductSeriesVO>> productSeriesGroup = productSeries.stream().collect(Collectors.groupingBy(ProductSeriesVO::getCategoryName));
		data.put("productSeriesGroup", productSeriesGroup);
		return ResponseUtil.ok(data);
	}

	@GetMapping("/getWxSeriesByCategoryId")
	public Object getWxSeriesByCategoryId(Integer categoryId) {
		List<ProductSeriesVO> productSeries = productSeriesService.getProductSeriesByCategoryId(categoryId);
		Map<String, Object> data = new HashMap<>();
		data.put("items", productSeries);
		return ResponseUtil.ok(data);
	}

}
