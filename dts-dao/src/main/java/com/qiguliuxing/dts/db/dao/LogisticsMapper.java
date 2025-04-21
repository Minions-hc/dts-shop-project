package com.qiguliuxing.dts.db.dao;

import com.qiguliuxing.dts.vo.LogisticsInfoVO;

import java.util.List;
import java.util.Map;

public interface LogisticsMapper {

    void addLogistics(LogisticsInfoVO vo);

    void deleteLogistics(Integer id);

    void updateLogistics(LogisticsInfoVO vo);

    List<LogisticsInfoVO> queryLogisticsByCondition(Map<String, Object> param);
}
