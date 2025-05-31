package com.qiguliuxing.dts.db.dao;

import com.qiguliuxing.dts.vo.BoxProductVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BoxProductMapper {
    // 根据用户ID查询
    List<BoxProductVO> selectByUserId(@Param("userId") String userId, @Param("statusList") List<String> statusList);

    /**
     * 根据ID和用户ID查询盒柜产品
     */
    BoxProductVO selectByIdAndUserId(
            @Param("id") Integer id,
            @Param("userId") String userId);

    // 新增商品
    int insert(BoxProductVO boxProduct);

    // 动态更新
    int updateSelective(BoxProductVO boxProduct);


    /**
     * 动态查询盒柜商品
     * @param userId 用户ID
     * @param activityTypeList 活动类型
     * @param statusList 状态
     * @return 盒柜商品列表
     */
    List<BoxProductVO> selectBoxProducts(
            @Param("userId") String userId,
            @Param("activityTypeList") List<String> activityTypeList,
            @Param("statusList") List<String> statusList);

    /**
     * 使用微信订单号查询盒柜
     * @param wxOrderNo 微信订单号
     * @return 盒柜商品列表
     */
    List<BoxProductVO> selectBoxProductsByWxOrderNo(String wxOrderNo);
}
