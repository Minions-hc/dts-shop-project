package com.qiguliuxing.dts.wx.web;

import com.qiguliuxing.dts.db.service.LuckyKingRankService;
import com.qiguliuxing.dts.vo.LuckyKingRankVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("wx/rank")
public class WxLuckyKingRankController {


    @Resource
    private LuckyKingRankService luckyKingRankService;

    /**
     * 获取当月欧皇榜TOP10
     */
    @GetMapping("/lucky-king")
    public List<LuckyKingRankVO> getLuckyKingRank() {
        return luckyKingRankService.getCurrentMonthTop10();
    }



    /**
     * 获取当月全体用户欧皇榜总积分
     * @return 所有用户的总积分和
     */
    @GetMapping("/lucky-king/total-points")
    public Integer getTotalLuckyKingPoints() {
        return luckyKingRankService.getTotalLuckyKingPoints();
    }
}
