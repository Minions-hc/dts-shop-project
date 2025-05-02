package com.qiguliuxing.dts.db.dao;

import com.qiguliuxing.dts.vo.RedemptionCodeVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RedemptionCodeMapper {
    int insert(RedemptionCodeVo redemptionCodeVo);
    int deleteByCode(@Param("code") String code);
    RedemptionCodeVo selectByCode(@Param("code") String code);
    List<RedemptionCodeVo> selectAll(
            @Param("codeType") Integer codeType,
            @Param("code") String code,
            @Param("available") Boolean available,
            @Param("offset") Integer offset,
            @Param("limit") Integer limit
    );

    /**
     * 批量更新兑换码可用状态
     * @param codes 兑换码列表
     * @param available 是否可用
     * @return 影响的行数
     */
    int batchUpdateStatus(@Param("codes") List<String> codes, @Param("available") Boolean available);

    /**
     * 批量更新兑换码类型
     * @param codes 兑换码列表
     * @param codeType 要更新的类型
     * @return 影响的行数
     */
    int batchUpdateCodeType(@Param("codes") List<String> codes, @Param("codeType") Integer codeType);

}
