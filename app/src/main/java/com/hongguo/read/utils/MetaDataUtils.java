package com.hongguo.read.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

/**
 * 通过清单文件 获取 meta 标签的内容
 * Created by losg on 2017/7/21.
 */

public class MetaDataUtils {

    public static String getMeta(Context context, String meta) {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return applicationInfo.metaData.get(meta) + "";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }
}
