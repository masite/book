package com.hongguo.read.widget;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by losg on 2018/1/10.
 */

public class CommonBannerView extends BannerView {

    private static final float RADIO = 0.42f;

    public CommonBannerView(Context context) {
        super(context);
    }

    public CommonBannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CommonBannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = (int) (getResources().getDisplayMetrics().widthPixels * RADIO);
        super.onMeasure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));

    }

}
