package com.hongguo.read.widget;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created time 2017/12/6.
 *
 * @author losg
 */

public class TopicImageView extends android.support.v7.widget.AppCompatImageView {

    private static final float RADIO = 0.4f;

    public TopicImageView(Context context) {
        super(context);
    }

    public TopicImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TopicImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = (int) (width * RADIO);
        setMeasuredDimension(width, height);
    }
}
