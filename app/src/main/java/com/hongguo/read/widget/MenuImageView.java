package com.hongguo.read.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * Created by losg on 2018/2/9.
 */

public class MenuImageView extends AppCompatImageView {

    public MenuImageView(Context context) {
        super(context);
    }

    public MenuImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MenuImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        requestLayout();
    }
}
