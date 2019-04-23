package com.hongguo.common.utils;


import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * SharedPreferences 工具类 引用全局 context
 */
public class SpStaticUtil {

    private static SharedPreferences        sp;
    private static SharedPreferences.Editor editor;
    private static Context                  sContext;
    private static final String SHARE_NAME = "hongguo_pre";

    /***
     * @param context 上下文
     */
    public static void init(Context context) {
        sContext = context;
        sp = context.getApplicationContext().getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.commit();
    }


    public static Context getApplicationContext() {
        return sContext;
    }

    /**
     * SP中写入String类型value
     *
     * @param key   键
     * @param value 值
     */
    public static void putString(String key, String value) {
        editor.putString(key, value).commit();
    }

    /**
     * SP中读取String
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值{@code null}
     */
    public static String getString(String key) {
        return getString(key, null);
    }


    /**
     * SP中读取String
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public static String getString(String key, String defaultValue) {

        return sp.getString(key, defaultValue);
    }

    /**
     * SP中写入int类型value
     *
     * @param key   键
     * @param value 值
     */
    public static void putInt(String key, int value) {
        editor.putInt(key, value).commit();
    }

    /**
     * SP中读取int
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值-a
     */
    public static int getInt(String key) {
        return getInt(key, -1);
    }

    /**
     * SP中读取int
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public static int getInt(String key, int defaultValue) {
        return sp.getInt(key, defaultValue);
    }

    /**
     * SP中写入long类型value
     *
     * @param key   键
     * @param value 值
     */
    public static void putLong(String key, long value) {
        editor.putLong(key, value).commit();
    }

    /**
     * SP中读取long
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值-a
     */
    public static long getLong(String key) {
        return getLong(key, -1L);
    }

    /**
     * SP中读取long
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public static long getLong(String key, long defaultValue) {
        return sp.getLong(key, defaultValue);
    }

    /**
     * SP中写入float类型value
     *
     * @param key   键
     * @param value 值
     */
    public static void putFloat(String key, float value) {
        editor.putFloat(key, value).commit();
    }

    /**
     * SP中读取float
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值-a
     */
    public static float getFloat(String key) {
        return getFloat(key, -1f);
    }

    /**
     * SP中读取float
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public static float getFloat(String key, float defaultValue) {
        return sp.getFloat(key, defaultValue);
    }

    /**
     * SP中写入boolean类型value
     *
     * @param key   键
     * @param value 值
     */
    public static void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value).commit();
    }

    /**
     * SP中读取boolean
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值{@code false}
     */
    public static boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    /**
     * SP中读取boolean
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public static boolean getBoolean(String key, boolean defaultValue) {
        return sp.getBoolean(key, defaultValue);
    }

    /**
     * 获取SP中所有键值对
     *
     * @return Map对象
     */
    public static Map<String, ?> getAll() {
        return sp.getAll();
    }

    /**
     * 从SP中移除该key
     *
     * @param key 键
     */
    public static void remove(String key) {
        editor.remove(key).commit();
    }

    /**
     * 判断SP中是否存在该key
     *
     * @param key 键
     * @return {@code true}: 存在<br>{@code false}: 不存在
     */
    public static boolean contains(String key) {
        return sp.contains(key);
    }

    /**
     * 清除SP中所有数据
     */
    public static void clear() {
        editor.clear().commit();
    }

}
