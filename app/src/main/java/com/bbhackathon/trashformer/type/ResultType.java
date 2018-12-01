package com.bbhackathon.trashformer.type;

public enum ResultType {

    BOTTLE(1, "寶特瓶"),

    GLASS(2, "玻璃"),

    CAN(3, "鐵鋁罐"),

    BATTERY(4, "電池"),

    UNKNOWN(99, "未知");

    private int code;

    private String memo;

    ResultType(int code, String memo){
        this.code = code;
        this.memo = memo;
    }

    public int getCode() {
        return code;
    }

    public String getMemo() {
        return memo;
    }

    public boolean isBottle(){
        return equals(ResultType.BOTTLE);
    }

    public boolean isGlass(){
        return equals(ResultType.GLASS);
    }

    public boolean isCan(){
        return equals(ResultType.CAN);
    }

    public boolean isBattery(){
        return equals(ResultType.BATTERY);
    }

    public boolean isUnknow(){
        return equals(ResultType.UNKNOWN);
    }
}


