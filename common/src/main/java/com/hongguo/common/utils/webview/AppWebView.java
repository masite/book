package com.hongguo.common.utils.webview;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.hongguo.common.base.BaseActivity;
import com.hongguo.common.router.AppRouter;
import com.hongguo.common.router.NavigationCallbackImp;
import com.hongguo.common.utils.UmengShareHelper;
import com.hongguo.common.widget.X5WebView;
import com.hongguo.read.base.R;
import com.lbsw.stat.LBStat;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import butterknife.ButterKnife;

/**
 * Created time 2017/12/7.
 *
 * @author losg
 */

@Route(path = AppRouter.WEB_DIRECT)
public class AppWebView extends BaseActivity {

    private static final String INTENT_URL      = "url";
    private static final String APP_ROUTER_NAME = "hgread";
    private static final String APP_ROUTER_URL  = "hgread://www.hgread.com";
    private static final String APP_HOME_URL    = "http://m.hgread.com";

    public X5WebView mAppWebview;

    @Autowired(name = "url")
    String mUrl;

    private UmengShareHelper mUmengShareHelper;

    public static void toActivity(String url) {
        AppRouter.createBuilder(AppRouter.WEB_DIRECT).putString(INTENT_URL, url).build();
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_app_webview;
    }



    @Override
    protected void initView() {
        ARouter.getInstance().inject(this);
        mAppWebview = ButterKnife.findById(this, R.id.app_webview);

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
        if (TextUtils.isEmpty(mUrl)) return;
        nativeAppCheck(mUrl);
    }


    private void dealUrlClient() {
        mAppWebview.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
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

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private class HgReadWebInteract {
        @JavascriptInterface
        public void share(String title, String describe, String directUrl, String imageUrl) {
            mUmengShareHelper.showShare(title, directUrl, describe, imageUrl);
            LBStat.collect("点击", "网页分享" + directUrl);
        }
    }

    protected void nativeAppCheck(String url) {
        if (url.startsWith(APP_ROUTER_URL)) {
            try {
                AppRouter.navigation(AppWebView.this, Uri.parse(url), new NavigationCallbackImp() {
                    @Override
                    public void onLost(Postcard postcard) {
                        mAppWebview.loadUrl(APP_HOME_URL);
                    }

                    @Override
                    public void onArrival(Postcard postcard) {
                        if (!TextUtils.isEmpty(mUrl) && mUrl.contains(APP_ROUTER_URL)) {
                            finish();
                        }
                    }
                });
            } catch (Exception e) {
                mAppWebview.loadUrl(APP_HOME_URL);
            }
            return;
        }
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
    }

    @Override
    protected boolean activityAddSwipBackLayout() {
        return true;
    }
}
