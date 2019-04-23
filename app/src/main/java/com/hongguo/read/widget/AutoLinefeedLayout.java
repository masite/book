package com.hongguo.read.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hongguo.read.R;
import com.losg.library.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by losg on 2018/1/7.
 */

public class AutoLinefeedLayout extends ViewGroup {
    private int                mTextColor;
    private int                mTextStatusColor;
    private int                mTextBackgroundResource;
    private int                mTextSize;
    private int                mMarginSize;
    private int                mMinWidth;
    private LabelClickListener mLabelClickListener;
    private int                mMaxLine;
    private List<ChildPosition> mRowNumbers = new ArrayList<>();

    public AutoLinefeedLayout(Context context) {
        this(context, null);
    }

    public AutoLinefeedLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoLinefeedLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ty = context.obtainStyledAttributes(attrs, R.styleable.AutoLineLabelView);
        mTextColor = ty.getColor(R.styleable.AutoLineLabelView_label_textColor, 0xff000000);
        mTextSize = (int) ty.getDimension(R.styleable.AutoLineLabelView_label_textSize, 12);
        mTextStatusColor = ty.getResourceId(R.styleable.AutoLineLabelView_label_textStatusColor, 0);
        mMarginSize = (int) ty.getDimension(R.styleable.AutoLineLabelView_label_margin, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
        mMaxLine =  ty.getInt(R.styleable.AutoLineLabelView_label_maxLine, Integer.MAX_VALUE);
        setMarginSize(mMarginSize);
        mTextBackgroundResource = ty.getResourceId(R.styleable.AutoLineLabelView_label_backgroundResource, 0);
        mMinWidth = (int) ty.getDimension(R.styleable.AutoLineLabelView_label_minWidth, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics()));

        ty.recycle();
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
    }

    public void setTextBackgroundResource(int textBackgroundResource) {
        mTextBackgroundResource = textBackgroundResource;
    }

    public void setTextSize(int textSize) {
        mTextSize = textSize;
    }

    public void setMinWidth(int minWidth) {
        mMinWidth = minWidth;
    }

    public void setMarginSize(int marginSize) {
        mMarginSize = marginSize;
    }

    public void setLabelClickListener(LabelClickListener labelClickListener) {
        mLabelClickListener = labelClickListener;
    }

    public void setLables(String[] lables) {
        setLables(lables, null, false);
    }

    public void setLables(String[] lables, boolean colorful) {
        setLables(lables, null, colorful);
    }

    public void setLables(String[] lables, String[] tags, boolean colorful) {
        removeAllViews();
        for (int i = 0; i < lables.length; i++) {
            String label = lables[i];
            TextView textView = new TextView(getContext());
            textView.setClickable(true);
            textView.setOnClickListener(v -> {
                if (mLabelClickListener != null) {
                    mLabelClickListener.labelClick(v, label);
                }
            });
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
            if (tags != null)
                textView.setTag(tags[i]);
            if (mTextStatusColor != 0) {
                textView.setTextColor(getResources().getColorStateList(mTextStatusColor));
            } else
                textView.setTextColor(mTextColor);
            if (mTextBackgroundResource != 0)
                textView.setBackgroundResource(mTextBackgroundResource);
            else{
                int paddingLeft = DisplayUtil.dip2px(getContext(), 8);
                int paddingTop = DisplayUtil.dip2px(getContext(), 4);
                textView.setPadding(paddingLeft, paddingTop, paddingLeft, paddingTop);
            }
            if (colorful) {
                Drawable drawable = createRadomColorDrawable();
                textView.setBackground(drawable);
                int paddingLeft = DisplayUtil.dip2px(getContext(), 8);
                int paddingTop = DisplayUtil.dip2px(getContext(), 4);
                textView.setPadding(paddingLeft, paddingTop, paddingLeft, paddingTop);
            }
            textView.setGravity(Gravity.CENTER);
            textView.setText(label);
            if (mMinWidth != 0) {
                textView.setMinWidth(mMinWidth);
            }
            addView(textView, new LayoutParams(-2, -2));
        }
    }

    private Drawable createRadomColorDrawable() {
        Random random = new Random();
        int red = 100 + random.nextInt(100);
        int green = 100 + random.nextInt(100);
        int blue = 100 + random.nextInt(100);
        int color = Color.argb(255, red, green, blue);
        int radio = DisplayUtil.dip2px(getContext(), 4);
        ShapeDrawable shapeDrawable = new ShapeDrawable(new RoundRectShape(new float[]{radio, radio, radio, radio, radio, radio, radio, radio}, null, null));
        shapeDrawable.getPaint().setColor(color);
        return shapeDrawable;
    }


    public interface LabelClickListener {
        void labelClick(View view, String labelName);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (getChildCount() == 0) {
            super.onMeasure(widthMeasureSpec, 0);
        }
        // 获得它的父容器为它设置的测量模式和大小
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);

        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        sizeWidth -= paddingLeft;
        sizeWidth -= paddingRight;

        int currentWidth = paddingLeft;

        int currentHeight = paddingTop;

        int childCount = getChildCount();

        int lastHeight = 0;

        int line = 0;

        ChildPosition rowInfo = null;

        mRowNumbers.clear();

        // 遍历每个子元素
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            // 测量每一个child的宽和高
            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            // 当前子空间实际占据的宽度
            int childWidth = child.getMeasuredWidth();
            // 当前子空间实际占据的高度
            int childHeight = child.getMeasuredHeight();

            rowInfo = new ChildPosition();
            //换行
            if (currentWidth + childWidth > sizeWidth) {
                line ++;
                currentHeight += lastHeight + mMarginSize;
                currentWidth = paddingLeft;
                rowInfo.left = currentWidth;
                rowInfo.top = currentHeight;
                rowInfo.right = rowInfo.left + childWidth;
                rowInfo.bottom = rowInfo.top + childHeight;
                lastHeight = childHeight;
                currentWidth += childWidth + mMarginSize;
                if(line >= mMaxLine){
                    break;
                }
            } else {
                rowInfo.left = currentWidth;
                rowInfo.top = currentHeight;
                rowInfo.right = rowInfo.left + childWidth;
                rowInfo.bottom = rowInfo.top + childHeight;
                currentWidth += childWidth + mMarginSize;
                lastHeight = childHeight;
            }
            mRowNumbers.add(rowInfo);
        }
        setMeasuredDimension(sizeWidth, mRowNumbers.size() > 0 ? mRowNumbers.get(mRowNumbers.size() - 1).bottom + paddingBottom : paddingTop + paddingBottom);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (getChildCount() == 0) return;
        for (int i = 0; i < mRowNumbers.size(); i++) {
            ChildPosition childPosition = mRowNumbers.get(i);
            getChildAt(i).layout(childPosition.left, childPosition.top, childPosition.right, childPosition.bottom);
        }
    }


    private static class ChildPosition {
        public int left;
        public int top;
        public int right;
        public int bottom;
    }
}
