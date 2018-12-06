package com.bbhackathon.trashformer.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserEntity implements Serializable {
    private static final long serialVersionUID = 3901229528819090698L;

    @SerializedName("UserProfileTable")
    private UserProfileTable userProfileTable;

    @SerializedName("UserMissionTable")
    private Object userMissionTable;

    @SerializedName("UserEquipmentTable")
    private Object userEqipmentTable;

    public UserEntity() {
    }

    public UserEntity(UserProfileTable userProfileTable, Object userMissionTable, Object userEqipmentTable) {
        this.userProfileTable = userProfileTable;
        this.userMissionTable = userMissionTable;
        this.userEqipmentTable = userEqipmentTable;
    }

    public UserProfileTable getUserProfileTable() {
        return userProfileTable;
    }

    public Object getUserMissionTable() {
        return userMissionTable;
    }

    public Object getUserEqipmentTable() {
        return userEqipmentTable;
    }
}
