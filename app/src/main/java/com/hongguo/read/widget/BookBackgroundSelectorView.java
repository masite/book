package com.hongguo.read.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by losg on 2018/1/16.
 */

public class BookBackgroundSelectorView extends View {

    private Paint mPaint;

    public BookBackgroundSelectorView(Context context) {
        this(context, null);
    }

    public BookBackgroundSelectorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BookBackgroundSelectorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    public void setColor(int color){
        mPaint.setColor(color);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(getMeasuredWidth() / 2 , getMeasuredHeight() / 2, getMeasuredWidth() / 2, mPaint);
    }
}
