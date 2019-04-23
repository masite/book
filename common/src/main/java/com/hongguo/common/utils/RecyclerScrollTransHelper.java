package com.hongguo.common.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by losg on 2017/12/27.
 */

public class RecyclerScrollTransHelper extends RecyclerView.OnScrollListener {

    private RecyclerView                mRecyclerView;
    private int                         mCurrentDy;
    private ScrollTransProgressListener mScrollTransProgressListener;
    private View                        mArmView;
    private View                        mScrollView;

    public RecyclerScrollTransHelper(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        mRecyclerView.addOnScrollListener(this);
    }

    public void setScrollView(View view) {
        mScrollView = view;
    }

    public void setArmView(View view) {
        mArmView = view;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int armHeight = mScrollView.getMeasuredHeight() - mArmView.getMeasuredHeight();
        mCurrentDy += dy;
        int currentDy = dy;
        if (mCurrentDy > armHeight) {
            currentDy = armHeight;
        } else if (mCurrentDy < 0) {
            currentDy = 0;
        }else{
            currentDy = mCurrentDy;
        }
        if (armHeight == 0) return;
        int percent = (int) ((float) currentDy / armHeight * 100);
        if(mScrollTransProgressListener != null){
            mScrollTransProgressListener.progressChange(percent);
        }
    }

    public void destory() {
        mRecyclerView.removeOnScrollListener(this);
    }


    public void setScrollTransProgressListener(ScrollTransProgressListener scrollTransProgressListener) {
        mScrollTransProgressListener = scrollTransProgressListener;
    }

    public interface ScrollTransProgressListener {
        void progressChange(int percent);
    }
}
