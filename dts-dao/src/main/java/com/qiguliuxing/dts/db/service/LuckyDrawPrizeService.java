package com.qiguliuxing.dts.db.service;

import com.qiguliuxing.dts.db.dao.LuckyDrawPrizeMapper;
import com.qiguliuxing.dts.db.dao.RedemptionCodeMapper;
import com.qiguliuxing.dts.db.util.RedemptionCodeType;
import com.qiguliuxing.dts.vo.LuckyDrawPrizeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class LuckyDrawPrizeService {
    @Autowired
    private LuckyDrawPrizeMapper prizeMapper;

    @Autowired
    private RedemptionCodeService redemptionCodeService;

    @Transactional
    public int create(LuckyDrawPrizeVo prize) {
        // 更新兑换码类型为奖品
        redemptionCodeService.batchUpdateCodeType(Collections.singletonList(prize.getRedemptionCode()), RedemptionCodeType.PRIZE.getValue());
        return prizeMapper.insert(prize);
    }

    @Transactional
    public int update(LuckyDrawPrizeVo prize) {
        return prizeMapper.update(prize);
    }

    @Transactional
    public int delete(Integer prizeId) {
        LuckyDrawPrizeVo luckyDrawPrizeVo = prizeMapper.selectById(prizeId);
        // 删除奖品时将兑换码类型改为未分配
        redemptionCodeService.batchUpdateCodeType(Collections.singletonList(luckyDrawPrizeVo.getRedemptionCode()), RedemptionCodeType.UNASSIGNED.getValue());
        return prizeMapper.deleteById(prizeId);
    }

    @Transactional
    public int deleteByActivityId(Integer activityId) {
        List<LuckyDrawPrizeVo> luckyDrawPrizeVos = prizeMapper.selectByCondition(activityId, null, null);
        List<String> redemptionCodes = new ArrayList<>();
        luckyDrawPrizeVos.forEach(prize-> redemptionCodes.add(prize.getRedemptionCode()));
        // 删除奖品时将兑换码类型改为未分配
        redemptionCodeService.batchUpdateCodeType(redemptionCodes, RedemptionCodeType.UNASSIGNED.getValue());
        return prizeMapper.deleteByActivityId(activityId);
    }

    public List<LuckyDrawPrizeVo> getPrizesByCondition(Integer activityId, String activityName, String productName) {
        return prizeMapper.selectByCondition(activityId, activityName, productName);
    }


    @Transactional
    public int decreaseQuantity(Integer prizeId, Integer quantity) {
        return prizeMapper.decreaseQuantity(prizeId, quantity);
    }

    /**
     * 根据兑换码查询奖品信息
     * @param redemptionCode 兑换码
     * @return 奖品信息VO
     */
    public LuckyDrawPrizeVo getPrizeByRedemptionCode(String redemptionCode) {
        return prizeMapper.selectByRedemptionCode(redemptionCode);
    }
}
