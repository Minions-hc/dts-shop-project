package com.qiguliuxing.dts.wx.web;

import com.qiguliuxing.dts.core.util.ResponseUtil;
import com.qiguliuxing.dts.db.service.IProductCategoryService;
import com.qiguliuxing.dts.db.service.ProductBoxService;
import com.qiguliuxing.dts.db.service.ProductSeriesService;
import com.qiguliuxing.dts.vo.ProductBoxResultVo;
import com.qiguliuxing.dts.vo.ProductBoxVO;
import com.qiguliuxing.dts.vo.ProductSeriesVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/wx/series")
@Validated
public class WxProductSeriesController {

    @Autowired
    private ProductBoxService productBoxService;

    @Autowired
    private ProductSeriesService productSeriesService;

    @Autowired
    private IProductCategoryService productCategoryService;

    @GetMapping("/getProductBoxBySeriesId")
    public Object getProductBoxBySeriesId(Integer seriesId){
        List<ProductBoxResultVo> productBoxResultVos = productBoxService.getProductBoxBySeriesId(seriesId);

        ProductSeriesVO productSeries = productSeriesService.getProductSeriesById(seriesId);
        // 1. 按照boxNumber分组获取分组后的列表
        Map<String, List<ProductBoxResultVo>> groupedByBoxNumber = productBoxResultVos.stream()
                .collect(Collectors.groupingBy(ProductBoxResultVo::getBoxNumber));

        // 3. 获得以boxNumber为key，boxNumber底下的产品数量总数为value的map
        Map<String, Integer> productQuantityMap = productBoxResultVos.stream()
                .collect(Collectors.groupingBy(
                        ProductBoxResultVo::getBoxNumber,
                        Collectors.summingInt(ProductBoxResultVo::getQuantity)
                ));
        // 4. 获得以boxNumber为key，boxNumber底下的产品已售数量总数为value的map
        Map<String, Integer> soldQuantityMap = productBoxResultVos.stream()
                .collect(Collectors.groupingBy(
                        ProductBoxResultVo::getBoxNumber,
                        Collectors.summingInt(ProductBoxResultVo::getSoldQuantity)
                ));
        // 5. 获得以boxNumber为key，产品剩余数量总数（产品数量总数-已售数量总数）为value的map
        Map<String, Integer> remainingQuantityMap = productBoxResultVos.stream()
                .collect(Collectors.groupingBy(
                        ProductBoxResultVo::getBoxNumber,
                        Collectors.summingInt(vo -> vo.getQuantity() - vo.getSoldQuantity())
                ));
        Map<String, Object> result = new HashMap<>();
        result.put("productBoxResultVos", productBoxResultVos);
        result.put("boxTotalNum", groupedByBoxNumber.size());
        result.put("groupedByBoxNumber", groupedByBoxNumber);
        result.put("productQuantityMap", productQuantityMap);
        result.put("soldQuantityMap", soldQuantityMap);
        result.put("remainingQuantityMap", remainingQuantityMap);
        result.put("productSeries", productSeries);
        return ResponseUtil.ok(result);
    }


    @GetMapping("/getSpiritPowerSeries")
    public Object getSpiritPowerSeries(){
        Map<String, Object> params = new HashMap<>();
        params.put("isSpiritPower", 1);
        List<ProductSeriesVO> productSeries = productSeriesService.getWxProductSeries(params);
        return ResponseUtil.ok(productSeries);
    }

}
