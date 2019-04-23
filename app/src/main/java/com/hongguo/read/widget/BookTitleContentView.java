package com.hongguo.read.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by losg on 2017/12/26.
 */

public class BookTitleContentView extends RelativeLayout {

    private ImageView mImageView;
    private View     mView;

    public BookTitleContentView(Context context) {
        this(context, null);
    }

    public BookTitleContentView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BookTitleContentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mImageView = new ImageView(getContext());
        mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mView = new View(getContext());
        //黑色蒙版，预防白底后文字看不见
        mView.setBackgroundColor(0x25000000);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getChildAt(0) instanceof ImageView || getChildCount() == 0) return;
        if (mImageView.getParent() != null) return;
        addView(mImageView, 0, new LayoutParams(getMeasuredWidth(), getMeasuredHeight()));
        addView(mView, 1, new LayoutParams(getMeasuredWidth(), getMeasuredHeight()));
    }


    public ImageView getBookBgImage() {
        if (getChildCount() == 0 || !(getChildAt(0) instanceof ImageView)) return null;
        return (ImageView) getChildAt(0);
    }

}
