package com.hongguo.read.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2017/7/26.
 */

public class RechargeGiveMark extends RelativeLayout {
    public RechargeGiveMark(Context context) {
        super(context);
    }

    public RechargeGiveMark(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RechargeGiveMark(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            measureChild(childAt, widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = this.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = this.getChildAt(i);
            if (view.getVisibility() != View.GONE) {
                view.layout(getWidth() - view.getMeasuredWidth(), 0, view.getMeasuredWidth(),
                        view.getMeasuredHeight());
            }
            view.setRotation(45);
        }
    }
}
