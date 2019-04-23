package com.hongguo.common.utils;

import android.view.View;

import com.hongguo.common.widget.ScrollViewEx;


/**
 * Created by losg on 2017/12/27.
 */

public class ScrollTransHelper implements ScrollViewEx.ScrollViewListner {

    private ScrollViewEx                mScrollViewEx;
    private ScrollTransProgressListener mScrollTransProgressListener;
    private View                        mArmView;
    private View                        mScrollView;

    public ScrollTransHelper(ScrollViewEx scrollViewEx) {
        mScrollViewEx = scrollViewEx;
       mScrollViewEx.setScrollViewListner(this);
    }

    public void setScrollView(View view) {
        mScrollView = view;
    }

    public void setArmView(View view) {
        mArmView = view;
    }

    public void setScrollTransProgressListener(ScrollTransProgressListener scrollTransProgressListener) {
        mScrollTransProgressListener = scrollTransProgressListener;
    }

    @Override
    public void scroll(int dy) {
        int armHeight = mScrollView.getMeasuredHeight() - mArmView.getMeasuredHeight();
        int currentDy;
        if (dy > armHeight) {
            currentDy = armHeight;
        } else if (dy < 0) {
            currentDy = 0;
        }else{
            currentDy = dy;
        }
        if (armHeight == 0) return;
        int percent = (int) ((float) currentDy / armHeight * 100);
        if(mScrollTransProgressListener != null){
            mScrollTransProgressListener.progressChange(percent);
        }
    }

    public interface ScrollTransProgressListener {
        void progressChange(int percent);
    }
}
