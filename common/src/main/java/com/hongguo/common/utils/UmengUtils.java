package com.hongguo.common.utils;

import android.content.Context;

import com.hongguo.read.base.BuildConfig;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

/**
 * Created by losg on 2018/3/5.
 */

public class UmengUtils {

    private static final String WEIXIN_APP_KEY    = "wx3b51546ee61cceca";
    private static final String WEIXIN_APP_SECRET = "249952c31b7ad1688066a54c1411b5f5";
    private static final String QQ_APP_KEY        = "101394352";
    private static final String QQ_APP_SECRET     = "41e27957203a52bc67a1973e94efaa79";

    public static void initUmeng(Context context) {
        // umeng 初始化
        PlatformConfig.setWeixin(WEIXIN_APP_KEY, WEIXIN_APP_SECRET);
        PlatformConfig.setQQZone(QQ_APP_KEY, QQ_APP_SECRET);
        if (BuildConfig.DEBUG) {
            Config.DEBUG = true;
        }
        UMShareAPI.get(context);
    }
}
