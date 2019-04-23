package com.hongguo.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by Administrator on 2018/2/8.
 */

public class ScrollViewEx extends ScrollView {

    private ScrollViewListner mScrollViewListner;

    public ScrollViewEx(Context context) {
        super(context);
    }

    public ScrollViewEx(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollViewEx(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mScrollViewListner != null) {
            mScrollViewListner.scroll(t);
        }
    }

    public void setScrollViewListner(ScrollViewListner scrollViewListner) {
        mScrollViewListner = scrollViewListner;
    }

    public interface ScrollViewListner {
        void scroll(int dy);
    }
}
