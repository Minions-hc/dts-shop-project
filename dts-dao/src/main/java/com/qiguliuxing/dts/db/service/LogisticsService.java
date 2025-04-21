package com.qiguliuxing.dts.db.service;
import com.qiguliuxing.dts.db.dao.LogisticsMapper;
import com.qiguliuxing.dts.vo.LogisticsInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class LogisticsService {

    @Autowired
    private LogisticsMapper logisticsMapper;

    public void addLogistics(LogisticsInfoVO vo) {
        logisticsMapper.addLogistics(vo);
    }

    public void deleteLogistics(Integer id) {
        logisticsMapper.deleteLogistics(id);
    }

    public void updateLogistics(LogisticsInfoVO vo) {
        logisticsMapper.updateLogistics(vo);
    }

    public List<LogisticsInfoVO> queryLogisticsByCondition(Map<String, Object> condition) {
        return logisticsMapper.queryLogisticsByCondition(condition);
    }

}
