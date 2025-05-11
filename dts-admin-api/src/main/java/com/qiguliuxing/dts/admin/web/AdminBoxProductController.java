package com.qiguliuxing.dts.admin.web;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qiguliuxing.dts.core.util.ResponseUtil;
import com.qiguliuxing.dts.db.service.BoxProductService;
import com.qiguliuxing.dts.vo.BoxProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/box")
public class AdminBoxProductController {

    @Autowired
    private BoxProductService boxProductService;

    /**
     * 动态查询盒柜商品
     * @param userId 用户ID(精确查询)H
     * @param activityType 活动类型(精确查询)
     * @param status 状态(精确查询)
     * @return 盒柜商品列表
     */
    @GetMapping("/products")
    public Object queryBoxProducts(@RequestParam(required = false) String userId,
                                   @RequestParam(required = false) String activityType,
                                   @RequestParam(required = false) String status,
                                   @RequestParam(defaultValue = "1") Integer page,
                                   @RequestParam(defaultValue = "20") Integer limit,
                                   @RequestParam(defaultValue = "add_time") String sort,
                                   @RequestParam(defaultValue = "desc") String order) {
        // 处理分页
        PageHelper.startPage(page, limit);
        List<BoxProductVO> boxProductVOS = boxProductService.queryBoxProducts(userId, activityType, status);
        long total = PageInfo.of(boxProductVOS).getTotal();
        Map<String, Object> data = new HashMap<>();
        data.put("total", total);
        data.put("items", boxProductVOS);
        return ResponseUtil.ok(data);
    }
}
