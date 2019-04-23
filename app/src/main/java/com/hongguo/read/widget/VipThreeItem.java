package com.hongguo.read.widget;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hongguo.read.R;
import com.losg.library.utils.stylestring.StyleStringBuilder;

/**
 * @author losg
 * @date 2017/6/15
 */

public class VipThreeItem extends LinearLayout implements View.OnClickListener {

    private LoadImage         mLoadImage;
    private ItemClickListener mItemClickListener;
    private LinearLayout      mRootView;


    public VipThreeItem(Context context) {
        this(context, null);
    }

    public VipThreeItem(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VipThreeItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View.inflate(getContext(), R.layout.view_thiree_super_vip_item, this);
        mRootView = (LinearLayout) getChildAt(0);
        mRootView.getChildAt(0).setOnClickListener(this);
        mRootView.getChildAt(1).setOnClickListener(this);
        mRootView.getChildAt(2).setOnClickListener(this);
    }


    public void setPositionVisiable(int position, boolean visiable) {
        if (visiable) {
            mRootView.getChildAt(position).setVisibility(VISIBLE);
            mRootView.getChildAt(position).setEnabled(true);
        } else {
            mRootView.getChildAt(position).setVisibility(INVISIBLE);
            mRootView.getChildAt(position).setEnabled(false);
        }
    }

    public void setName(int position, String bookName, String price) {
        ((TextView) ((LinearLayout) mRootView.getChildAt(position)).getChildAt(1)).setText(bookName);
        StyleStringBuilder styleStringBuilder = new StyleStringBuilder();
        ((TextView) ((LinearLayout) mRootView.getChildAt(position)).getChildAt(2)).setText("售价 "+price +" 元");
        ((TextView) ((LinearLayout) mRootView.getChildAt(position)).getChildAt(2)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }

    public void setImageUrl(int position, String url) {
        if (mLoadImage != null) {
            mLoadImage.loadImage((ImageView) ((FrameLayout) ((LinearLayout) mRootView.getChildAt(position)).getChildAt(0)).getChildAt(0), url);
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
