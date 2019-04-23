package com.hongguo.common.utils;

import android.content.Context;

import com.hongguo.common.widget.skin.SkinConfig;
import com.hongguo.common.widget.skin.loader.SkinManager;

/**
 * Created by losg on 2018/3/5.
 */

public class SkinChangeUtils {

    public static void init(Context context) {
        SkinManager.getInstance().init(context);
        SkinManager.getInstance().loadSkin(null);
        SkinConfig.setCanChangeStatusColor(true);
        SkinConfig.setCanChangeFont(true);
        SkinConfig.setDebug(true);
        SkinConfig.enableGlobalSkinApply();
    }
}
