package com.hongguo.read.repertory.share;

import android.text.TextUtils;

import com.hongguo.common.utils.SpStaticUtil;
import com.hongguo.read.mvp.model.book.BookReaderBackground;
import com.hongguo.read.utils.AppUtils;
import com.losg.library.utils.DisplayUtil;
import com.losg.library.utils.JsonUtils;

/**
 * Created by losg on 2018/1/7.
 */

public class BookRepertory {

    public static final int DEFAULT_TEXT_SIZE = 15; //sp
    public static final int DEFAULT_MAX_SIZE  = 30; //sp
    public static final int DEFAULT_MIN_SIZE  = 12; //sp

    /**
     * 音量键翻页
     */
    public static void setVolumePage(boolean volumePage) {
        SpStaticUtil.putBoolean("volume_page", volumePage);
    }

    public static boolean getVolPage() {
        return SpStaticUtil.getBoolean("volume_page");
    }


    /**
     * 阅读页字体大小
     *
     * @param textSize
     */
    public static void setTextSize(int textSize) {
        SpStaticUtil.putInt("text_size", textSize);
    }

    public static int getTextSize() {
        return SpStaticUtil.getInt("text_size", DisplayUtil.dip2px(SpStaticUtil.getApplicationContext(), DEFAULT_TEXT_SIZE));
    }

    public static int getDefaultTextSize() {
        return DisplayUtil.dip2px(SpStaticUtil.getApplicationContext(), DEFAULT_TEXT_SIZE);
    }

    public static int getMaxTextSize() {
        return DisplayUtil.dip2px(SpStaticUtil.getApplicationContext(), DEFAULT_MAX_SIZE);
    }

    public static int getMinTextSize() {
        return DisplayUtil.dip2px(SpStaticUtil.getApplicationContext(), DEFAULT_MIN_SIZE);
    }

    /**
     * 阅读速度
     * @return
     */
    public static int getSpeed(){
        return SpStaticUtil.getInt("book_read_speed", 50);
    }

    public static void setSpeed(int speed){
        SpStaticUtil.putInt("book_read_speed", speed);
    }

    /**
     * 阅读人名称
     * @return
     */
    public static String getSpeakerName(){
        return SpStaticUtil.getString("book_speaker_name", "");
    }

    public static void setSpeakerName(String speakerName){
        SpStaticUtil.putString("book_speaker_name", speakerName);
    }


    /**
     * 阅读获取亮度
     *
     * @param
     */
    public static void setLight(int light) {
        SpStaticUtil.putInt("light", light);
    }

    public static int getLight() {
        return SpStaticUtil.getInt("light", AppUtils.getSystemLight(SpStaticUtil.getApplicationContext()));
    }

    /**
     * 阅读夜晚模式
     *
     */
    public static void setModeNeight(boolean modeNeight) {
        SpStaticUtil.putBoolean("mode_neight", modeNeight);
    }

    public static boolean getModeNeight() {
        return SpStaticUtil.getBoolean("mode_neight", false);
    }

    /**
     * 阅读页动画形式
     *
     * @param animType
     */
    public static void setAnimType(int animType) {
        SpStaticUtil.putInt("anim_type", animType);
    }

    public static int getAnimType() {
        return SpStaticUtil.getInt("anim_type", 0);
    }

    /**
     * 阅读页动背景
     *
     * @param background
     */
    public static void setReadBackground(String background) {
        SpStaticUtil.putString("read_background_1.0.0", background);
    }

    public static String getReadBackground() {
        String read_background = SpStaticUtil.getString("read_background_1.0.0");
        if (TextUtils.isEmpty(read_background)) {
            BookReaderBackground bookReaderBackground = new BookReaderBackground(0xfff7ecce, "ic_read_orange", 0xff000000);
            bookReaderBackground.index = 0;
            return JsonUtils.toJson(bookReaderBackground);
        }
        return read_background;
    }

    /**
     * 行距类型
     * @param lineType
     */
    public static void setLineType(int lineType) {
        SpStaticUtil.putInt("page_line_type", lineType);
    }

    public static int getLineType() {
        return SpStaticUtil.getInt("page_line_type", 0);
    }
}
