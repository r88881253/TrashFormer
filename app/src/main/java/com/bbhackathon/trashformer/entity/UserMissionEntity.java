package com.bbhackathon.trashformer.entity;

public class UserMissionEntity {
    private int recycleAmt;
    private int recycleCount;
    private String recycleType;

    public UserMissionEntity() {
    }

    public UserMissionEntity(int recycleAmt, int recycleCount, String recycleType) {
        this.recycleAmt = recycleAmt;
        this.recycleCount = recycleCount;
        this.recycleType = recycleType;
    }

    public int getRecycleAmt() {
        return recycleAmt;
    }

    public void setRecycleAmt(int recycleAmt) {
        this.recycleAmt = recycleAmt;
    }

    public int getRecycleCount() {
        return recycleCount;
    }

    public void setRecycleCount(int recycleCount) {
        this.recycleCount = recycleCount;
    }

    public String getRecycleType() {
        return recycleType;
    }

    public void setRecycleType(String recycleType) {
        this.recycleType = recycleType;
    }
}