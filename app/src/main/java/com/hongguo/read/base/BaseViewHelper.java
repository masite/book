package com.hongguo.read.base;

import android.content.Context;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by losg on 2018/1/5.
 */

public abstract class BaseViewHelper {

    protected Context  mContext;
    private   Unbinder mBind;
    protected View     mView;

    public BaseViewHelper(Context context) {
        mContext = context;
        mView = View.inflate(mContext, initLayout(), null);
        mBind = ButterKnife.bind(this, mView);
    }

    protected abstract int initLayout();


    public void destory() {
        mBind.unbind();
    }


    public View getView() {
        return mView;
    }

}
