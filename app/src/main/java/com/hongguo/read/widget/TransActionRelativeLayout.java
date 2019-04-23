package com.hongguo.read.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by losg on 2017/12/27.
 */

public class TransActionRelativeLayout extends RelativeLayout {

    public TransActionRelativeLayout(Context context) {
        super(context);
    }

    public TransActionRelativeLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TransActionRelativeLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        post(mRunnable);
    }

    private Runnable mRunnable = () -> getChildAt(0).setLayoutParams(new LayoutParams(-1, getMeasuredHeight()));
}
