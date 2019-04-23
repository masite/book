package com.hongguo.common.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

public class X5WebView extends WebView {

    public X5WebView(Context context) {
        this(context, null);
    }

    public X5WebView(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
        initWebViewSettings();
    }

    private void initWebViewSettings() {
        WebSettings webSetting = this.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
    }

}
