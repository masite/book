package com.hongguo.read.widget.pullfind;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;

/**
 * Created by losg on 2018/3/29.
 */

public class PullFindLayout extends ViewGroup {

    private int                    mMoveY;
    private RecyclerView           mRecyclerView;
    private WaterDropView          mWaterDropView;
    private int                    mTempY;
    private FindMoreArriveListener mFindMoreArriveListener;

    public PullFindLayout(Context context) {
        this(context, null);
    }

    public PullFindLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullFindLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initView();
    }

    private void initView() {
        if (getChildAt(1) instanceof RecyclerView) {
            mRecyclerView = (RecyclerView) getChildAt(1);
        } else {
            mRecyclerView = (RecyclerView) ((FrameLayout) getChildAt(1)).getChildAt(0);
        }
        mWaterDropView = (WaterDropView) getChildAt(0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        for (int i = 0; i < getChildCount(); i++) {
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (getChildCount() == 0) return;
        int left = (getMeasuredWidth() - getChildAt(0).getMeasuredWidth()) / 2;
        getChildAt(0).layout(left, getMeasuredHeight() - getChildAt(0).getMeasuredHeight(), left + getChildAt(0).getMeasuredWidth(), getMeasuredHeight());
        getChildAt(1).layout(0, -mMoveY, getMeasuredWidth(), getMeasuredHeight() - mMoveY);
        if (getChildAt(1) instanceof RecyclerView) {
            mRecyclerView = (RecyclerView) getChildAt(1);
        } else {
            mRecyclerView = (RecyclerView) ((FrameLayout) getChildAt(1)).getChildAt(0);
        }
        mWaterDropView = (WaterDropView) getChildAt(0);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mRecyclerView == null) return false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTempY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float currentY = ev.getY();
                int dy = (int) (currentY - mTempY);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                int last = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                int itemCount = mRecyclerView.getAdapter().getItemCount();
                //上拉(已经到最底部), 将上拉操作交给本层去处理
                if (dy < 0 && last >= itemCount - 1) {
                    MotionEvent obtain = MotionEvent.obtain(ev.getDownTime(), ev.getEventTime(), MotionEvent.ACTION_CANCEL, ev.getX(), ev.getY(), ev.getMetaState());
                    getChildAt(1).dispatchTouchEvent(obtain);
                    mMoveY -= dy / 2;
                    if (mMoveY > mWaterDropView.getMeasuredHeight()) {
                        mMoveY = mWaterDropView.getMeasuredHeight();
                        mFindMoreArriveListener.moreArrive();
                    }
                    if (mMoveY >= mWaterDropView.getMaxCircleRadius() * 2) {
                        int current = (int) (mMoveY - mWaterDropView.getMaxCircleRadius() * 2);
                        float max = mWaterDropView.getMeasuredHeight() - mWaterDropView.getMaxCircleRadius() * 2;
                        mWaterDropView.updatePercent(current / max);
                    }
                    //已经拉到最底
                    if (mMoveY <= 0) mMoveY = 0;
                    requestLayout();
                    mTempY = (int) currentY;
                    return true;
                }
                mTempY = (int) currentY;
                break;
            case MotionEvent.ACTION_UP:
                if (mMoveY != 0) {
                    if (mMoveY == mWaterDropView.getMeasuredHeight()) {
                        mFindMoreArriveListener.showMoreFind();
                    }
                    //返回初始点
                    animToBottom();
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private void animToBottom() {
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(300);
        valueAnimator.setFloatValues(mMoveY, 0);
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.addUpdateListener(animation -> {
            mMoveY = (int) ((float) animation.getAnimatedValue());
            if (mMoveY >= mWaterDropView.getMaxCircleRadius() * 2) {
                int current = (int) (mMoveY - mWaterDropView.getMaxCircleRadius() * 2);
                float max = mWaterDropView.getMeasuredHeight() - mWaterDropView.getMaxCircleRadius() * 2;
                mWaterDropView.updatePercent(current / max);
            } else {
                mWaterDropView.updatePercent(0);
            }
            requestLayout();
        });
        valueAnimator.start();
    }

    public void setFindMoreArriveListener(FindMoreArriveListener findMoreArriveListener) {
        mFindMoreArriveListener = findMoreArriveListener;
    }

    public interface FindMoreArriveListener {
        void moreArrive();

        void showMoreFind();
    }

    public void setColor(int color) {
        mWaterDropView.setColor(color);
    }
}
