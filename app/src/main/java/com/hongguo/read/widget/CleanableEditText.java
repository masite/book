package com.hongguo.read.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.hongguo.read.R;
import com.losg.library.utils.DisplayUtil;

/**
 * Created time 2017/11/1.
 *
 * @author losg
 */

public class CleanableEditText extends AppCompatEditText implements TextWatcher {
    private boolean mShowDrawable = false;
    private Drawable mClearIcon;
    private int      mIconWidth;
    private int      mIconHeight;
    private int      mPaddingLeft;
    private int      mPaddingTop;
    private int      mPaddingRight;
    private int      mPaddingBottom;
    private boolean mShowClearBtn = true;
    public CleanableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mClearIcon = getResources().getDrawable(R.mipmap.ic_edit_delete);
        mIconWidth = mClearIcon.getIntrinsicWidth();
        mIconHeight = mClearIcon.getIntrinsicHeight();
        mPaddingLeft = getPaddingLeft();
        mPaddingTop = getPaddingTop();
        mPaddingRight = getPaddingRight();
        mPaddingBottom = getPaddingBottom();
        setPadding(mPaddingLeft, mPaddingTop, mPaddingRight + mIconWidth, mPaddingBottom);
        addTextChangedListener(this);
    }
    public void setClearButtonVisibility(boolean visible) {
        mShowClearBtn = visible;
        invalidate();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mShowClearBtn) {
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mClearIcon.getBounds().contains((int) event.getX(), (int) event.getY())) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mClearIcon.getBounds().contains((int) event.getX(), (int) event.getY())) {
                    setText("");
                    requestLayout();
                    return true;
                }
                break;
        }
        return super.onTouchEvent(event);
    }
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (!TextUtils.isEmpty(getText())) {
            if (!mShowClearBtn) return;
            int iconWidth = mIconWidth;
            int iconHeight = mIconHeight;
            int textViewWidth = right - left;
            int textViewHeight = bottom - top;
            iconWidth = Math.min(iconWidth, textViewWidth);
            iconHeight = Math.min(iconHeight, textViewHeight);
            iconWidth = iconHeight = Math.min(iconWidth, iconHeight);
            mShowDrawable = true;
            int l = textViewWidth - iconWidth;
            l = l <= 0 ? 0 : l - (textViewWidth - l - DisplayUtil.px2dip(getContext(), getPaddingLeft() + getPaddingRight())) / 2;
            int t = (textViewHeight - iconHeight) / 2;
            int r = l + iconWidth;
            int b = t + iconHeight;
            mClearIcon.setBounds(l, t, r, b);
        } else {
            mShowDrawable = false;
            mClearIcon.setBounds(0, 0, 0, 0);
        }
    }
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (mShowClearBtn) {
            mClearIcon.draw(canvas);
        }
    }
    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }
    @Override
    public void afterTextChanged(Editable s) {
        // 当有文本没有显示删除图标时和没有文本显示删除图标时，重新请求布局
        if ((!mShowDrawable && !TextUtils.isEmpty(s)) || (mShowDrawable && TextUtils.isEmpty(s))) {
            requestLayout();
        }
    }
}