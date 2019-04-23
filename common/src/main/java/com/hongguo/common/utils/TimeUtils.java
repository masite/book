package com.hongguo.common.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by losg on 2017/12/20.
 */

public class TimeUtils {

    public static String parseTime(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    public static String parseCTime2Day(String time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String cSharpTime = getCSharpTime(time);
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(Long.parseLong(cSharpTime));
        return simpleDateFormat.format(instance.getTime());
    }

    public static String parseNoMinuteTime(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
        return simpleDateFormat.format(date);
    }

    ///Date(1495550307000)/
    public static String getCSharpTime(String time) {
        Pattern compile = Pattern.compile("(\\d+)");
        Matcher matcher = compile.matcher(time);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    public static String getTime(String ctime) {
        String cSharpTime = getCSharpTime(ctime);
        if (TextUtils.isEmpty(cSharpTime)) return "";
        long time = Long.parseLong(cSharpTime) / 1000;
        long currentTime = System.currentTimeMillis() / 1000;
        if (time > currentTime) {
            return "刚刚";
        }
        int dTime = (int) (currentTime - time);
        if (dTime < 60) {
            //小于一分钟
            return "刚刚";
        } else if (dTime < 60 * 60) {
            //小于一小时
            return dTime / (60) + "分钟前";
        } else if (dTime < 60 * 60 * 24) {
            //小于一天
            return dTime / (60 * 60) + "小时前";
        } else if (dTime < 60 * 60 * 24 * 30) {
            //小于30天
            return dTime / (60 * 60 * 24) + "天前";
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(time * 1000);
            return parseNoMinuteTime(calendar.getTime());
        }
    }

    public static String getTime(String ctime, boolean isCSharpTime) {
        String cSharpTime = "";
        if (isCSharpTime) {
            cSharpTime = getCSharpTime(ctime);
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date parse = simpleDateFormat.parse(ctime);
                cSharpTime = parse.getTime() + "";
            } catch (ParseException e) {
                cSharpTime = "";
                e.printStackTrace();
            }
        }
        if (TextUtils.isEmpty(cSharpTime)) return "";
        long time = Long.parseLong(cSharpTime) / 1000;
        long currentTime = System.currentTimeMillis() / 1000;
        if (time > currentTime) {
            return "刚刚";
        }
        int dTime = (int) (currentTime - time);
        if (dTime < 60) {
            //小于一分钟
            return "刚刚";
        } else if (dTime < 60 * 60) {
            //小于一小时
            return dTime / (60) + "分钟前";
        } else if (dTime < 60 * 60 * 24) {
            //小于一天
            return dTime / (60 * 60) + "小时前";
        } else if (dTime < 60 * 60 * 24 * 30) {
            //小于30天
            return dTime / (60 * 60 * 24) + "天前";
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(time * 1000);
            return parseNoMinuteTime(calendar.getTime());
        }
    }
}
