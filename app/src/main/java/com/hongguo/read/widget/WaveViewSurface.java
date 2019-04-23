package com.hongguo.read.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.hongguo.read.R;

/**
 * Created by losg on 2018/1/11.
 */

public class WaveViewSurface extends SurfaceView implements SurfaceHolder.Callback {

    private WaveDrawThead mWaveDrawThead;

    private static final int ANIMATION_DURATION = 4000;
    private static final int WAVE_SIZE          = 2;
    private static final int WAVE_SECOND_SIZE   = 3;
    private static final int WAVE_THREE_SIZE    = 1;

    private Paint mPaint;
    private Path  mPath;
    private int   mWidth;
    private int   mHeight;
    private int   mColorOne;
    private int   mColorTwo;
    private int   mColorThree;
    private int   dx;
    private int   dx2;
    private int   dx3;
    private int   mPerWidth;
    private int   mSecondWidth;
    private int   mThreeWidth;

    public WaveViewSurface(Context context) {
        this(context, null);
    }

    public WaveViewSurface(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveViewSurface(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ty = context.obtainStyledAttributes(attrs, R.styleable.WaveView);
        mColorOne = ty.getColor(R.styleable.WaveView_color_one, 0x55ffffff);
        mColorTwo = ty.getColor(R.styleable.WaveView_color_two, 0xaaffffff);
        mColorThree = ty.getColor(R.styleable.WaveView_color_three, 0x88ffffff);
        ty.recycle();
        initView();
    }

    private void initView() {
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mWaveDrawThead = new WaveDrawThead(this);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mWaveDrawThead.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mWidth = width;
        mHeight = height;
        mPerWidth = mWidth / WAVE_SIZE;
        mSecondWidth = mWidth / WAVE_SECOND_SIZE;
        mThreeWidth = mWidth / WAVE_THREE_SIZE;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    private class WaveDrawThead extends Thread {
        private SurfaceView   mSurfaceView;
        private SurfaceHolder mSurfaceHolder;

        public WaveDrawThead(SurfaceView surfaceView) {
            mSurfaceView = surfaceView;
            mSurfaceHolder = mSurfaceView.getHolder();
        }

        @Override
        public void run() {
            super.run();
            drawWave(mSurfaceHolder);
        }
    }

    private void drawWave(SurfaceHolder surfaceHolder) {
        Canvas canvas = null;
        try {
            canvas = surfaceHolder.lockCanvas();
            while (true) {
                doDraw(canvas);
                computeTime();
            }
        } catch (Exception e) {
        } finally {
            if (canvas != null)
                surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void computeTime() throws Exception {
        //一共多少帧的 图像
        float frame = ANIMATION_DURATION / 1000 * 60;
        dx += (int) (mPerWidth * 2 / frame);
        dx2 += (int) (mSecondWidth * 2 / frame);
        dx3 += (int) (mThreeWidth * 2 / frame);
        Thread.sleep(10);
    }

    private void doDraw(Canvas canvas) {
        mPath.reset();
        mPaint.setColor(mColorOne);
        int currentPosition = -2 * mPerWidth + dx;
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
        currentPosition = -2 * mSecondWidth + dx2;
        mPath.moveTo(currentPosition, mHeight / 2);
        for (int i = 0; i < WAVE_SECOND_SIZE + 2; i++) {
            mPath.quadTo(currentPosition + mSecondWidth / 2 + mSecondWidth * i, i % 2 == 0 ? 0 : mHeight, currentPosition + mSecondWidth * (i + 1), mHeight / 2);
        }

        mPath.lineTo(mWidth, mHeight);
        mPath.lineTo(0, mHeight);
        mPath.close();

        canvas.drawPath(mPath, mPaint);

        mPath.reset();
        mPaint.setColor(mColorThree);
        currentPosition = -2 * mThreeWidth + dx3;
        mPath.moveTo(currentPosition, mHeight / 2);
        for (int i = 0; i < WAVE_THREE_SIZE + 2; i++) {
            mPath.quadTo(currentPosition + mThreeWidth / 2 + mThreeWidth * i, i % 2 == 0 ? 0 : mHeight, currentPosition + mThreeWidth * (i + 1), mHeight / 2);
        }

        mPath.lineTo(mWidth, mHeight);
        mPath.lineTo(0, mHeight);
        mPath.close();

        canvas.drawPath(mPath, mPaint);
    }

}
