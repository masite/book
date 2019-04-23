package com.hongguo.common.widget.refresh.base;

import android.content.Context;
import android.view.View;

/**
 * Created by losg on 2016/8/8.
 */
public abstract class BaFooterView {

    protected View    mFooterView;
    protected Context mContext;

    public BaFooterView(Context context) {
        mContext = context;
        mFooterView = initView();
    }

    public abstract View initView();

    //加载中
    public abstract void loadding();

    //初始状态
    public abstract void loadNormal();

    //加载失败
    public abstract void loaddingError(String errorMessage);

    //全部加载完成状态
    public abstract void allLoaded(boolean showInfo);

    public View getFooterView() {
        return mFooterView;
    }
}

