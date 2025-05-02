package com.qiguliuxing.dts.db.service;

import com.qiguliuxing.dts.db.dao.LuckyDrawActivityMapper;
import com.qiguliuxing.dts.vo.LuckyDrawActivityVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class LuckyDrawActivityService {
    @Autowired
    private LuckyDrawActivityMapper activityMapper;
    @Autowired
    private LuckyDrawPrizeService prizeService;

    @Transactional
    public int create(LuckyDrawActivityVo activity) {
        // 设置所有活动为无效状态
        activityMapper.setAllActivitiesInactive();
        return activityMapper.insert(activity);
    }

    @Transactional
    public int update(LuckyDrawActivityVo activity) {
        // 如果当前更新设置为有效则设置其他活动为无效状态
        if (activity.isActive()) {
            activityMapper.setAllActivitiesInactive();
        }
        return activityMapper.update(activity);
    }

    @Transactional
    public int delete(Integer activityId) {
        prizeService.deleteByActivityId(activityId);
        return activityMapper.deleteById(activityId);
    }

    public LuckyDrawActivityVo findById(Integer activityId) {
        return activityMapper.findById(activityId);
    }

    public List<LuckyDrawActivityVo> findAll() {
        return activityMapper.findAll();
    }

    public LuckyDrawActivityVo findByPeriodNumber(Integer periodNumber) {
        return activityMapper.findByPeriodNumber(periodNumber);
    }

    /**
     * 查询指定开奖日期的有效活动
     */
    public LuckyDrawActivityVo selectValidActivitiy(){
        return activityMapper.selectValidActivitiy();
    }

    /**
     * 更新活动状态
     */
    void updateActivityStatus(@Param("activityId") Integer activityId,
                              @Param("isActive") Integer isActive){
        activityMapper.updateActivityStatus(activityId, isActive);
    }
}
