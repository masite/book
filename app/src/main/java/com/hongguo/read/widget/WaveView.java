package com.hongguo.read.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.hongguo.read.R;

/**
 * Created by losg on 2018/1/11.
 */

public class WaveView extends View implements ValueAnimator.AnimatorUpdateListener {

    private static final int ANIMATION_DURATION = 30000;
    private static final int WAVE_SIZE          = 2;
    private static final int WAVE_SECOND_SIZE   = 1;

    private Paint         mPaint;
    private Path          mPath;
    private int           mWidth;
    private int           mHeight;
    private int           mColorOne;
    private int           mColorTwo;
    private int           mDx;
    private int           mDx2;
    private int           mFramesNumber;
    private int           mPerAdd;
    private int           mTwoPerAdd;
    private int           mPerWidth;
    private int           mSecondWidth;
    private ValueAnimator mValueAnimator;

    public WaveView(Context context) {
        this(context, null);
    }

    public WaveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ty = context.obtainStyledAttributes(attrs, R.styleable.WaveView);
        mColorOne = ty.getColor(R.styleable.WaveView_color_one, 0x55ffffff);
        mColorTwo = ty.getColor(R.styleable.WaveView_color_two, 0xffffffff);
        ty.recycle();
        init();
    }

    private void init() {
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        initAnimaion();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mPerWidth = mWidth / WAVE_SIZE;
        mSecondWidth = mWidth / WAVE_SECOND_SIZE;
        //总帧数
        mFramesNumber = ANIMATION_DURATION / 1000 * 60;
        mPerAdd = mPerWidth * 2 / mFramesNumber;
        mTwoPerAdd = mSecondWidth * 2 / mFramesNumber;

    }

    private void initAnimaion() {
        if (mValueAnimator == null) {
            mValueAnimator = ValueAnimator.ofFloat(0, 1);
            mValueAnimator.setDuration(ANIMATION_DURATION);
            mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
            mValueAnimator.setInterpolator(new LinearInterpolator());
        }
    }

    public void startAnimation() {
        mValueAnimator.removeAllUpdateListeners();
        mValueAnimator.addUpdateListener(this);
        if(!mValueAnimator.isRunning()) {
            mValueAnimator.start();
        }
    }

    public void pause() {
        mValueAnimator.cancel();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();
        mPaint.setColor(mColorOne);
        int currentPosition = -2 * mPerWidth + mDx;
        mPath.moveTo(currentPosition, mHeight / 2);
        for (int i = 0; i < WAVE_SIZE + 2; i++) {
            mPath.quadTo(currentPosition + mPerWidth / 2 + mPerWidth * i, i % 2 == 0 ? 0 : mHeight, currentPosition + mPerWidth * (i + 1), mHeight / 2);
        }

        mPath.lineTo(mWidth, mHeight);
        mPath.lineTo(0, mHeight);
        mPath.close();
        canvas.drawPath(mPath, mPaint);

        mPath.reset();
        mPaint.setColor(mColorTwo);
        currentPosition = -2 * mSecondWidth + mDx2;
        mPath.moveTo(currentPosition, mHeight / 2);
        for (int i = 0; i < WAVE_SECOND_SIZE + 2; i++) {
            mPath.quadTo(currentPosition + mSecondWidth / 2 + mSecondWidth * i, i % 2 == 0 ? 0 : mHeight, currentPosition + mSecondWidth * (i + 1), mHeight / 2);
        }

        mPath.lineTo(mWidth, mHeight);
        mPath.lineTo(0, mHeight);
        mPath.close();

        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        if (mDx == mPerWidth * 2) {
            mDx = 0;
            mDx2 = 0;
        }

        mDx += mPerAdd;
        mDx2 += mTwoPerAdd;
        if (mDx >= mPerWidth * 2) {
            mDx = mPerWidth * 2;
        }

        if (mDx2 >= mSecondWidth * 2) {
            mDx2 = mSecondWidth * 2;
        }

        invalidate();
    }

}
