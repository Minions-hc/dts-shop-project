package com.qiguliuxing.dts.wx.web;


import com.qiguliuxing.dts.core.util.ResponseUtil;
import com.qiguliuxing.dts.db.dao.BlindBoxRecordMapper;
import com.qiguliuxing.dts.db.service.BlindBoxRecordService;
import com.qiguliuxing.dts.vo.BlindBoxRecordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 盲盒记录控制器
 */
@RestController
@RequestMapping("/wx/blindbox/records")
public class BlindBoxRecordController {

    @Autowired
    private BlindBoxRecordService blindBoxRecordService;


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
        return ResponseUtil.ok(records);
    }



}
