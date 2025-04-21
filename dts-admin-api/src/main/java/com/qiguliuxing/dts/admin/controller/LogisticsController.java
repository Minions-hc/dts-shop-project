package com.qiguliuxing.dts.admin.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.qiguliuxing.dts.core.util.ResponseUtil;
import com.qiguliuxing.dts.db.domain.DtsAdmin;
import com.qiguliuxing.dts.db.service.IProductSeriesService;
import com.qiguliuxing.dts.db.service.LogisticsService;
import com.qiguliuxing.dts.vo.LogisticsInfoVO;
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
@RequestMapping("/admin/logistics")
public class LogisticsController {

    private static final Logger logger = LoggerFactory.getLogger(LogisticsController.class);

    @Autowired
    private LogisticsService logisticsService;

    @PostMapping("/createLogistics")
    public Object createLogistics(@RequestBody LogisticsInfoVO logisticsInfoVO) {
        logisticsService.addLogistics(logisticsInfoVO);
        return ResponseUtil.ok(logisticsInfoVO);

    }

    @PostMapping("deleteLogistics")
    public Object deleteLogistics(@RequestBody LogisticsInfoVO logisticsInfoVO) {
        logisticsService.deleteLogistics(logisticsInfoVO.getId());
        return ResponseUtil.ok();
    }

    @PostMapping("/updateLogistics")
    public Object updateLogistics(@RequestBody LogisticsInfoVO logisticsInfoVO) {
        logisticsService.updateLogistics(logisticsInfoVO);
        return ResponseUtil.ok(logisticsInfoVO);
    }



    @GetMapping("/listLogistics")
    public Object listLogistics(String logisticsCode, String logisticsName) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("logisticsCode", logisticsCode);
        paramMap.put("logisticsName", logisticsName);
        List<LogisticsInfoVO> logisticsInfoVOS = logisticsService.queryLogisticsByCondition(paramMap);
        long total = PageInfo.of(logisticsInfoVOS).getTotal();
        Map<String, Object> data = new HashMap<>();
        data.put("total", total);
        data.put("items", logisticsInfoVOS);
        return ResponseUtil.ok(data);
    }
}
