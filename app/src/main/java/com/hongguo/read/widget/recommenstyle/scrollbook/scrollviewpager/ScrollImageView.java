package com.hongguo.read.widget.recommenstyle.scrollbook.scrollviewpager;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by losg
 */

public class ScrollImageView extends android.support.v7.widget.AppCompatImageView {
    private static final float RATIO = 1.4f;

    public ScrollImageView(Context context) {
        this(context, null);
    }

    public ScrollImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setScaleType(ScaleType.CENTER_CROP);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredHeight = getMeasuredHeight();
        setMeasuredDimension((int) (measuredHeight / RATIO), measuredHeight);
    }
}
