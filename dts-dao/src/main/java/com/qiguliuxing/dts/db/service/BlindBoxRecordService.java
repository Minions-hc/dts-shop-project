package com.qiguliuxing.dts.db.service;

import com.qiguliuxing.dts.db.dao.BlindBoxRecordMapper;
import com.qiguliuxing.dts.vo.BlindBoxRecordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * 盲盒记录服务
 */
@Service
public class BlindBoxRecordService {

    @Autowired
    private BlindBoxRecordMapper blindBoxRecordMapper;

    /**
     * 获取指定系列和箱子的开赏记录
     * @param seriesId 系列ID
     * @param boxNumber 箱子编号
     * @return 开赏记录列表，如果没有记录则返回空列表
     */
    public List<BlindBoxRecordVO> getOpenRecords(Integer seriesId, String boxNumber) {
        List<BlindBoxRecordVO> records = blindBoxRecordMapper.selectOpenRecordsBySeriesAndBox(seriesId, boxNumber);
        return CollectionUtils.isEmpty(records) ? Collections.emptyList() : records;
    }
}
