package com.hongguo.common.utils;

/**
 * Created by losg on 2018/1/9.
 */

public class MathTypeParseUtils {

    public static int string2Int(String number){
        return string2Int(number, 0);
    }

    public static int string2Int(String number, int defaultValue) {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    public static float string2Float(String number){
        return string2Float(number, 0);
    }

    public static float string2Float(String number,float defaultValue) {
        try {
            return Float.parseFloat(number);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    public static double string2Double(String number){
        return string2Double(number, 0);
    }

    public static double string2Double(String number, double defaultValue) {
        try {
            return Double.parseDouble(number);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }
}
