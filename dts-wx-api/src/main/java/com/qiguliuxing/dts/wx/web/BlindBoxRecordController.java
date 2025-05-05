package com.qiguliuxing.dts.wx.web;


import com.qiguliuxing.dts.core.util.ResponseUtil;
import com.qiguliuxing.dts.db.service.BlindBoxRecordService;
import com.qiguliuxing.dts.vo.BlindBoxDrawResultVO;
import com.qiguliuxing.dts.vo.BlindBoxRecordVO;
import com.qiguliuxing.dts.vo.ProductBoxResultVo;
import com.qiguliuxing.dts.vo.BlindBoxDrawRequestVO;
import com.qiguliuxing.dts.wx.dao.NumberItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 盲盒记录控制器
 */
@RestController
@RequestMapping("/wx/blindbox")
public class BlindBoxRecordController {

    @Autowired
    private BlindBoxRecordService blindBoxRecordService;


    /**
     * 查询开赏记录接口
     * @param seriesId 系列ID
     * @param boxNumber 箱子编号
     * @return 包含开赏记录的结果
     */
    @GetMapping("/numbers")
    public Object getNumbers(
            @RequestParam Integer seriesId,
            @RequestParam String boxNumber) {
        // 1. 查询所有产品信息
        List<ProductBoxResultVo> productBoxResultVos = blindBoxRecordService.selectAllProducts(seriesId, boxNumber);
        // 2. 查询已开赏记录
        List<BlindBoxRecordVO> records = blindBoxRecordService.getOpenRecords(seriesId, boxNumber);

        // 3. 计算总数量（所有产品数量的总和）
        int totalNumber = productBoxResultVos.stream()
                .mapToInt(ProductBoxResultVo::getQuantity)
                .sum();
        // 4. 创建编号列表
        List<NumberItemVO> numbers = new ArrayList<>(totalNumber);

        // 5. 初始化所有编号（1到totalNumber）
        for (int i = 1; i <= totalNumber; i++) {
            NumberItemVO item = new NumberItemVO();
            item.setNumber(i); // 设置编号
            item.setSoldOut(false); // 默认未售完
            numbers.add(item);
        }
        for (BlindBoxRecordVO record : records) {
            // 获取对应的编号项（注意：编号从1开始，列表索引从0开始）
            if (record.getNumber() > 0 && record.getNumber() <= numbers.size()) {
                NumberItemVO item = numbers.get(record.getNumber() - 1);
                item.setProductImage(record.getProductImage());
                item.setSoldOut(true);
            }
        }
        // 7. 返回结果
        return ResponseUtil.ok(numbers);
    }


    /**
     * 查询开赏记录接口
     * @param seriesId 系列ID
     * @param boxNumber 箱子编号
     * @return 包含开赏记录的结果
     */
    @GetMapping("/openRecords")
    public Object getOpenRecords(
            @RequestParam Integer seriesId,
            @RequestParam String boxNumber) {
        List<BlindBoxRecordVO> records = blindBoxRecordService.getOpenRecords(seriesId, boxNumber);
        // 使用Stream按levelName分组
        Map<String, List<BlindBoxRecordVO>> groupedByLevel = records.stream()
                .collect(Collectors.groupingBy(BlindBoxRecordVO::getLevelName));
        Map<String, Object> result = new HashMap<>();
        result.put("groupedByLevel", groupedByLevel);
        result.put("records", records);
        return ResponseUtil.ok(result);
    }

    /**
     * 抽取盲盒接口
     * @param request 抽取请求参数
     * @return 抽取结果
     */
    @PostMapping("/drawBlindBox")
    public Object drawBlindBox(@RequestBody BlindBoxDrawRequestVO request) {
        List<BlindBoxDrawResultVO> results = blindBoxRecordService.drawBlindBox(request);
        return ResponseUtil.ok(results);
    }

}
