package com.hongguo.common.base;

import android.content.Context;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by losg on 2018/1/5.
 */

public abstract class BaseViewHelper {

    protected     Context  mContext;
    private final Unbinder mBind;
    private final View     mView;

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
