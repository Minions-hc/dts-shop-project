package com.qiguliuxing.dts.wx.web;

import com.qiguliuxing.dts.core.util.ResponseUtil;
import com.qiguliuxing.dts.db.service.IMarketSeriesService;
import com.qiguliuxing.dts.db.service.MarketProductService;
import com.qiguliuxing.dts.vo.BoxProductRequestVO;
import com.qiguliuxing.dts.vo.BoxProductVO;
import com.qiguliuxing.dts.vo.MarketProductVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 集市换娃服务
 */
@RestController
@RequestMapping("/wx/market")
@Validated
public class WxMarketController {
	private static final Logger logger = LoggerFactory.getLogger(WxMarketController.class);

	@Autowired
	private IMarketSeriesService marketSeriesService;

	@Autowired
	private MarketProductService marketProductService;

	@GetMapping("/getWxMarketSeries")
	public Object getWxMarketSeries() {
		Map<String, Object> params = new HashMap<>();
		List<MarketProductVO> marketSeries = marketProductService.getMarketProduct(params);
		Map<String, Object> data = new HashMap<>();
		data.put("items", marketSeries);
		Map<String, List<MarketProductVO>> marketProductGroup = marketSeries.stream().collect(Collectors.groupingBy(MarketProductVO::getProductSeriesName));
		data.put("marketProductGroup", marketProductGroup);
		return ResponseUtil.ok(data);
	}

	@GetMapping("/getMarketProductByProductId")
	public Object getMarketProductByProductId(Integer productId) {
		MarketProductVO mrketProductVO = marketProductService.getMarketProductById(productId);
		Map<String, Object> data = new HashMap<>();
		data.put("item", mrketProductVO);
		return ResponseUtil.ok(data);
	}

	/**
	 * 用户当前勋章
	 * @param userId
	 * @return
	 */
	@GetMapping("/getCurrentBadge")
	public Object getCurrentBadge(String userId) {
		Integer productBadge = marketProductService.getCurrentBadge(userId);
		return ResponseUtil.ok(productBadge);
	}

	/**
	 * 集市换娃*可兑换的列表
	 * @param userId
	 * @return
	 */
	@GetMapping("/getBoxProductList")
	public Object getBoxProductList(String userId) {
		List<BoxProductVO> boxProductList = marketProductService.getBoxProductList(userId);
		return ResponseUtil.ok(boxProductList);
	}


	/**
	 * 集市换娃
	 * @param boxProductRequestVO
	 * @return
	 */
	@PostMapping("/redeemProduct")
	public Object redeemProduct(@RequestBody BoxProductRequestVO boxProductRequestVO) {
		marketProductService.redeemProduct(boxProductRequestVO);
		return ResponseUtil.ok();
	}
}
