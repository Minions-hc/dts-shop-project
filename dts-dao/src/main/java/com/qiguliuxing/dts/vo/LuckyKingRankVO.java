package com.qiguliuxing.dts.vo;

public class LuckyKingRankVO {

    /** 用户ID */
    private String userId;

    /** 用户名 */
    private String userName;

    /** 用户头像URL */
    private String avatar;

    /** 总积分 */
    private Integer totalPoints;

    /** A赏数量 */
    private Integer aCount;

    /** B赏数量 */
    private Integer bCount;

    /** 排名 */
    private Integer rank;

    /** 距离上一名或不在前十名就显示距离第十名差距得分数 */
    private Integer distancePoints;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    }

    public Integer getaCount() {
        return aCount;
    }

    public void setaCount(Integer aCount) {
        this.aCount = aCount;
    }

    public Integer getbCount() {
        return bCount;
    }

    public void setbCount(Integer bCount) {
        this.bCount = bCount;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Integer getDistancePoints() {
        return distancePoints;
    }

    public void setDistancePoints(Integer distancePoints) {
        this.distancePoints = distancePoints;
    }
}
