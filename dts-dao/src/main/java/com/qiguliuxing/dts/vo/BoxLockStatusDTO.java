package com.qiguliuxing.dts.vo;

import java.util.Date;

public class BoxLockStatusDTO {
    private boolean isLocked;
    private boolean isOwnedByCurrentUser;
    private Date unlockTime;
    private Integer lockCount;
    private String lockedByUserId;

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public boolean isOwnedByCurrentUser() {
        return isOwnedByCurrentUser;
    }

    public void setOwnedByCurrentUser(boolean ownedByCurrentUser) {
        isOwnedByCurrentUser = ownedByCurrentUser;
    }

    public Date getUnlockTime() {
        return unlockTime;
    }

    public void setUnlockTime(Date unlockTime) {
        this.unlockTime = unlockTime;
    }

    public Integer getLockCount() {
        return lockCount;
    }

    public void setLockCount(Integer lockCount) {
        this.lockCount = lockCount;
    }

    public String getLockedByUserId() {
        return lockedByUserId;
    }

    public void setLockedByUserId(String lockedByUserId) {
        this.lockedByUserId = lockedByUserId;
    }
}
