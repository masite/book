package com.hongguo.read.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.widget.TextView;

import com.hongguo.common.base.BaseActivity;
import com.hongguo.read.BaApp;
import com.hongguo.read.R;
import com.hongguo.read.constants.FileManager;
import com.hongguo.read.utils.update.DownService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DownXunFeiDialog extends Dialog {

    @BindView(R.id.down_xun_fei)
    TextView mDownXunFei;

    private Unbinder mBind;
    private String   mDownUrl;
    private Context  mContext;
    private XunFeiDownSuccess mXunFeiDownSuccess;

    public DownXunFeiDialog(@NonNull Context context) {
        super(context, R.style.FullWidthDialog);
        mContext = context;
        getWindow().getAttributes().gravity = Gravity.BOTTOM;
        setContentView(R.layout.dialog_down_feixun);
        mBind = ButterKnife.bind(this);
    }

    public void setDownUrl(String downUrl) {
        mDownUrl = downUrl;
    }

    @OnClick(R.id.down_xun_fei)
    public void downXunFei() {
        mDownXunFei.setEnabled(false);
        mDownXunFei.setText("准备下载，请稍候");
        ((BaApp) ((BaseActivity) mContext).findApp()).getDownService().addTask(mDownUrl,
                FileManager.getTempPath("xunfei"), FileManager.getApkDownPath("xunfei.apk"),
                mDownLoadListener);
    }

    private DownService.DownLoadListener mDownLoadListener = new DownService.DownLoadListener() {

        @Override
        public void currentProgress(int currentSize, int maxSize, int progress) {
            mDownXunFei.setText("当前进度 " + progress + " %");
        }

        @Override
        public void downError(String errorMessage) {
            mDownXunFei.setEnabled(true);
        }

        @Override
        public void success(String fileUrl, String savePath) {
            dismiss();
            mDownXunFei.setEnabled(true);
            mDownXunFei.setText("下载语音插件");
            mXunFeiDownSuccess.xunFeiDownSuccess(savePath);
        }
    };


    public void destory() {
        mBind.unbind();
        if (mDownLoadListener != null) {
            ((BaApp) ((BaseActivity) mContext).findApp()).getDownService().removeDownListener(mDownLoadListener);
        }
    }


    public void setXunFeiDownSuccess(XunFeiDownSuccess xunFeiDownSuccess) {
        mXunFeiDownSuccess = xunFeiDownSuccess;
    }

    public interface XunFeiDownSuccess{
        void xunFeiDownSuccess(String path);
    }
}
