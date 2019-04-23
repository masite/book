package com.hongguo.read.widget.openbook;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.hongguo.read.R;

/**
 * Created by losg on 2018/2/24.
 */

public class RotateView extends View {

    private float  mRotate;
    private Bitmap mBitmap;
    private Rect   mOrginRect;
    private Rect   mArmRect;
    private Rect   mBackArmRect;
    private Rect   mBackRect;
    private Matrix mMatrix = new Matrix();
    private Camera mCamera = new Camera();
    private Bitmap mBackBitmap;
    private Paint  mPaint;

    public RotateView(Context context) {
        super(context);
        initView();
    }

    public RotateView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mOrginRect = new Rect();
        mArmRect = new Rect();
        mBackBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_openbook_back);
        mBackRect = new Rect(0, 0, mBackBitmap.getWidth(), mBackBitmap.getHeight());
        mBackArmRect = new Rect();
    }

    public RotateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setRotate(float rotate) {
        mRotate = rotate;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mArmRect.set(0, 0, getMeasuredWidth() / 2, getMeasuredHeight());
        mBackArmRect.set(getMeasuredWidth() / 2, 0, getMeasuredWidth(), getMeasuredHeight());
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBitmap == null) return;

        canvas.drawBitmap(mBackBitmap, mBackRect, mBackArmRect, mPaint);

        mMatrix.reset();
        mCamera.save();
        mCamera.setLocation(0, 0, -getMeasuredHeight() / 2);
        mCamera.translate(getMeasuredWidth() / 2, 0, 0);
        mCamera.rotateY(-mRotate);
        mCamera.getMatrix(mMatrix);
        canvas.save();
        mMatrix.preTranslate(0, -getMeasuredHeight() / 2);
        mMatrix.postTranslate(0, getMeasuredHeight() / 2);
        canvas.concat(mMatrix);
        canvas.drawBitmap(mBitmap, mOrginRect, mArmRect, mPaint);
        canvas.restore();
        mCamera.restore();
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
        mOrginRect.set(0, 0, bitmap.getWidth(), bitmap.getHeight());
        requestLayout();
    }
}
