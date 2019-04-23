package com.hongguo.read.widget.recommenstyle.scrollbook.scrollviewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * Created by losg
 */

public class ScrollViewPager extends ViewPager {

    private static final float IMAGE_RATIO = 1.4f;

    public ScrollViewPager(Context context) {
        this(context, null);
    }

    public ScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setClipChildren(false);
        setClipToPadding(false);
        setOffscreenPageLimit(3);
        setPageTransformer(true, new ScrollViewPagerTransformer());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredHeight = getMeasuredHeight();
        int measuredWidth = (int) (measuredHeight / IMAGE_RATIO);
        setPadding(measuredWidth, 0, measuredWidth, 0);
        setMeasuredDimension(measuredWidth * 3, measuredHeight);
    }
}
