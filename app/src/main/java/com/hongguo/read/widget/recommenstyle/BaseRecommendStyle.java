package com.hongguo.read.widget.recommenstyle;

import android.content.Context;
import android.view.View;

import com.hongguo.read.mvp.model.bookstore.RecommendItemBean;

/**
 * Created by losg
 */

public class BaseRecommendStyle {

    protected RcommendItemClickListener mRcommendItemClickListener;
    protected Context                   mContext;
    protected RecommendItemBean.ObjBean mBookStoreItem;

    public BaseRecommendStyle(Context context) {
        mContext = context;
    }

    public int getTitleResource() {
        return 0;
    }

    public int getItemResource() {
        return 0;
    }

    public int getItemCount(){
        return 0;
    }

    public void initTitleView(View titleView) {

    }

    public void initItemView(View itemView, int position) {

    }

    public void setRcommendItemClickListener(RcommendItemClickListener rcommendItemClickListener) {
        mRcommendItemClickListener = rcommendItemClickListener;
    }

    public void setBookStoreItem(RecommendItemBean.ObjBean bookStoreItems) {
        mBookStoreItem = bookStoreItems;
    }
}
