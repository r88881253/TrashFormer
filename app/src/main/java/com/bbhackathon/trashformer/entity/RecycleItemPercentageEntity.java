package com.bbhackathon.trashformer.entity;

import android.databinding.ObservableField;

import java.text.DecimalFormat;

public class RecycleItemPercentageEntity {
    private ObservableField<Double> bottlePercentage = new ObservableField<>(0d);
    private ObservableField<Double>  canPercentage = new ObservableField<>(0d);
    private ObservableField<Double>  batteryPercentage = new ObservableField<>(0d);
    private ObservableField<Double>  glassPercentage = new ObservableField<>(0d);

    public RecycleItemPercentageEntity() {
    }

    public ObservableField<Double> getBottlePercentage() {
        return bottlePercentage;
    }

    public void setBottlePercentage(ObservableField<Double> bottlePercentage) {
        this.bottlePercentage = bottlePercentage;
    }

    public ObservableField<Double> getCanPercentage() {
        return canPercentage;
    }

    public void setCanPercentage(ObservableField<Double> canPercentage) {
        this.canPercentage = canPercentage;
    }

    public ObservableField<Double> getBatteryPercentage() {
        return batteryPercentage;
    }

    public void setBatteryPercentage(ObservableField<Double> batteryPercentage) {
        this.batteryPercentage = batteryPercentage;
    }

    public ObservableField<Double> getGlassPercentage() {
        return glassPercentage;
    }

    public void setGlassPercentage(ObservableField<Double> glassPercentage) {
        this.glassPercentage = glassPercentage;
    }

    public void setRecyclePercentage(UserProfileTable userProfile){
        int amount = userProfile.getBottleRecycleCount() + userProfile.getCanRecycleCount() +
                userProfile.getBatteryRecycleCount() + userProfile.getGlassRecycleCount();

        if(amount != 0){
            DecimalFormat df = new DecimalFormat("##.0");
            bottlePercentage.set(Double.parseDouble(df.format(userProfile.getBottleRecycleCount()*100d / amount)));
            canPercentage.set(Double.parseDouble(df.format(userProfile.getCanRecycleCount()*100d / amount)));
            batteryPercentage.set(Double.parseDouble(df.format(userProfile.getBatteryRecycleCount()*100d / amount)));
            glassPercentage.set(Double.parseDouble(df.format(userProfile.getGlassRecycleCount()*100d / amount)));
        }else{
            batteryPercentage.set(0d);
            canPercentage.set(0d);
            batteryPercentage.set(0d);
            glassPercentage.set(0d);
        }
    }
}
