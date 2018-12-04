package com.bbhackathon.trashformer.entity;

public class UserProfileEntity {
    private String email;
    private String pwd;
    private String nickName;
    private int batteryRecycleCount;
    private int bottleRecycleCount;
    private int canRecycleCount;
    private int glassRecycleCount;
    private int othersRecycleCount;
    private String monsterName;
    private int level;
    private int exp;
    private float heartStatus;
    private int levelGiftCount;
    private int missionGiftCount;

    public UserProfileEntity() {
    }

    public UserProfileEntity(String email, String pwd, String nickName, int batteryRecycleCount, int bottleRecycleCount, int canRecycleCount, int glassRecycleCount,
                             int othersRecycleCount, String monsterName, int level, int exp, float heartStatus, int levelGiftCount, int missionGiftCount) {
        this.email = email;
        this.pwd = pwd;
        this.nickName = nickName;
        this.batteryRecycleCount = batteryRecycleCount;
        this.bottleRecycleCount = bottleRecycleCount;
        this.canRecycleCount = canRecycleCount;
        this.glassRecycleCount = glassRecycleCount;
        this.othersRecycleCount = othersRecycleCount;
        this.monsterName = monsterName;
        this.level = level;
        this.exp = exp;
        this.heartStatus = heartStatus;
        this.levelGiftCount = levelGiftCount;
        this.missionGiftCount = missionGiftCount;
    }

    public String getEmail() {
        return email;
    }

    public String getPwd() {
        return pwd;
    }

    public String getNickName() {
        return nickName;
    }

    public int getBatteryRecycleCount() {
        return batteryRecycleCount;
    }

    public int getBottleRecycleCount() {
        return bottleRecycleCount;
    }

    public int getCanRecycleCount() {
        return canRecycleCount;
    }

    public int getGlassRecycleCount() {
        return glassRecycleCount;
    }

    public int getOthersRecycleCount() {
        return othersRecycleCount;
    }

    public String getMonsterName() {
        return monsterName;
    }

    public int getLevel() {
        return level;
    }

    public int getExp() {
        return exp;
    }

    public float getHeartStatus() {
        return heartStatus;
    }

    public int getLevelGiftCount() {
        return levelGiftCount;
    }

    public int getMissionGiftCount() {
        return missionGiftCount;
    }
}