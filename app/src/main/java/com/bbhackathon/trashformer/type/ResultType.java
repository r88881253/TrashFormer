package com.bbhackathon.trashformer.type;

public enum ResultType {

    BOTTLE(1, "寶特瓶"),

    CAN(2, "鐵鋁罐"),

    BATTERY(3, "電池"),

    GLASS(4, "玻璃"),

    PLASTIC(5, "寶特瓶"),

    UNKNOWN(99, "未知");

    private int code;

    private String memo;

    ResultType(int code, String memo) {
        this.code = code;
        this.memo = memo;
    }

    public int getCode() {
        return code;
    }

    public String getMemo() {
        return memo;
    }

    public boolean isBottle() {
        return equals(ResultType.BOTTLE);
    }

    public boolean isGlass() {
        return equals(ResultType.GLASS);
    }

    public boolean isPlastic(){return equals(ResultType.PLASTIC);}

    public boolean isCan() {
        return equals(ResultType.CAN);
    }

    public boolean isBattery() {
        return equals(ResultType.BATTERY);
    }

    public boolean isUnknow() {
        return equals(ResultType.UNKNOWN);
    }

    public static ResultType getResultType(String memo) {
        switch (memo) {
            case "寶特瓶":
                return BOTTLE;
            case "鐵鋁罐":
                return CAN;
            case "電池":
                return BATTERY;
            case "玻璃":
                return GLASS;
            case "塑膠":
                return PLASTIC;
            default:
                return UNKNOWN;
        }
    }

    public String getResultDatabaseItem(ResultType resultType) {
        switch (resultType) {
            case BOTTLE:
                return "bottleRecycleCount";
            case CAN:
                return "canRecycleCount";
            case BATTERY:
                return "batteryRecycleCount";
            case GLASS:
                return "glassRecycleCount";
            case PLASTIC:
                return "plasticRecycleCount";
            case UNKNOWN:
            default:
                return "othersRecycleCount";
        }
    }
}


