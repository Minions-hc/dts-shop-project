package com.qiguliuxing.dts.wx.web;

import com.qiguliuxing.dts.core.util.ResponseUtil;
import com.qiguliuxing.dts.db.service.PointsTransactionService;
import com.qiguliuxing.dts.vo.PointsTransactionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/wx/points")
@Validated
public class WxPointsTransactionController {

    @Autowired
    private PointsTransactionService pointsTransactionService;

    @GetMapping("/getTransactionsByUser")
    public Object getTransactionsByUser(String userId) {
        List<PointsTransactionVO> transactionsByUser = pointsTransactionService.getTransactionsByUser(userId);
        Map<String, Object> data = new HashMap<>();
        data.put("items", transactionsByUser);
        return ResponseUtil.ok(data);
    }


    @GetMapping("/getUserCurrentPoints")
    public Object getUserCurrentPoints(String userId) {
        Integer currentPoints = pointsTransactionService.getUserCurrentPoints(userId);
        Map<String, Object> data = new HashMap<>();
        data.put("currentPoints", currentPoints);
        return ResponseUtil.ok(data);
    }
}
