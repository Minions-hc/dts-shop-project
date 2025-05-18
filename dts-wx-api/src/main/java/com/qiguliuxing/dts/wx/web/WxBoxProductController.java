package com.qiguliuxing.dts.wx.web;

import com.qiguliuxing.dts.core.util.JacksonUtil;
import com.qiguliuxing.dts.core.util.ResponseUtil;
import com.qiguliuxing.dts.db.service.BoxProductService;
import com.qiguliuxing.dts.vo.BoxProductVO;
import com.qiguliuxing.dts.wx.service.WxOrderService;
import com.qiguliuxing.dts.wx.util.ProductLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 盒柜服务
 */
@RestController
@RequestMapping("/wx/boxproduct")
@Validated
public class WxBoxProductController {

    @Autowired
    private BoxProductService boxProductService;

    @Autowired
    private WxOrderService wxOrderService;

    @GetMapping("/getProductsByUser")
    public Object getProductsByUser(String userId, String status) {
        List<String> statusList = new ArrayList<>();
        if (status.equals("all")) {
            statusList.add("pending");
            statusList.add("locked");
            statusList.add("shipped");
        } else {
            statusList.add(status);
        }
        List<BoxProductVO> products = boxProductService.getProductsByUserId(userId, statusList);
        List<BoxProductVO> aProducts = products.stream().filter(boxProductVO -> boxProductVO.getProductLevel().equals(ProductLevel.A_PRIZE.getChineseName())).collect(Collectors.toList());
        List<BoxProductVO> bProducts = products.stream().filter(boxProductVO -> boxProductVO.getProductLevel().equals(ProductLevel.B_PRIZE.getChineseName())).collect(Collectors.toList());
        List<BoxProductVO> finalProducts = products.stream().filter(boxProductVO -> boxProductVO.getProductLevel().equals(ProductLevel.FINAL_PRIZE.getChineseName())).collect(Collectors.toList());
        List<BoxProductVO> otherProducts = products.stream().filter(boxProductVO -> boxProductVO.getProductLevel().equals(ProductLevel.OTHER.getChineseName())).collect(Collectors.toList());
        Map<String, Object> data = new HashMap<>();
        data.put("allProducts", products);
        data.put("aProducts", aProducts);
        data.put("bProducts", bProducts);
        data.put("finalProducts", finalProducts);
        data.put("otherProducts", otherProducts);
        return ResponseUtil.ok(data);
    }

    /**
     * 盒柜锁定
     * @param boxProduct
     * @return
     */
    @PostMapping("/locked")
    public Object locked(@RequestBody BoxProductVO boxProduct) {
        int result = boxProductService.updateProductStatus(boxProduct);
        if (result == 1) {
            return ResponseUtil.ok();
        }
        return ResponseUtil.fail();
    }


    /**
     * 盒柜提交发货
     */
    @PostMapping("/submitDelivery")
    public Object submitDelivery(@RequestBody String body) {
        String userId = JacksonUtil.parseString(body, "userId");
        List<Integer> ids = JacksonUtil.parseIntegerList(body, "ids");
        boxProductService.shipProducts(userId, ids);
        return ResponseUtil.ok();
    }

    /**
     * 盒柜提交发货
     */
    @GetMapping("/boxProductInfo")
    public Object boxProductInfo(@PathParam("userId") String userId, @PathParam("id") Integer id) {
        BoxProductVO boxProductVO = boxProductService.selectByIdAndUserId(id, userId);
        return ResponseUtil.ok(boxProductVO);
    }

}
