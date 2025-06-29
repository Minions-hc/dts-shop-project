package com.qiguliuxing.dts.db.dao;

import com.qiguliuxing.dts.vo.BoxLockRecordVO;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface BoxLockRecordMapper {
    Optional<BoxLockRecordVO> findActiveLockByUserId(@Param("userId") String userId);

    Optional<BoxLockRecordVO> findActiveLockByBox(
            @Param("seriesId") Integer seriesId,
            @Param("boxNumber") String boxNumber);

    List<Long> findExpiredLockIds(
            @Param("currentTime") Date currentTime,
            @Param("limit") int limit);


    int unlockExpiredRecords(@Param("ids") List<Long> ids);

    boolean isBoxLockedByUser(
            @Param("userId") String userId,
            @Param("seriesId") Integer seriesId,
            @Param("boxNumber") String boxNumber);

    int unlockAllByUser(@Param("userId") String userId);

    int unlockBox(
            @Param("seriesId") Integer seriesId,
            @Param("boxNumber") String boxNumber);

    Optional<BoxLockRecordVO> getUserCurrentLock(@Param("userId") String userId);

    int getLockCountForUserAndBox(
            @Param("userId") String userId,
            @Param("seriesId") Integer seriesId,
            @Param("boxNumber") String boxNumber);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(BoxLockRecordVO record);

    int updateLock(BoxLockRecordVO record);
}