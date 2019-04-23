package com.hongguo.read.widget;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by losg
 * 长宽比为 1 / 1.4 通用图片比例
 */

public class CommonImageView extends android.support.v7.widget.AppCompatImageView {

    private static final float RATIO = 1.4f;

    public CommonImageView(Context context) {
        this(context, null);
    }

    public CommonImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setScaleType(ScaleType.FIT_XY);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = getMeasuredWidth();
        int height = (int) (measuredWidth * RATIO);
        setMeasuredDimension(measuredWidth, height);
    }
}
