package com.bbhackathon.trashformer.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.bbhackathon.trashformer.entity.EquipmentEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

public class LoginManager {

    private static LoginManager mLoginManager;
    private String SHARE_PREFERENCE_KEY_RECYCLE_CATEGORY = "SHARE_PREFERENCE_KEY_RECYCLE_CATEGORY";
    private String SHARE_PREFERENCE_KEY_EQUIPMENT_MAP = "SHARE_PREFERENCE_KEY_EQUIPMENT_MAP";

    private static SharedPreferences sharedPreferences;

    private LoginManager() {
    }

    public LoginManager(Context context) {
        mLoginManager = this;
        sharedPreferences = context.getSharedPreferences("TrashFormer", context.MODE_PRIVATE);
    }

    public static LoginManager getInstance(Context context) {
        if (mLoginManager == null) {
            mLoginManager = new LoginManager(context);
        }
        return mLoginManager;
    }

    public void setRecycleCategory(String recycleCategory) {
        sharedPreferences.edit().putString(SHARE_PREFERENCE_KEY_RECYCLE_CATEGORY, recycleCategory).apply();
    }

    public String getRecycleCategory() {
        return sharedPreferences.getString(SHARE_PREFERENCE_KEY_RECYCLE_CATEGORY, "");
    }

    public void clearRecycleCategory() {
        sharedPreferences.edit().remove(SHARE_PREFERENCE_KEY_RECYCLE_CATEGORY).commit();
    }

    public void setEquipmentMap(Map<String, EquipmentEntity> equipmentMap) {
        Gson gson = new Gson();
        String equipmentMapString = gson.toJson(equipmentMap);
        sharedPreferences.edit().putString(SHARE_PREFERENCE_KEY_EQUIPMENT_MAP, equipmentMapString).apply();
    }

    public Map<String, EquipmentEntity> getEquipment() {
        String storedequipmentMapString = sharedPreferences.getString(SHARE_PREFERENCE_KEY_EQUIPMENT_MAP, "");
        java.lang.reflect.Type type = new TypeToken<HashMap<String, EquipmentEntity>>() {
        }.getType();
        Gson gson = new Gson();

        Map<String, EquipmentEntity> resultMap = gson.fromJson(storedequipmentMapString, type);

        if(resultMap == null || resultMap.size() == 0){
            resultMap = new HashMap<>();
            resultMap.put("equipment_h_empty", new EquipmentEntity("無", "equipment_h_empty", true));
            resultMap.put("equipment_f_empty", new EquipmentEntity("無", "equipment_f_empty", true));
            resultMap.put("equipment_r_empty", new EquipmentEntity("無", "equipment_r_empty", true));
            resultMap.put("equipment_l_empty", new EquipmentEntity("無", "equipment_l_empty", true));
        }

        return resultMap;
    }

    public void addEquipment(EquipmentEntity equipmentEntity) {
        String storedequipmentMapString = sharedPreferences.getString(SHARE_PREFERENCE_KEY_EQUIPMENT_MAP, "");

        java.lang.reflect.Type type = new TypeToken<HashMap<String, EquipmentEntity>>() {
        }.getType();
        Gson gson = new Gson();

        Map<String, EquipmentEntity> tempEquipmentMap = gson.fromJson(storedequipmentMapString, type);

        if (tempEquipmentMap == null || tempEquipmentMap.size() == 0) {
            tempEquipmentMap = new HashMap<>();
            tempEquipmentMap.put("equipment_h_empty", new EquipmentEntity("無", "equipment_h_empty", true));
            tempEquipmentMap.put("equipment_f_empty", new EquipmentEntity("無", "equipment_f_empty", true));
            tempEquipmentMap.put("equipment_r_empty", new EquipmentEntity("無", "equipment_r_empty", true));
            tempEquipmentMap.put("equipment_l_empty", new EquipmentEntity("無", "equipment_l_empty", true));
        }

        tempEquipmentMap.put(equipmentEntity.getDrawableName(), equipmentEntity);

        setEquipmentMap(tempEquipmentMap);
    }

    public void setEquipmentEquip(String equipDrawableName){
        String storedequipmentMapString = sharedPreferences.getString(SHARE_PREFERENCE_KEY_EQUIPMENT_MAP, "");

        java.lang.reflect.Type type = new TypeToken<HashMap<String, EquipmentEntity>>() {
        }.getType();
        Gson gson = new Gson();

        Map<String, EquipmentEntity> tempEquipmentMap = gson.fromJson(storedequipmentMapString, type);

        if (tempEquipmentMap == null) {
            tempEquipmentMap = new HashMap<>();
            tempEquipmentMap.put("equipment_h_empty", new EquipmentEntity("無", "equipment_h_empty", true));
            tempEquipmentMap.put("equipment_f_empty", new EquipmentEntity("無", "equipment_f_empty", true));
            tempEquipmentMap.put("equipment_r_empty", new EquipmentEntity("無", "equipment_r_empty", true));
            tempEquipmentMap.put("equipment_l_empty", new EquipmentEntity("無", "equipment_l_empty", true));
        } else{
            if(tempEquipmentMap.get(equipDrawableName) != null){

                String tempEmptyString = tempEquipmentMap.get(equipDrawableName).getDrawableName().substring(0, 12);
                for(String key: tempEquipmentMap.keySet()){
                    if(key.contains(tempEmptyString) && tempEquipmentMap.get(key).isEquipStatus()){
                        tempEquipmentMap.get(key).setEquipStatus(false);
                    }
                }
                tempEquipmentMap.get(equipDrawableName).setEquipStatus(true);
            }
        }
        setEquipmentMap(tempEquipmentMap);
    }

    public void clearEquipment() {
        sharedPreferences.edit().remove(SHARE_PREFERENCE_KEY_EQUIPMENT_MAP).commit();
    }

}
