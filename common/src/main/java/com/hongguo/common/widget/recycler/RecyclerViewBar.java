package com.hongguo.common.widget.recycler;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hongguo.read.base.R;


/**
 * Created by Administrator on 2018/2/10.
 */

public class RecyclerViewBar extends ViewGroup {

    private int                               mImageResource;
    private ImageView                         mBarImage;
    private RectF                             mBarRect;
    private int                               mBarWidth;
    private int                               mBarHeight;
    private int                               mCurrentDY;
    private float                             mTempY;
    private boolean                           mScroll;
    private boolean                           mIsDown;
    private boolean                           mIsMove;
    private int                               mLastPercent;
    private RecyclerBarProgressChangeListener mRecyclerBarProgressChangeListener;

    public RecyclerViewBar(Context context) {
        this(context, null);
    }

    public RecyclerViewBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ty = context.obtainStyledAttributes(attrs, R.styleable.RecyclerViewBar);
        mImageResource = ty.getResourceId(R.styleable.RecyclerViewBar_BarResource, R.mipmap.ic_recycler_bar);
        ty.recycle();
        initView();
    }

    private void initView() {
        mBarImage = new ImageView(getContext());
        if (mImageResource != 0) {
            mBarImage.setImageResource(mImageResource);
        }
        mBarRect = new RectF();
        addView(mBarImage, new LayoutParams(-2, -2));
    }

    public void setImageResource(int resource){
        mBarImage.setImageResource(resource);
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mBarImage.measure(widthMeasureSpec, heightMeasureSpec);
        mBarWidth = mBarImage.getMeasuredWidth();
        mBarHeight = mBarImage.getMeasuredWidth();
        mBarRect.left = 0;
        mBarRect.top = mCurrentDY;
        mBarRect.right = mBarWidth;
        mBarRect.bottom = mCurrentDY + mBarHeight;
        super.onMeasure(MeasureSpec.makeMeasureSpec(mBarWidth, MeasureSpec.EXACTLY), heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean change, int left, int top, int right, int bottom) {
        mBarImage.layout((int) mBarRect.left, (int) mBarRect.top, (int) mBarRect.right, (int) mBarRect.bottom);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mBarRect.top = mCurrentDY;
                mBarRect.bottom = mCurrentDY + mBarHeight;
                if (mBarImage.getVisibility() == GONE || !mBarRect.contains(event.getX(), event.getY()))
                    return false;
                mScroll = true;
                mIsDown = true;
                mIsMove = false;
                mTempY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int currentPosition = (int) event.getY();
                int dy = (int) (currentPosition - mTempY);
                mCurrentDY += dy;
                if (mCurrentDY >= getMeasuredHeight() - mBarHeight) {
                    mCurrentDY = getMeasuredHeight() - mBarHeight;
                }
                if (mCurrentDY <= 0) mCurrentDY = 0;
                computeProgress(true);
                mTempY = currentPosition;
                mIsMove = true;
                break;
            case MotionEvent.ACTION_UP:
                mScroll = false;
                mIsDown = false;
                mIsMove = false;
                animationGone();
                break;
        }
        return true;
    }

    private void computeProgress(boolean isMove) {
        mBarRect.top = mCurrentDY;
        mBarRect.bottom = mCurrentDY + mBarHeight;
        requestLayout();
        if (isMove && mRecyclerBarProgressChangeListener != null) {
            mRecyclerBarProgressChangeListener.barProgressChange(mCurrentDY);
        }
    }

    private void animationGone() {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!mScroll && !mIsDown) {
                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mBarImage, "translationX", 0, mBarWidth).setDuration(200);
                    objectAnimator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mBarImage.setVisibility(GONE);
                        }
                    });
                    objectAnimator.start();
                }
            }
        }, 1000);
    }

    private void animaionShow() {
        mBarImage.setVisibility(VISIBLE);
        ObjectAnimator.ofFloat(mBarImage, "translationX", mBarWidth, 0).setDuration(200).start();
    }


    public int getMaxProgress() {
        return getMeasuredHeight() - mBarHeight;
    }

    public void setProgress(int progress) {
        if (progress == 0 && mLastPercent == 0) {
            animationGone();
            return;
        }
        mLastPercent = progress;
        if (mIsMove) {
            return;
        }
        int maxProgress = getMaxProgress();
        progress = progress > maxProgress ? maxProgress : progress;
        progress = progress < 0 ? 0 : progress;
        mScroll = true;
        if (mBarImage.getVisibility() == GONE) {
            animaionShow();
        }
        mCurrentDY = progress;
        computeProgress(false);
    }

    public void setIde() {
        mScroll = false;
        animationGone();
    }

    public void setRecyclerBarProgressChangeListener(RecyclerBarProgressChangeListener recyclerBarProgressChangeListener) {
        mRecyclerBarProgressChangeListener = recyclerBarProgressChangeListener;
    }

    public interface RecyclerBarProgressChangeListener {
        void barProgressChange(int dy);
    }
}

