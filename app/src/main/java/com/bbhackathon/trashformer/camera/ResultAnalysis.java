package com.bbhackathon.trashformer.camera;

import com.bbhackathon.trashformer.entity.CameraResultEntity;
import com.bbhackathon.trashformer.type.ResultType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class ResultAnalysis {

    private Map<String, String> bottleKeywordMap;

    private Map<String, String> glassKeywordMap;

    private Map<String, String> plasticKeywordMap;

    private Map<String, String> canKeywordMap;

    private Map<String, String> batteryKeywordMap;


    public ResultAnalysis(){

        this.bottleKeywordMap = getKeywordMap(ResultType.BOTTLE);
        this.glassKeywordMap = getKeywordMap(ResultType.GLASS);
        this.plasticKeywordMap = getKeywordMap(ResultType.PLASTIC);
        this.canKeywordMap = getKeywordMap(ResultType.CAN);
        this.batteryKeywordMap = getKeywordMap(ResultType.BATTERY);

    }

    public ResultType doAnalysis(ArrayList<CameraResultEntity> entityList){

        ResultType result = ResultType.UNKNOWN;
        float confident = 0;

        // 依照信心指數排列(高至低)
        orderByConfience(entityList);

        // 初步判斷類別
        result = doInitAnalysis(entityList);

        if(result.isBottle()){

            // 進階判斷為Plastic/Bottle,若結果仍為Bottle視為Plastic
            result = doAdvanceAnalysis(entityList);
            result = result.isBottle() ? ResultType.PLASTIC : result;
        }

//        for (CameraResultEntity entity : entityList) {
//
//            // 取信心指數高於50%之結果做判斷
//            if(entity.getConfidence() > 0.5){
//
//                // 取得關鍵字
//                String keyword = entity.getText();
//
//                //取得結果
//                ResultType analysisResult = getResultType(keyword);
//
//                // 判斷結果
//                if(isHigherConfidenceResult(result, confident, analysisResult, entity.getConfidence())){
//                    result = analysisResult;
//                    confident = entity.getConfidence();
//                }
//                result = getCompareResult(result, confident, analysisResult, entity.getConfidence());
//            }
//        }

        return result;
    }

    // 初步判斷類別
    private ResultType doInitAnalysis(ArrayList<CameraResultEntity> entityList){

        ResultType result = ResultType.UNKNOWN;

        float bottleConfident = 0;

        float canConfident = 0;

        float batteryConfident = 0;

        int canCount = 0;

        int batteryCount = 0;

        int bottleCount = 0;

        for(CameraResultEntity entity : entityList){

            //取得結果(Battery, Can, Bottle)
            ResultType analysisResult = getResultType(entity.getText(), true);

            if(analysisResult.isCan()){

                canConfident += entity.getConfidence();
                canCount++;

            }else if (analysisResult.isBattery()){

                batteryConfident += entity.getConfidence();
                batteryCount++;

            }else if(analysisResult.isBottle()){

                bottleConfident += entity.getConfidence();
                bottleCount++;

            }
        }

        // 計算信心指數平均
        if(canCount > 0){
            canConfident = canConfident / canCount;
        }

        if(batteryCount > 0){
            batteryConfident = batteryConfident / batteryCount;
        }

        if(bottleCount > 0){
            bottleConfident = bottleConfident / bottleCount;
        }

        // 取得初步類別
        if(bottleConfident > batteryConfident && bottleConfident > canConfident){

            result = ResultType.BOTTLE;

        }else if(batteryConfident > canConfident && batteryConfident > bottleConfident){

            result = ResultType.BATTERY;

        }else if(canConfident > bottleConfident && canConfident > batteryConfident){

            result = ResultType.CAN;
        }

        return result;
    }

    // 進階判斷結果
    private ResultType doAdvanceAnalysis(ArrayList<CameraResultEntity> entityList){

        ResultType result = ResultType.BOTTLE;

        float glassConfident = 0;

        float plasticConfident = 0;

        int glassCount = 0;

        int plasticCount = 0;

        for(CameraResultEntity entity : entityList){

            //取得結果(Glass, Plastic)
            ResultType analysisResult = getResultType(entity.getText(), false);

            if(analysisResult.isGlass()){

                glassConfident += entity.getConfidence();
                glassCount++;

            } else if(analysisResult.isPlastic()){

                plasticConfident += entity.getConfidence();
                plasticCount++;

            }
        }

        if(glassCount > 0){
            glassConfident = glassCount / glassCount;
        }

        if(plasticCount > 0){
            plasticConfident = plasticConfident / plasticCount;
        }

        if(glassConfident > plasticConfident){
            result = ResultType.GLASS;
        }else if(plasticConfident > glassConfident){
            result = ResultType.PLASTIC;
        }

        return result;
    }


    // 依照信心指數排列(高至低)
    private void orderByConfience(ArrayList<CameraResultEntity> entityList){

        // Sorting
        Collections.sort(entityList, new Comparator<CameraResultEntity>() {
            @Override
            public int compare(CameraResultEntity cameraResultEntity1, CameraResultEntity cameraResultEntity2) {
                Float entity1Confidence = cameraResultEntity1.getConfidence();
                Float entity2Confidence = cameraResultEntity2.getConfidence();
                // 降序排序
                return entity2Confidence.compareTo(entity1Confidence);
            }
        });
    }

    private ResultType getResultType(String keyword, boolean isInitAnalysis){

        ResultType result = ResultType.UNKNOWN;

        if(keyword != null && isInitAnalysis){

            if(batteryKeywordMap.containsKey(keyword.toLowerCase())){
                return ResultType.BATTERY;
            }

            if(canKeywordMap.containsKey(keyword.toLowerCase())){
                return ResultType.CAN;
            }


            if(bottleKeywordMap.containsKey(keyword.toLowerCase())){
                return ResultType.BOTTLE;
            }

        }else if(keyword != null && !isInitAnalysis){

            if(plasticKeywordMap.containsKey(keyword.toLowerCase())){
                return ResultType.PLASTIC;
            }

            if(glassKeywordMap.containsKey(keyword.toLowerCase())){
                return ResultType.GLASS;
            }

        }

        return result;
    }

    private boolean isHigherConfidenceResult(ResultType result, float confident, ResultType analysisResult, float analysisConfident){
        // 若不為未知才要判斷
        if(analysisResult != ResultType.UNKNOWN){
            // 判斷信心指數是否比較高，較高則回true，要取代
            if(confident >= analysisConfident){
                return false;
            } else {
                return true;
            }
         //未知直接回false
        } else{
            return false;
        }
    }

    private ResultType getCompareResult(ResultType result, float confident, ResultType analysisResult, float analysisConfident){

        if(analysisResult.isUnknow()){
            return  result;
        }

//        if(result.isGlass() && analysisResult.isBottle()){
//            return result;
//        }

        if(confident > analysisConfident){
            return result;
        }else {
            return analysisResult;
        }

    }

    private Map<String, String> getKeywordMap(ResultType resultType){

        Map<String, String> resultMap = new HashMap<>();

        if(resultType.isBottle()){
            resultMap.put("bottle","bottle");
            resultMap.put("plastic bottle","plastic bottle");
            resultMap.put("glass","glass");
            resultMap.put("glass bottle","glass bottle");
            resultMap.put("beer bottle","beer bottle");
            resultMap.put("wine bottle","wine bottle");
            resultMap.put("mason jar","mason jar");
        }

        if(resultType.isGlass()){
            resultMap.put("glass","glass");
            resultMap.put("glass bottle","glass bottle");
            resultMap.put("beer bottle","beer bottle");
            resultMap.put("wine bottle","wine bottle");
            resultMap.put("mason jar","mason jar");
        }

        if(resultType.isPlastic()){
            resultMap.put("plastic","plastic");
            resultMap.put("plastic bottle","plastic bottle");
        }

        if(resultType.isCan()){
            resultMap.put("can","can");
            resultMap.put("tin can","tin can");
            resultMap.put("aluminum can","aluminum can");
        }

        if(resultType.isBattery()){
            resultMap.put("battery","battery");
            resultMap.put("electronics accessory","electronics accessory");
            resultMap.put("electronic device","electronic device");
        }

        return resultMap;
    }

}
