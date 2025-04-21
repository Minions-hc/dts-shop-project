package com.qiguliuxing.dts.db.service;

import com.qiguliuxing.dts.db.dao.RewardRulesMapper;
import com.qiguliuxing.dts.vo.RewardRulesVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RewardRulesService {

    @Autowired
    private RewardRulesMapper rewardRulesMapper;

    /**
     * 添加奖励规则
     */
    public int add(RewardRulesVo rewardRulesVo) {
        return rewardRulesMapper.insert(rewardRulesVo);
    }

    /**
     * 更新奖励规则
     */
    public int update(RewardRulesVo rewardRulesVo) {
        return rewardRulesMapper.updateById(rewardRulesVo);
    }

    /**
     * 删除奖励规则
     */
    public int delete(Integer id) {
        return rewardRulesMapper.deleteById(id);
    }

    /**
     * 根据ID查询奖励规则
     */
    public RewardRulesVo findById(Integer id) {
        return rewardRulesMapper.selectById(id);
    }

    /**
     * 查询所有奖励规则（按排名顺序排序）
     */
    public List<RewardRulesVo> findAll() {
        return rewardRulesMapper.selectAll();
    }

    /**
     * 根据排名顺序查询奖励规则
     */
    public RewardRulesVo findByRankOrder(Integer rankOrder) {
        return rewardRulesMapper.selectByRankOrder(rankOrder);
    }
}
