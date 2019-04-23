package com.hongguo.read.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hongguo.read.R;

/**
 * Created by losg on 2017/6/15.
 */

public class CommonThreeItem extends LinearLayout implements View.OnClickListener {

    private LoadImage         mLoadImage;
    private ItemClickListener mItemClickListener;
    private LinearLayout      mRootView;


    public CommonThreeItem(Context context) {
        this(context, null);
    }

    public CommonThreeItem(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonThreeItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View.inflate(getContext(), R.layout.view_thiree_picture, this);
        mRootView = (LinearLayout) getChildAt(0);
        mRootView.getChildAt(0).setOnClickListener(this);
        mRootView.getChildAt(1).setOnClickListener(this);
        mRootView.getChildAt(2).setOnClickListener(this);
    }

    public void setLines(int lines) {
        ((TextView) ((LinearLayout) mRootView.getChildAt(0)).getChildAt(1)).setLines(lines);
        ((TextView) ((LinearLayout) mRootView.getChildAt(1)).getChildAt(1)).setLines(lines);
        ((TextView) ((LinearLayout) mRootView.getChildAt(2)).getChildAt(1)).setLines(lines);
    }

    public void setPositionVisiable(int position, boolean visiable) {
        mRootView.getChildAt(position).setVisibility(visiable ? VISIBLE : INVISIBLE);
        mRootView.getChildAt(position).setEnabled(visiable);
    }

    public void setName(int position, String text) {
        ((TextView) ((LinearLayout) mRootView.getChildAt(position)).getChildAt(1)).setText(text);
    }

    public void setImageUrl(int position, String url) {
        if (mLoadImage != null) {
            mLoadImage.loadImage((ImageView) ((LinearLayout) mRootView.getChildAt(position)).getChildAt(0), url);
        }
    }

    public void setLoadImage(LoadImage loadImage) {
        mLoadImage = loadImage;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        if (mItemClickListener == null) {
            return;
        }
        if (v == mRootView.getChildAt(0)) {
            mItemClickListener.click(0);
        } else if (v == mRootView.getChildAt(1)) {
            mItemClickListener.click(1);
        } else if (v == mRootView.getChildAt(2)) {
            mItemClickListener.click(2);
        }
    }

    public interface LoadImage {
        void loadImage(ImageView imageView, String url);
    }

    public interface ItemClickListener {
        void click(int position);
    }

}
