package com.hongguo.read.widget;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hongguo.read.R;

public class DownChapterMenuPop extends PopupWindow implements View.OnClickListener {

    private Context              mContext;
    private int                  mArrowHeight;
    private TextView             mAllChapter;     //关于页面按钮
    private TextView             mAllFree;  //反馈页面按钮
    private TextView             mAllNeedPay;     //分享页面按钮
    private PopItemClickListener mpopItemClickListener;

    public DownChapterMenuPop(Context context) {
        super(-2, -2);
        mContext = context;
        initView();
    }

    private void initView() {
        View view = View.inflate(mContext, R.layout.pop_download_setting, null);
        View content = view.findViewById(R.id.menu_content);
        mAllChapter = (TextView) view.findViewById(R.id.all_chapter);
        mAllFree = (TextView) view.findViewById(R.id.all_free);
        mAllNeedPay = (TextView) view.findViewById(R.id.all_need_pay);
        mAllChapter.setOnClickListener(this);
        mAllFree.setOnClickListener(this);
        mAllNeedPay.setOnClickListener(this);
        content.getLayoutParams().width = mContext.getResources().getDisplayMetrics().widthPixels / 3;
        setContentView(view);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setOutsideTouchable(true);
        Drawable drawable = mContext.getResources().getDrawable(R.mipmap.ic_pop_arrow);
        mArrowHeight = drawable.getIntrinsicHeight();
    }

    public int getArrowHeight() {
        return mArrowHeight;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.all_chapter:
                if (mpopItemClickListener != null) {
                    mpopItemClickListener.allChapter();
                }
                break;
            case R.id.all_free:
                if (mpopItemClickListener != null) {
                    mpopItemClickListener.allFree();
                }
                break;
            case R.id.all_need_pay:
                if (mpopItemClickListener != null) {
                    mpopItemClickListener.allNeedPay();
                }
                break;
        }
        this.dismiss();
    }

    public void setMpopItemClickListener(PopItemClickListener mpopItemClickListener) {
        this.mpopItemClickListener = mpopItemClickListener;
    }

    public interface PopItemClickListener {
        void allChapter();

        void allFree();

        void allNeedPay();
    }
}
