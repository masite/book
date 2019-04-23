package com.hongguo.read.widget.online;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuItem;

import com.hongguo.common.widget.X5WebView;
import com.hongguo.read.R;
import com.hongguo.read.base.ActivityEx;
import com.hongguo.read.repertory.share.UserRepertory;
import com.hongguo.read.widget.loading.LoadingHelper;
import com.losg.library.widget.loading.BaLoadingView;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import butterknife.BindView;

/**
 * Created by losg on 2018/5/25.
 */

public class OnlineActivity extends ActivityEx {

    @BindView(R.id.app_webview)
    X5WebView mAppWebview;
    private LoadingHelper mLoadingHelper;
    private WebOpenFileManager mWebChromeClient;

    public static void toActivity(Context context) {
        Intent intent = new Intent(context, OnlineActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_online;
    }

    @Override
    protected void initView() {
        super.initView();
        setTitle("在线客服");
        mLoadingHelper = new LoadingHelper(mContext);
        bindLoadingView(mLoadingHelper, mAppWebview, 0);
        changeLoadingStatus(BaLoadingView.LoadingStatus.LOADING, 0);

        WebSettings settings = mAppWebview.getSettings();
        settings.setJavaScriptEnabled(true);
        mAppWebview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView webView, String s) {
                super.onPageFinished(webView, s);
                changeLoadingStatus(BaLoadingView.LoadingStatus.LADING_SUCCESS, 0);
            }
        });
        mWebChromeClient = new WebOpenFileManager(mContext);
        mAppWebview.setWebChromeClient(mWebChromeClient);
        mAppWebview.loadUrl("http://free.weikefu.net/AppKeFu/weikefu/wap/chat.php?wg=hgread&robot=true&hidenav=true&history=true&postscript=" + UserRepertory.getUserID());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mWebChromeClient.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem contactQQ = menu.add(0, 0, 0, "联系QQ");
        contactQQ.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == 0){
            openQQ();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openQQ() {
        if (checkApkExist(this, "com.tencent.mobileqq")){
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=1373073715&version=1")));
        }else{
            toastMessage("本机未安装QQ应用");
        }
    }

    public boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName,
                    PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
