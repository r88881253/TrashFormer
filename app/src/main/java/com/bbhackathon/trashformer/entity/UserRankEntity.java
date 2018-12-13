package com.bbhackathon.trashformer.entity;

public class UserRankEntity {
    private String name;
    private String monsterName;
    private int level;
    private int exp;
    private String uid;

    public UserRankEntity(String name, String monsterName, int level, int exp, String uid) {
        this.name = name;
        this.monsterName = monsterName;
        this.level = level;
        this.exp = exp;
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMonsterName() {
        return monsterName;
    }

    public void setMonsterName(String monsterName) {
        this.monsterName = monsterName;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
