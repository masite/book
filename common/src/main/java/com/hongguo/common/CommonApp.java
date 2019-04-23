package com.hongguo.common;

import android.app.Application;
import android.util.Log;

import com.hongguo.common.utils.SkinChangeUtils;
import com.hongguo.common.utils.UmengUtils;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsDownloader;
import com.tencent.smtt.sdk.TbsListener;


/**
 * Created by losg on 2017/7/18.
 */

public class CommonApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initUmeng();
        initSkin();
    }

    /**
     * 友盟SDK初始化信息
     */
    private void initUmeng() {
        UmengUtils.initUmeng(this);
    }

    /**
     * 初始化皮肤信息
     */
    private void initSkin() {
        SkinChangeUtils.init(this);
    }


    public void initX5WebView(){
        //腾讯X5浏览器
        TbsDownloader.needDownload(getApplicationContext(), false);
        QbSdk.preInit(this);
        QbSdk.setTbsListener(new TbsListener() {
            @Override
            public void onDownloadFinish(int i) {
                Log.e("TBS", "下载完成" + i);
            }

            @Override
            public void onInstallFinish(int i) {
                Log.e("TBS", "安装完成" + i);
            }

            @Override
            public void onDownloadProgress(int i) {
                Log.e("TBS", "下载进度" + i);
            }
        });

    }
}
