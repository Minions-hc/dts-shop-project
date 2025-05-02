package com.qiguliuxing.dts.db.dao;

import com.qiguliuxing.dts.vo.BoxProductVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BoxProductMapper {
    // 根据用户ID查询
    List<BoxProductVO> selectByUserId(@Param("userId") String userId, @Param("statusList") List<String> statusList);

    // 新增商品
    int insert(BoxProductVO boxProduct);

    // 动态更新
    int updateSelective(BoxProductVO boxProduct);
}
