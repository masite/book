package com.hongguo.read.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.hongguo.read.R;

/**
 * Created by losg on 2017/7/13.
 */

public class PressImageView extends android.support.v7.widget.AppCompatImageView {

    private boolean mImageChange = false;

    private int[] PRESS_CHANGE_IMAGE = new int[]{R.attr.image_imageChange};

    public PressImageView(Context context) {
        this(context, null);
    }

    public PressImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PressImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PressImageView);
        ColorStateList colorStateList = typedArray.getColorStateList(R.styleable.PressImageView_image_color_list);
        if (colorStateList != null) {
            ViewCompat.setBackgroundTintList(this, colorStateList);
        }
        typedArray.recycle();
        setClickable(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean touch = super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setSelected(true);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                setSelected(false);
                break;
        }
        return touch;
    }

    public boolean isImageChange() {
        return mImageChange;
    }

    public void setChangeImage(boolean change) {
        if (mImageChange != change) {
            mImageChange = change;
            refreshDrawableState();
        }
    }

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        if (mImageChange) {
            int[] ints = super.onCreateDrawableState(extraSpace + 1);
            return mergeDrawableStates(ints, PRESS_CHANGE_IMAGE);
        }
        return super.onCreateDrawableState(extraSpace);
    }
}
