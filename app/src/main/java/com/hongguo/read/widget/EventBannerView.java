package com.hongguo.read.widget;

/**
 * Created by Administrator on 2017/6/13.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by dongjunkun on 2015/8/9.
 */
public class EventBannerView extends BannerView {

    private static final float RATIO = 4.2f;

    public EventBannerView(Context context) {
        this(context, null);
    }

    public EventBannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EventBannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setScaleType(ImageView.ScaleType.FIT_XY);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = getMeasuredWidth();
        int height = (int) (measuredWidth / RATIO);
        super.onMeasure(MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }
}
