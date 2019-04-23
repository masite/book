package com.hongguo.common.widget.recycler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import com.hongguo.read.base.R;
import com.losg.library.utils.DisplayUtil;

/**
 * Created by losg on 2018/2/11.
 */

public class RecyclerViewWithBar extends FrameLayout implements RecyclerViewBar.RecyclerBarProgressChangeListener {

    private int                 mMoveY;
    private int                 mTotalY;
    private RecyclerView        mRecyclerView;
    private RecyclerViewBar     mRecyclerViewBar;
    private LinearLayoutManager mLinearLayoutManager;
    private int                 mItemHeight;
    private int                 mPerWindow;

    public RecyclerViewWithBar(@NonNull Context context) {
        this(context, null);
    }

    public RecyclerViewWithBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        mRecyclerView = new RecyclerView(getContext());
        mRecyclerView.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
        addView(mRecyclerView, new LayoutParams(-1, -1));
        mRecyclerViewBar = new RecyclerViewBar(getContext());
        mRecyclerViewBar.setImageResource(R.mipmap.ic_recycler_bar);
        mRecyclerViewBar.setRecyclerBarProgressChangeListener(this);

        LayoutParams layoutParams = new LayoutParams(-2, -1);
        layoutParams.gravity = Gravity.RIGHT;
        layoutParams.rightMargin = DisplayUtil.dip2px(getContext(), 0);
        addView(mRecyclerViewBar, layoutParams);

        mRecyclerView.addOnScrollListener(mOnScrollListener);
    }

    public void setLayoutManager(LinearLayoutManager layoutManager) {
        mLinearLayoutManager = layoutManager;
        mRecyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int size = h;
        //解决5.0和以下 recyclerview 计算到底部的问题
        if (size != 0) {
            ViewGroup.LayoutParams layoutParams = getChildAt(0).getLayoutParams();
            layoutParams.height = size;
            getChildAt(0).setLayoutParams(layoutParams);
        }
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

    }

    @Override
    public void barProgressChange(int dy) {
        float percent = (float) dy / mRecyclerViewBar.getMaxProgress() * mTotalY;
        mMoveY = (int) percent;
        float number = percent / mItemHeight;
        if (number != (int) number) {
            number = (int) number + 1;
            number = number > mRecyclerView.getAdapter().getItemCount() - mPerWindow ? mRecyclerView.getAdapter().getItemCount() - mPerWindow - 1 : number;
        }
        mLinearLayoutManager.scrollToPositionWithOffset((int) number, 0);
        mLinearLayoutManager.setStackFromEnd(true);
    }

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                mRecyclerViewBar.setIde();
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int maxProgress = mRecyclerViewBar.getMaxProgress();
            if (maxProgress == 0) {
                mRecyclerViewBar.setProgress(0);
                return;
            }
            LinearLayoutManager linearLayoutManager1 = (LinearLayoutManager) recyclerView.getLayoutManager();
            int firstCompletelyVisibleItemPosition = linearLayoutManager1.findFirstCompletelyVisibleItemPosition();
            int lastCompletelyVisibleItemPosition = linearLayoutManager1.findLastCompletelyVisibleItemPosition();
            if (firstCompletelyVisibleItemPosition == 0) {
                mPerWindow = lastCompletelyVisibleItemPosition - firstCompletelyVisibleItemPosition + 1;
                mItemHeight = linearLayoutManager1.getChildAt(0).getMeasuredHeight();
                mTotalY = mItemHeight * (recyclerView.getAdapter().getItemCount() - mPerWindow);
            }
            mMoveY += dy;
            mMoveY = mMoveY > mTotalY ? mTotalY : mMoveY;
            mMoveY = mMoveY < 0 ? 0 : mMoveY;

            int percent = (int) ((float) mMoveY / mTotalY * maxProgress);
            mRecyclerViewBar.setProgress(percent);
        }
    };

    public void smoothScrollToPosition(int selectedIndex) {
        mRecyclerView.smoothScrollToPosition(selectedIndex);
    }
}
