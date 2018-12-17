package com.bbhackathon.trashformer.equipment.drawable;

import com.bbhackathon.trashformer.entity.EquipmentEntity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DrawableName {

    public final static Map<String, EquipmentEntity> equipNameMap = new HashMap<String, EquipmentEntity>() {{
        put("equipment_f_empty", new EquipmentEntity("無", "equipment_f_empty", false));
        put("equipment_f_boots", new EquipmentEntity("靴子", "equipment_f_boots", false));
        put("equipment_f_xmas_socks", new EquipmentEntity("聖誕襪", "equipment_f_xmas_socks", false));

        put("equipment_h_empty", new EquipmentEntity("無", "equipment_h_empty", false));
        put("equipment_h_deer_hat", new EquipmentEntity("麋鹿帽", "equipment_h_deer_hat", false));
        put("equipment_h_wreath_hat", new EquipmentEntity("花圈帽", "equipment_h_wreath_hat", false));
        put("equipment_h_xmas_hat", new EquipmentEntity("聖誕帽", "equipment_h_xmas_hat", false));
        put("equipment_h_xmas_tree_hat", new EquipmentEntity("聖誕樹帽", "equipment_h_xmas_tree_hat", false));

        put("equipment_l_empty", new EquipmentEntity("無", "equipment_l_empty", false));
        put("equipment_l_candle", new EquipmentEntity("左手蠟燭", "equipment_l_candle", false));
        put("equipment_l_candycane", new EquipmentEntity("左手拐杖糖", "equipment_l_candycane", false));
        put("equipment_l_deco_ball", new EquipmentEntity("左手耶誕球", "equipment_l_deco_ball", false));
        put("equipment_l_snowman_stick", new EquipmentEntity("左手木仗", "equipment_l_snowman_stick", false));
        put("equipment_l_xmas_bell", new EquipmentEntity("左手鈴鐺", "equipment_l_xmas_bell", false));

        put("equipment_r_empty", new EquipmentEntity("無", "equipment_r_empty", false));
        put("equipment_r_candle", new EquipmentEntity("右手蠟燭", "equipment_r_candle", false));
        put("equipment_r_candycane", new EquipmentEntity("右手拐杖糖", "equipment_r_candycane", false));
        put("equipment_r_deco_ball", new EquipmentEntity("右手耶誕球", "equipment_r_deco_ball", false));
        put("equipment_r_snowman_stick", new EquipmentEntity("右手木仗", "equipment_r_snowman_stick", false));
        put("equipment_r_xmas_bell", new EquipmentEntity("右手鈴鐺", "equipment_r_xmas_bell", false));
    }};

    public static List<String> equipmentNameList = Arrays.asList("equipment_empty", "equipment_f_boots", "equipment_f_xmas_socks", "equipment_h_deer_hat", "equipment_h_wreath_hat",
            "equipment_h_xmas_hat", "equipment_h_xmas_tree_hat", "equipment_l_candle", "equipment_l_candycane", "equipment_l_deco_ball", "equipment_l_stick", "equipment_l_xmas_bell",
            "equipment_r_candle", "equipment_r_candycane", "equipment_r_deco_ball", "equipment_r_snowman_stick", "equipment_r_xmas_bell");

}
