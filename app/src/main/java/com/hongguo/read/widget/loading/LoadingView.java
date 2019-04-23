package com.hongguo.read.widget.loading;


import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hongguo.read.R;
import com.losg.library.widget.loading.BaLoadingView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoadingView extends BaLoadingView {

    @BindView(R.id.describe)
    TextView  mDescribe;
    @BindView(R.id.loading_progress)
    ImageView mProgress;
    @BindView(R.id.result_null)
    ImageView mResultNull;
    @BindView(R.id.try_again)
    TextView  mTryAgain;

    private String                   mResultNullDescribe;
    private LoadingViewClickListener mLoadingViewClickListener;
    private LoadingStatus            mLoadingStatus;
    private String                   mTryDescribe;

    private int     mNullImageResource = 0;
    private boolean mNullBtnVisible    = true;
    private AnimationDrawable mLoadingAnimaiton;

    public LoadingView(Context context) {
        super(context);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int initViewResource() {
        return R.layout.view_loading;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.bind(this, view);
        mTryAgain.getLayoutParams().width = getResources().getDisplayMetrics().widthPixels / 2;
        mLoadingAnimaiton = (AnimationDrawable) mProgress.getBackground();
    }

    @OnClick(R.id.loading_content_view)
    void tryAgain() {
        if (mLoadingViewClickListener != null) {
            mLoadingViewClickListener.loadingClick(mLoadingStatus);
        }
    }

    @Override
    protected void connectTimeout() {
        mLoadingAnimaiton.stop();
        mDescribe.setText("访问服务器超时");
        mProgress.setVisibility(GONE);
        mResultNull.setVisibility(VISIBLE);
        mResultNull.setImageResource(R.mipmap.ic_error_page);
        mTryAgain.setText("加载失败，点击重试");
        mTryAgain.setVisibility(VISIBLE);
    }

    @Override
    protected void loadding() {
        mLoadingAnimaiton.start();
        mDescribe.setText("努力加载中");
        mProgress.setVisibility(VISIBLE);
        mResultNull.setVisibility(GONE);
        mTryAgain.setVisibility(GONE);
    }

    @Override
    protected void networkError() {
        mLoadingAnimaiton.stop();
        mDescribe.setText("网络连接失败");
        mProgress.setVisibility(GONE);
        mResultNull.setVisibility(VISIBLE);
        mResultNull.setImageResource(R.mipmap.ic_error_page);
        mTryAgain.setText("加载失败，点击重试");
        mTryAgain.setVisibility(VISIBLE);
    }

    @Override
    protected void resultNull() {
        mLoadingAnimaiton.stop();
        mDescribe.setText(mResultNullDescribe);
        mProgress.setVisibility(GONE);
        mResultNull.setVisibility(VISIBLE);
        if (mNullImageResource == 0) {
            mResultNull.setImageResource(R.mipmap.ic_loading_null);
        } else {
            mResultNull.setImageResource(mNullImageResource);
        }
        if (mNullBtnVisible) {
            mTryAgain.setVisibility(VISIBLE);
            mTryAgain.setText(mTryDescribe);
        } else {
            mTryAgain.setVisibility(GONE);
        }
    }

    @Override
    protected void serviceError() {
        mLoadingAnimaiton.stop();
        mDescribe.setText("连接服务器异常");
        mProgress.setVisibility(GONE);
        mResultNull.setVisibility(VISIBLE);
        mResultNull.setImageResource(R.mipmap.ic_error_page);
        mTryAgain.setVisibility(VISIBLE);
        mTryAgain.setText("加载失败，点击重试");
    }

    @Override
    public void setLoadingStatus(LoadingStatus loadingStatus) {
        super.setLoadingStatus(loadingStatus);
        mLoadingStatus = loadingStatus;
    }

    public void setResultNullImage(int resource) {
        mNullImageResource = resource;
    }

    public void setNullBtnVisible(boolean nullBtnVisible) {
        mNullBtnVisible = nullBtnVisible;
    }

    public void setResultNullDescribe(String resultNullDescribe, String tryDescribe) {
        mResultNullDescribe = resultNullDescribe;
        mTryDescribe = tryDescribe;
    }

    public void setLoadingViewClickListener(LoadingViewClickListener loadingViewClickListener) {
        mLoadingViewClickListener = loadingViewClickListener;
    }

    public interface LoadingViewClickListener {
        void loadingClick(LoadingStatus loadingStatus);
    }

}
