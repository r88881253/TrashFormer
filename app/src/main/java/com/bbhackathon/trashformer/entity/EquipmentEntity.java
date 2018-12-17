package com.bbhackathon.trashformer.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class EquipmentEntity implements Parcelable {
    private String name;
    private String drawableName;
    private boolean isEquipStatus;

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public EquipmentEntity createFromParcel(Parcel in) {

            EquipmentEntity equipmentEntity = new EquipmentEntity(in.readString(), in.readString(), in.readInt() == 1 ? true : false);
            equipmentEntity.setName(in.readString());
            equipmentEntity.setDrawableName(in.readString());
            equipmentEntity.setEquipStatus((in.readInt() == 1) ? true : false);

            return equipmentEntity;
        }

        public EquipmentEntity[] newArray(int size) {
            return new EquipmentEntity[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(drawableName);
        parcel.writeInt(isEquipStatus?1 : 0);
    }

    public EquipmentEntity(String name, String drawableName, boolean isEquipStatus) {
        this.name = name;
        this.drawableName = drawableName;
        this.isEquipStatus = isEquipStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDrawableName() {
        return drawableName;
    }

    public void setDrawableName(String drawableName) {
        this.drawableName = drawableName;
    }

    public boolean isEquipStatus() {
        return isEquipStatus;
    }

    public void setEquipStatus(boolean equipStatus) {
        this.isEquipStatus = equipStatus;
    }
}
