package com.qiguliuxing.dts.db.dao;

import com.qiguliuxing.dts.vo.BlindBoxRecordVO;
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
}
