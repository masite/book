package com.hongguo.common.repertory.share;


import com.hongguo.common.utils.SpStaticUtil;

/**
 * Created time 2017/11/29.
 *
 * @author losg
 */

public class AppRepertory {


    /**
     * 用户首次打开书架
     *
     * @return
     */
    public static boolean issFirstOpenShelf() {
        return SpStaticUtil.getBoolean("first_open_shelf", true);
    }

    public static void setIsFirstOpenShelf(boolean open) {
        SpStaticUtil.putBoolean("first_open_shelf", open);
    }

    /**
     * 用户首次进入加载页面
     *
     * @param open
     */
    public static void setIsFirstLoading(boolean open) {
        SpStaticUtil.putBoolean("first_loading", open);
    }

    public static boolean issFirstLoading() {
        return SpStaticUtil.getBoolean("first_loading", true);
    }

    /**
     * 皮肤设置
     *
     * @param skinName
     */
    public static void setCurrentSkinName(String skinName) {
        SpStaticUtil.putString("skin_name", skinName);
    }

    public static String getCurrentSkinName() {
        return SpStaticUtil.getString("skin_name", "");
    }

    public static String getDefaultSkinName() {
        return SpStaticUtil.getString("default_skin_name", "");
    }

    public static void setDefaultSkinName(String name) {
        SpStaticUtil.putString("default_skin_name", name);
    }

    /**
     * 自动更新
     */
    public static void setAutoUpdate(boolean update) {
        SpStaticUtil.putBoolean("auto_update", update);
    }

    public static boolean getAutoUpdate() {
        return SpStaticUtil.getBoolean("auto_update");
    }

    /**
     * 夜间模式
     */
    public static void setNightMode(boolean night) {
        SpStaticUtil.putBoolean("night_mode", night);
    }

    public static boolean getNightMode() {
        return SpStaticUtil.getBoolean("night_mode");
    }

    /**
     * 活动版本
     */
    public static void setEventCode(int eventCode) {
        SpStaticUtil.putInt("event_code", eventCode);
    }

    public static int getEventCode() {
        return SpStaticUtil.getInt("event_code", -1);
    }

    /**
     * svip活动点击
     *
     * @param isClick
     */
    public static void setVipEventClick(boolean isClick) {
        SpStaticUtil.putBoolean("vip_event_click", isClick);
    }

    public static boolean isVipEventClick() {
        return SpStaticUtil.getBoolean("vip_event_click", false);
    }
}
