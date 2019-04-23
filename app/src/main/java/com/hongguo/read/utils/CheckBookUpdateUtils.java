package com.hongguo.read.utils;

import android.text.TextUtils;

import com.hongguo.common.utils.TimeUtils;

/**
 * Created by losg on 2018/2/14.
 */

public class CheckBookUpdateUtils {

    public static boolean bookHasUpdate(String netLastUpdateTime, String lastUpdateTime) {
        if (TextUtils.isEmpty(netLastUpdateTime) || TextUtils.isEmpty(lastUpdateTime)) {
            return true;
        }
        if(netLastUpdateTime.startsWith("/Dat")){
            netLastUpdateTime = TimeUtils.getCSharpTime(netLastUpdateTime);
        }
        if(lastUpdateTime.startsWith("/Dat")){
            lastUpdateTime = TimeUtils.getCSharpTime(lastUpdateTime);
        }
        return lastUpdateTime.compareTo(netLastUpdateTime) < 0;
    }

}
