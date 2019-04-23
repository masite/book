package com.hongguo.read.mvp.ui.mine.recharge;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

import com.hongguo.common.utils.LogUtils;
import com.hongguo.common.widget.X5WebView;
import com.hongguo.read.constants.Constants;
import com.hongguo.read.eventbus.PayCheckEvent;
import com.hongguo.read.utils.AppUtils;
import com.losg.library.base.BaseView;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2017/1/16.
 */

public class PayUtils {

    private X5WebView mWebView;
    private String    mPayType;
    private String    mPayUrl;
    private Context   mContext;
    private String    mOwer;
    private boolean   mPaying;

    public static PayUtils startToPay(Context context, String url, String payMothod, Class clazz) {
        PayUtils payUtils = new PayUtils(context, url, payMothod, clazz);
        payUtils.initView();
        return payUtils;
    }

    public PayUtils(Context context, String url, String payMothod, Class clazz) {
        mContext = context;
        mOwer = clazz.getSimpleName();
        mPayUrl = url;
        mPayType = payMothod;
        mPaying = true;
    }

    protected void initView() {
        if (TextUtils.isEmpty(mPayType)) {
            mPayType = Constants.PAY_TYPE.WEI_XIN;
        }
        initWebview();
        ((BaseView) mContext).dismissWaitDialogWithoutAnim();
        ((BaseView) mContext).showWaitDialog(true, "支付进行中", null);
        if (!checkPayUrl(mPayUrl))
            mWebView.loadUrl(mPayUrl);
    }

    private void initWebview() {

        mWebView = new X5WebView(mContext);
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                return checkPayUrl(s);
            }

            @Override
            public void onReceivedError(WebView webView, int i, String s, String s1) {
                super.onReceivedError(webView, i, s, s1);
                Toast.makeText(mContext, "发生未知错误,请稍后再试", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
                super.onReceivedError(webView, webResourceRequest, webResourceError);
                Toast.makeText(mContext, "发生未知错误,请稍后再试", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
                super.onReceivedSslError(webView, sslErrorHandler, sslError);
                sslErrorHandler.proceed();
            }

        });
    }

    private boolean checkPayUrl(String url) {

        if (url.contains("wechatqrcode")) {
            EventBus.getDefault().post(new PayCheckEvent("发生未知错误,请稍后再试", mOwer));
            destroy();
            return true;
        }

        if (url.startsWith("weixin://") || url.startsWith("alipays://")) {
            try {
                if (url.startsWith("weixin://")) {
                    boolean wxAppInstalledAndSupported = AppUtils.isWxAppInstalledAndSupported(mContext);
                    if (!wxAppInstalledAndSupported) {
                        EventBus.getDefault().post(new PayCheckEvent("尚未安装微信，请先安装微信后再试", mOwer));
                        destroy();
                        return true;
                    }
                } else {
                    LogUtils.log(url);
                    if (!AppUtils.checkAliPayInstalled(mContext)) {
                        EventBus.getDefault().post(new PayCheckEvent("尚未安装支付宝，请先安装支付宝后再试", mOwer));
                        destroy();
                        return true;
                    }
                }
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                mContext.startActivity(intent);
                EventBus.getDefault().post(new PayCheckEvent("", mOwer));
                destroy();

            } catch (Exception e) {
                if (mPayType.equals(Constants.PAY_TYPE.WEI_XIN)) {
                    EventBus.getDefault().post(new PayCheckEvent("尚未安装微信，请先安装微信后再试", mOwer));
                    destroy();
                } else if (mPayType.equals(Constants.PAY_TYPE.ALIPAY)) {
                    EventBus.getDefault().post(new PayCheckEvent("尚未安装支付宝，请先安装支付宝后再试", mOwer));
                    destroy();
                } else {
                    EventBus.getDefault().post(new PayCheckEvent("调起应用失败", mOwer));
                    destroy();
                }
            }
            return true;
        }
        return false;
    }

    public boolean onBackPress() {
        if (mPaying) {
            mPaying = false;
            ((BaseView) mContext).dismissWaitDialogWithoutAnim();
            destroy();
            return true;
        }
        return false;
    }

    public void destroy() {
        mPaying = false;
        if (mWebView != null) {
            ((BaseView) mContext).dismissWaitDialogWithoutAnim();
            mWebView.stopLoading();
            mWebView.destroy();
            mWebView = null;
        }
    }

}
