package com.hongguo.read.widget.online;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.hongguo.read.base.ActivityEx;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;

import java.io.File;

/**
 * Created by losg on 2018/5/25.
 */

public class WebOpenFileManager extends WebChromeClient {

    private static final int OPEN_CHOOSE_FILER   = 100;
    private static final int OPEN_CHOOSE_FILER_5 = 101;

    private Context mContext;

    private ValueCallback<Uri>   mUriValueCallback;
    private ValueCallback<Uri[]> mUriValueCallback5;

    public WebOpenFileManager(Context context) {
        mContext = context;
    }

    @Override
    public void openFileChooser(ValueCallback<Uri> valueCallback, String s, String s1) {
        super.openFileChooser(valueCallback, s, s1);
        mUriValueCallback = valueCallback;
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        ((Activity) mContext).startActivityForResult(intent, OPEN_CHOOSE_FILER);
    }

    /**
     * 5.0 以上 回调
     *
     * @param webView
     * @param valueCallback
     * @param fileChooserParams
     * @return
     */
    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, FileChooserParams fileChooserParams) {
        mUriValueCallback5 = valueCallback;
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        ((Activity) mContext).startActivityForResult(intent, OPEN_CHOOSE_FILER_5);
        return true;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (intent == null || intent.getData() == null || resultCode != Activity.RESULT_OK) {
            setDefaultChooseResult(requestCode);
            return;
        }
        Uri data = intent.getData();
        if (!data.getScheme().toLowerCase().contains("content")) {
            setDefaultChooseResult(requestCode);
            return;
        }
        try {
            Uri selectedImage = intent.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = mContext.getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String imagePath = c.getString(columnIndex);
            if (TextUtils.isEmpty(imagePath)) {
                setDefaultChooseResult(requestCode);
                return;
            }
            if (requestCode == OPEN_CHOOSE_FILER) {
                mUriValueCallback.onReceiveValue(Uri.fromFile(new File(imagePath)));
            } else {
                mUriValueCallback5.onReceiveValue(new Uri[]{Uri.fromFile(new File(imagePath))});
            }
        } catch (Exception e) {
            setDefaultChooseResult(requestCode);
        }
    }

    private void setDefaultChooseResult(int requestCode) {
        if (requestCode == OPEN_CHOOSE_FILER) {
            mUriValueCallback.onReceiveValue(null);
        } else {
            mUriValueCallback5.onReceiveValue(null);
        }
    }

    @Override
    public boolean onJsAlert(WebView webView, String s, String s1, JsResult jsResult) {
        ((ActivityEx)mContext).showMessDialog("提醒", s1, "关闭", "确定", messageInfoDialog -> {
            messageInfoDialog.dismiss();
            jsResult.confirm();
        }, messageInfoDialog -> {
            messageInfoDialog.dismiss();
            jsResult.cancel();
        });
        return true;
    }
}
