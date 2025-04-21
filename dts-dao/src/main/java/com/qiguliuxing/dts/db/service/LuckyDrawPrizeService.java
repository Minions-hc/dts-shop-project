package com.qiguliuxing.dts.db.service;

import com.qiguliuxing.dts.db.dao.LuckyDrawPrizeMapper;
import com.qiguliuxing.dts.vo.LuckyDrawPrizeVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LuckyDrawPrizeService {
    @Autowired
    private LuckyDrawPrizeMapper prizeMapper;

    @Transactional
    public int create(LuckyDrawPrizeVo prize) {
        return prizeMapper.insert(prize);
    }

    @Transactional
    public int update(LuckyDrawPrizeVo prize) {
        return prizeMapper.update(prize);
    }

    @Transactional
    public int delete(Integer prizeId) {
        return prizeMapper.deleteById(prizeId);
    }

    @Transactional
    public int deleteByActivityId(Integer activityId) {
        return prizeMapper.deleteByActivityId(activityId);
    }

    public List<LuckyDrawPrizeVo> getPrizesByCondition(Integer activityId, String activityName, String productName) {
        return prizeMapper.selectByCondition(activityId, activityName, productName);
    }

    @Transactional
    public int decreaseQuantity(Integer prizeId, Integer quantity) {
        return prizeMapper.decreaseQuantity(prizeId, quantity);
    }
}
