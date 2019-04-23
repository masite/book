package com.hongguo.read.utils;

/**
 * Created by losg
 */

public class BookNumberUtils {

    /**
     * @param words
     * @return
     */
    public static String formartWords(int words) {
        String result = "";
        if (words >= 10000) {
            double words2 = (double) words / 10000d;
            result = String.format("%.1f", words2) + "万字";
        } else {
            result = words + "字";
        }
        return result;
    }

    public static String formartUserReads(int reads) {
        String result = "";
        if (reads >= 10000) {
            double words2 = (double) reads / 10000d;
            result = String.format("%.1f", words2) + "万";
        } else {
            result = reads + "";
        }
        return result;
    }
}
