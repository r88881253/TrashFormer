package com.bbhackathon.trashformer.equipment;

import android.os.Parcel;
import android.os.Parcelable;

public enum EquipType implements Parcelable {

    HEAD(1),
    LEFT_HAND(2),
    RIGHT_HAND(3),
    FEET(4);

    private int mValue;

    EquipType(int value){
        this.mValue = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(i);
    }

    public static final Creator<EquipType> CREATOR = new Creator<EquipType>() {
        @Override
        public EquipType createFromParcel(Parcel in) {
            return EquipType.values()[in.readInt()];
        }

        @Override
        public EquipType[] newArray(int size) {
            return new EquipType[size];
        }
    };

}
