package com.bbhackathon.trashformer.equipment.drawable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DrawableName {

    public final static Map<String, String> equipNameMap = new HashMap<String, String>() {{
        put("equipment_empty","無");
        put("equipment_f_boots","靴子");
        put("equipment_f_xmas_socks","聖誕襪");
        put("equipment_h_deer_hat","麋鹿帽");
        put("equipment_h_wreath_hat","花圈帽");
        put("equipment_h_xmas_hat","聖誕帽");
        put("equipment_h_xmas_tree_hat","聖誕樹帽");

        put("equipment_l_candle","左手蠟燭");
        put("equipment_l_candycane","左手拐杖糖");
        put("equipment_l_deco_ball","左手耶誕球");
        put("equipment_l_snowman_stick","左手木仗");
        put("equipment_l_xmas_bell","左手鈴鐺");

        put("equipment_r_candle","右手蠟燭");
        put("equipment_r_candycane","右手拐杖糖");
        put("equipment_r_deco_ball","右手耶誕球");
        put("equipment_r_snowman_stick","右手木仗");
        put("equipment_r_xmas_bell","右手鈴鐺");
    }};

    public static List<String> equipmentNameList = Arrays.asList("equipment_empty", "equipment_f_boots", "equipment_f_xmas_socks", "equipment_h_deer_hat", "equipment_h_wreath_hat",
            "equipment_h_xmas_hat", "equipment_h_xmas_tree_hat", "equipment_l_candle", "equipment_l_candycane", "equipment_l_deco_ball", "equipment_l_stick", "equipment_l_xmas_bell",
            "equipment_r_candle", "equipment_r_candycane", "equipment_r_deco_ball", "equipment_r_snowman_stick", "equipment_r_xmas_bell");

}
