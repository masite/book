package com.hongguo.read.widget.openbook;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

/**
 * Created by losg on 2017/4/28.
 */

public class OpenBookView extends ViewGroup {

    public static final int ANIMAION_TIME  = 700;

    private RotateView mCoverImage;
    private RectF      mOrginWidth;
    private ImageView  mOrginImageview;

    private float mCurrentPercent = 0;

    private OpenBookListener mOpenBookListener;
    private ValueAnimator    mOpenValueAnimator;
    private RectF mLayoutRectF = new RectF();

    public OpenBookView(Context context) {
        this(context, null);
    }

    public OpenBookView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OpenBookView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mOrginWidth = new RectF();
        mCoverImage = new RotateView(getContext());
        addView(mCoverImage);
        setBackgroundColor(Color.TRANSPARENT);
    }

    public void setCoverBitmap(ImageView imageView) {
        mOrginImageview = imageView;
    }

    //默认全屏
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mOrginWidth == null) return;
        computeCurrentPosition();
        mCoverImage.measure(MeasureSpec.makeMeasureSpec((int) mLayoutRectF.width(), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec((int) mLayoutRectF.height(), MeasureSpec.EXACTLY));
        setMeasuredDimension(getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        getChildAt(0).layout((int) mLayoutRectF.left, (int) mLayoutRectF.top, (int) mLayoutRectF.right, (int) mLayoutRectF.bottom);
    }

    private void computeCurrentPosition() {
        mLayoutRectF.left = (mOrginWidth.left - mOrginWidth.width()) - (mOrginWidth.left - mOrginWidth.width() + getWindowWidth()) * mCurrentPercent;
        mLayoutRectF.right = mOrginWidth.right + (getWindowWidth() - mOrginWidth.right) * mCurrentPercent;
        mLayoutRectF.top = mOrginWidth.top - mOrginWidth.top * mCurrentPercent;
        mLayoutRectF.bottom = mOrginWidth.bottom + (getHeightWidth() - mOrginWidth.bottom) * mCurrentPercent;
    }

    private int getWindowWidth() {
        return getResources().getDisplayMetrics().widthPixels;
    }

    private int getHeightWidth() {
        return getResources().getDisplayMetrics().heightPixels;
    }

    public void setContentRectf(RectF rectf) {
        mOrginWidth = rectf;
    }

    //当前进度  0 - 1
    public void setPercent(float percent) {
        mCurrentPercent = percent;
    }

    public boolean start() {
        Drawable drawable = mOrginImageview.getDrawable();
        getImageLocal(mOrginImageview);

        if (drawable == null || !(drawable instanceof BitmapDrawable)) {
            return false;
        }

        mCoverImage.setBitmap(((BitmapDrawable) drawable).getBitmap());

        mOpenValueAnimator = new ValueAnimator();
        mOpenValueAnimator.setInterpolator(new AccelerateInterpolator());
        mOpenValueAnimator.setIntValues(0, 180);
        mOpenValueAnimator.setDuration(ANIMAION_TIME);
        mOpenValueAnimator.addUpdateListener(animation -> {
            float percent = (float) animation.getCurrentPlayTime() / ANIMAION_TIME;
            mCoverImage.setRotate((int) animation.getAnimatedValue());
            if (percent >= 1) {
                percent = 1;
            }
            setPercent(percent);

            if (percent >= 1 && mOpenBookListener != null) {
                mOpenBookListener.openFinish();
            }
            notifyChange();
        });
        mOpenValueAnimator.start();
        return true;
    }


    public RectF getImageLocal(ImageView imageView) {
        int measuredHeight = imageView.getMeasuredHeight();
        int measuredWidth = imageView.getMeasuredWidth();
        int[] local = new int[2];
        imageView.getLocationInWindow(local);

        RectF contentRectF = new RectF();
        contentRectF.left = local[0];
        contentRectF.right = local[0] + measuredWidth;
        contentRectF.top = local[1];
        contentRectF.bottom = local[1] + measuredHeight;
        setContentRectf(contentRectF);
        return contentRectF;
    }

    public void close(RectF rectF) {
        mOrginWidth = rectF;
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.setIntValues(180, 0);
        valueAnimator.setDuration(ANIMAION_TIME);
        valueAnimator.addUpdateListener(animation -> {
            float percent = (ANIMAION_TIME - (float) animation.getCurrentPlayTime()) / ANIMAION_TIME;
            mCoverImage.setRotate((int) animation.getAnimatedValue());

            if (percent <= 0) {
                percent = 0;
            }
            setPercent(percent);
            if (percent <= 0 && mOpenBookListener != null) {
                mOpenBookListener.closeFinish();
            }
            notifyChange();
        });
        valueAnimator.start();
    }

    public void setOpenBookListener(OpenBookListener openBookListener) {
        mOpenBookListener = openBookListener;
    }

    public interface OpenBookListener {
        void openFinish();

        void closeFinish();
    }

    public void notifyChange() {
        requestLayout();
        invalidate();
    }

}
