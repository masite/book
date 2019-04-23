package com.hongguo.common.utils;

import android.util.Log;


/**
 * 打印log 类 (debug 版本才打印log日志)
 *
 * @author losg
 */
public class LogUtils {

    public static boolean sIsBug = true;

    public static void v(String tag, String msg) {
        if (sIsBug) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (sIsBug) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (sIsBug) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (sIsBug) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (sIsBug) {
            Log.e(tag, msg);
        }
    }

    public static void log(Object o, String msg) {
        if (sIsBug) {
            Log.e(o.getClass().getSimpleName(), msg);
        }
    }

    public static void log(String msg) {
        if (sIsBug) {
            Log.e("losg_log", msg);
        }
    }

}
