package com.hongguo.read.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.hongguo.read.R;

/**
 * Created by Administrator on 2017/5/5.
 */

public class TriangleView extends View {
    private Paint mPaint;
    private Paint mShadowPaint;
    private Path  mDrawPath;
    private int   mStartPosition;
    private int   mImageWidth;

    public TriangleView(Context context) {
        super(context);
    }

    public TriangleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray ty = context.obtainStyledAttributes(attrs, R.styleable.TriangleView);
        mStartPosition = (int) ty.getDimension(R.styleable.TriangleView_position, 0);
        mImageWidth = (int) ty.getDimension(R.styleable.TriangleView_imageSize, 0);
        ty.recycle();
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(getResources().getColor(R.color.base_white));
        mPaint.setAntiAlias(true);
        mDrawPath = new Path();

        mShadowPaint = new Paint();
        mShadowPaint.setStyle(Paint.Style.FILL);
        mShadowPaint.setColor(0xaaf5f5f5);
        mShadowPaint.setAntiAlias(true);

    }

    public void setStartPosition(int imageWidth, int startPosition) {
        mStartPosition = startPosition;
        mImageWidth = imageWidth / 2;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mDrawPath.reset();
        mDrawPath.moveTo(0, getMeasuredHeight());
        mDrawPath.lineTo(getMeasuredWidth(), getMeasuredHeight());
        mDrawPath.lineTo(getMeasuredWidth(), 0);
        canvas.drawPath(mDrawPath, mPaint);
        if (mStartPosition == 0) return;

        mDrawPath.reset();
        mDrawPath.moveTo(mStartPosition, getMeasuredHeight());

        int startHeight = getMeasuredHeight() - (int) (mStartPosition / ((float) getMeasuredWidth() / getMeasuredHeight()));
        mDrawPath.lineTo(mStartPosition, startHeight);

        int height = getMeasuredHeight() - (int) ((mStartPosition + mImageWidth) / ((float) getMeasuredWidth() / getMeasuredHeight()));

        mDrawPath.lineTo(mStartPosition + mImageWidth, height);
        canvas.drawPath(mDrawPath, mShadowPaint);

    }
}
