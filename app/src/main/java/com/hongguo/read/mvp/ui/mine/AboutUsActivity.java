package com.hongguo.read.mvp.ui.mine;

import android.content.Context;
import android.content.Intent;

import com.hongguo.common.utils.webview.AppWebView;


/**
 * Created by losg on 2018/1/7.
 */

public class AboutUsActivity extends AppWebView {

    public static void toActivity(Context context) {
        Intent intent = new Intent(context, AboutUsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        super.initView();
        setTitle("关于我们");
        mAppWebview.loadUrl("http://x1.hgread.com/guanyu/index.html");
    }

}
