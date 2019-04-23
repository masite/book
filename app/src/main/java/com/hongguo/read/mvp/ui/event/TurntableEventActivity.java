package com.hongguo.read.mvp.ui.event;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.hongguo.common.router.AppRouter;
import com.hongguo.common.router.NavigationCallbackImp;
import com.hongguo.common.utils.StatisticsUtils;
import com.hongguo.common.utils.UmengShareHelper;
import com.hongguo.common.widget.X5WebView;
import com.hongguo.read.R;
import com.hongguo.read.base.ActivityEx;
import com.hongguo.read.constants.Constants;
import com.hongguo.read.mvp.ui.mine.recharge.PayUtils;
import com.hongguo.read.widget.loading.LoadingHelper;
import com.losg.library.widget.loading.BaLoadingView;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/2/13.
 */

@Route(path = AppRouter.TRRN_TABLE_EVENT)
public class TurntableEventActivity extends ActivityEx {

    private static final String APP_ROUTER_NAME = "hgread";
    private static final String APP_ROUTER_URL  = "hgread://www.hgread.com";
    private static final String APP_HOME_URL    = "http://m.hgread.com";

    @BindView(R.id.app_webview)
    X5WebView mAppWebview;

    private UmengShareHelper mUmengShareHelper;

    private String mUrl        = "http://m.hgread.com/dist/turntable?utm_protocol=hgread";
    private String mCurrentUrl = "";
    private LoadingHelper mLoadingHelper;
    private PayUtils      mPayUtils;

    public static void toActivity(Context context) {
        Intent intent = new Intent(context, TurntableEventActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_turntable_webview;
    }

    @Override
    protected void initView() {
        ARouter.getInstance().inject(this);
        mUmengShareHelper = new UmengShareHelper(mContext);
        setTitle("正在加载");
        mAppWebview.addJavascriptInterface(new HgReadWebInteract(), APP_ROUTER_NAME);
        mAppWebview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView webView, String s) {
                super.onReceivedTitle(webView, s);
                setTitle(s);
            }
        });
        dealUrlClient();

        mLoadingHelper = new LoadingHelper(mContext, mAppWebview);
        mLoadingHelper.setStatus(BaLoadingView.LoadingStatus.LOADING);

        mCurrentUrl = mUrl;
        mAppWebview.loadUrl(mUrl);
    }


    private void dealUrlClient() {
        mAppWebview.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                if (url.contains("?")) {
                    url += "&utm_protocol=hgread";
                } else {
                    url += "?utm_protocol=hgread";
                }
                if (url.contains("/TradeCreate")) {
                    if (mPayUtils != null) {
                        mPayUtils.destroy();
                    }
                    mPayUtils = PayUtils.startToPay(mContext, url, Constants.PAY_TYPE.UNKNOW, TurntableEventActivity.class);
                    return true;
                }
                nativeAppCheck(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView webView, String s) {
                super.onPageFinished(webView, s);
                pageFinish();
            }
        });
    }

    protected void pageFinish() {
        mLoadingHelper.setStatus(BaLoadingView.LoadingStatus.LADING_SUCCESS);
    }


    private class HgReadWebInteract {
        @JavascriptInterface
        public void share(String title, String describe, String directUrl, String imageUrl) {
            mUmengShareHelper.showShare(title, directUrl, describe, imageUrl);
            StatisticsUtils.collect("点击", "网页分享" + directUrl);
        }
    }

    protected void nativeAppCheck(String url) {
        if (url.startsWith(APP_ROUTER_URL)) {
            if (url.contains("hgread://www.hgread.com/s-vip")) {
                url = "hgread://www.hgread.com/vip/svip";
            }
            try {
                AppRouter.navigation(TurntableEventActivity.this, Uri.parse(url), new NavigationCallbackImp() {
                    @Override
                    public void onLost(Postcard postcard) {
                        mAppWebview.loadUrl(APP_HOME_URL);
                    }
                });
            } catch (Exception e) {
                mAppWebview.loadUrl(APP_HOME_URL);
            }
            return;
        }
        mCurrentUrl = url;
        mAppWebview.loadUrl(url);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mUmengShareHelper.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mUmengShareHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        mAppWebview.destroy();
        super.onDestroy();
        mUmengShareHelper.onDestroy();
        if (mPayUtils != null) {
            mPayUtils.destroy();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem refresh = menu.add(0, 0, 0, "刷新");
        refresh.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 0) {
            mAppWebview.loadUrl(mCurrentUrl);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mPayUtils != null && mPayUtils.onBackPress()) {
            return;
        }
        if (mAppWebview.canGoBack() && !mCurrentUrl.contains("http://m.hgread.com/dist/turntable")) {
            mAppWebview.goBack();
        } else {
            finish();
        }
    }


}
