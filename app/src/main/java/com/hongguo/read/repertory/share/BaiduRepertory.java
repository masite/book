package com.hongguo.read.repertory.share;


import android.text.TextUtils;

import com.hongguo.common.utils.SpStaticUtil;
import com.losg.library.utils.MD5;

import java.util.UUID;

/**
 * Created time 2017/11/29.
 *
 * @author losg
 */

public class BaiduRepertory {


    /**
     * 百度初始化是否成功
     * @param success
     */
    public static void baiduInit(boolean success) {
        SpStaticUtil.putBoolean("baiduInit", success);
    }

    public static boolean getBaiduInitStatus() {
        return SpStaticUtil.getBoolean("baiduInit");
    }


    /**
     * 百度绑定的用户的信息
     *
     * @return
     */
    public static String getBaiduUserInfo() {
        return SpStaticUtil.getString("baidu_info");
    }

    public static void setBaiduUserInfo(String userinfo) {
        SpStaticUtil.putString("baidu_info", userinfo);
    }

    public static String getBaiduRandom(){
        String baidu_refresh = SpStaticUtil.getString("baidu_refresh");
        if(TextUtils.isEmpty(baidu_refresh)){
            baidu_refresh = MD5.md5(UUID.randomUUID().toString() + System.currentTimeMillis()).toLowerCase();
            SpStaticUtil.putString("baidu_refresh", baidu_refresh);
        }
        return baidu_refresh;
    }

    /**
     * 百度下订单需要的额外信息
     *
     * @return
     */
    public static String getBaiduPayEXTRA() {
        return SpStaticUtil.getString("extra_info", "");
    }

    public static void setBaiduPayEXTRA(String extra) {
        SpStaticUtil.putString("extra_info", extra);
    }

}
