package com.hongguo.read.widget.loading;


import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.losg.library.widget.loading.BaLoadingView;
import com.losg.library.widget.loading.BaLoadingViewHelper;


public class LoadingHelper extends BaLoadingViewHelper<LoadingView> {

    private LoadingView                          mLoadingView;
    private String                               mResultNullDescribe;
    private LoadingView.LoadingViewClickListener mLoadingViewClickListener;

    public LoadingHelper(Context context) {
        super(context);
    }

    public LoadingHelper(Context context, View bindView) {
        super(context);
        bindView(bindView);
        setStatus(BaLoadingView.LoadingStatus.LOADING);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mLoadingView.getLayoutParams();
        if(layoutParams != null){
            layoutParams.gravity = Gravity.CENTER;
            mLoadingView.setLayoutParams(layoutParams);
        }
    }

    @Override
    public void bindView(View view) {
        super.bindView(view);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mLoadingView.getLayoutParams();
        if(layoutParams != null){
            layoutParams.gravity = Gravity.CENTER;
            mLoadingView.setLayoutParams(layoutParams);
        }
    }

    @Override
    protected LoadingView createLoadingView(Context context) {
        mLoadingView = new LoadingView(context);
        return mLoadingView;
    }

    public void setResultNullImage(int resource) {
        mLoadingView.setResultNullImage(resource);
    }

    public void setResultNullVisible(boolean visible) {
        mLoadingView.setNullBtnVisible(visible);
    }

    public void setResultNullDescribe(String resultNullDescribe, String tryDescribe) {
        mResultNullDescribe = resultNullDescribe;
        mLoadingView.setResultNullDescribe(resultNullDescribe, tryDescribe);
    }

    public void setLoadingViewClickListener(LoadingView.LoadingViewClickListener loadingViewClickListener) {
        mLoadingViewClickListener = loadingViewClickListener;
        mLoadingView.setLoadingViewClickListener(loadingViewClickListener);
    }

}
