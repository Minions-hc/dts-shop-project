package com.qiguliuxing.dts.db.dao;

import com.qiguliuxing.dts.vo.BlindBoxRecordVO;
import com.qiguliuxing.dts.vo.ProductBoxResultVo;
import com.qiguliuxing.dts.vo.ProductBoxVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 盲盒记录Mapper
 */
public interface BlindBoxRecordMapper {

    /**
     * 查询指定系列和箱子的开赏记录
     * @param seriesId 系列ID
     * @param boxNumber 箱子编号
     * @return 开赏记录列表
     */
    List<BlindBoxRecordVO> selectOpenRecordsBySeriesAndBox(
            @Param("seriesId") Integer seriesId,
            @Param("boxNumber") String boxNumber);


    /**
     * 查询指定系列可用的产品列表
     * @param seriesId 系列ID
     * @param boxNumber 箱子编号
     * @return 可用产品列表
     */
    List<ProductBoxResultVo> selectAvailableProducts(
            @Param("seriesId") Integer seriesId,
            @Param("boxNumber") String boxNumber);

    /**
     * 查询指定系列所有的产品列表
     * @param seriesId 系列ID
     * @param boxNumber 箱子编号
     * @return 可用产品列表
     */
    List<ProductBoxResultVo> selectAllProducts(
            @Param("seriesId") Integer seriesId,
            @Param("boxNumber") String boxNumber);


    /**
     * 更新已售数量
     * @param boxId 盲盒ID
     * @param productId 产品ID
     * @param quantity 增加的数量
     * @return 影响行数
     */
    int updateSoldQuantity(
            @Param("boxId") Integer boxId,
            @Param("productId") Integer productId,
            @Param("quantity") Integer quantity);

    /**
     * 插入抽取记录
     * @param userId 用户ID
     * @param number 编号
     * @param seriesId 系列ID
     * @param boxNumber 箱子编号
     * @param productId 产品ID
     * @return 影响行数
     */
    int insertDrawRecord(
            @Param("userId") String userId,
            @Param("number") Integer number,
            @Param("seriesId") Integer seriesId,
            @Param("boxNumber") String boxNumber,
            @Param("productId") Integer productId);
}
