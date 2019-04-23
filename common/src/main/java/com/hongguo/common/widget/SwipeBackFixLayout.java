package com.hongguo.common.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.gw.swipeback.SwipeBackLayout;

/**
 * Created time 2017/11/2.
 *
 * @author losg
 */

public class SwipeBackFixLayout extends SwipeBackLayout {

    public SwipeBackFixLayout(@NonNull Context context) {
        super(context);
    }

    public SwipeBackFixLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SwipeBackFixLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void attachToActivity(Activity activity) {
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView().findViewById(android.R.id.content);
        ViewGroup decorChild = (ViewGroup) decorView.getChildAt(0);
        decorChild.setBackgroundColor(Color.TRANSPARENT);
        decorView.removeView(decorChild);
        addView(decorChild);
        decorView.addView(this);
    }
}
