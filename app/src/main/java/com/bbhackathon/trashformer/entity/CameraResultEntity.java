package com.bbhackathon.trashformer.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class CameraResultEntity implements Parcelable {

    String text;
    String entityId;
    float confidence;

    public CameraResultEntity(String text, String entityId, float confidence){
        this.text = text;
        this.entityId = entityId;
        this.confidence = confidence;
    }

    public static final Parcelable.Creator<CameraResultEntity> CREATOR = new Parcelable.Creator<CameraResultEntity>()
    {
        public CameraResultEntity createFromParcel(Parcel in)
        {
            return new CameraResultEntity(in);//创建一个有参构造函数
        }

        public CameraResultEntity[] newArray(int size)
        {
            return new CameraResultEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(text);
        parcel.writeString(entityId);
        parcel.writeFloat(confidence);

    }

    public CameraResultEntity(Parcel in)//根据写入的顺序依次读取
    {
        text = in.readString();
        entityId = in.readString();
        confidence = in.readFloat();
    }

    public String getText() {
        return text;
    }

    public String getEntityId() {
        return entityId;
    }

    public float getConfidence() {
        return confidence;
    }
}
