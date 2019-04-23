package com.hongguo.read.widget.recommenstyle;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hongguo.read.R;
import com.hongguo.read.mvp.model.bookstore.RecommendItemBean;
import com.hongguo.common.utils.StatisticsUtils;

/**
 * Created by losg
 */

public class RecommendStyleTwoManager extends BaseRecommendStyle {

    private TextView     mTitle;
    private LinearLayout mBookContent;


    public RecommendStyleTwoManager(Context context) {
        super(context);
    }

    @Override
    public int getTitleResource() {
        return R.layout.view_recommend_style_two;
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public void initTitleView(View titleView) {
        mBookContent = (LinearLayout) titleView.findViewById(R.id.book_content);
        mTitle = (TextView) titleView.findViewById(R.id.title);

        mTitle.setText(mBookStoreItem.dataname);
        for (int i = 0; i < mBookStoreItem.data.size(); i++) {
            RecommendItemBean.ObjBean.DataBean dataBean = mBookStoreItem.data.get(i);
            int row = i / 3;
            if (row >= 2) return;
            CardBookView bookView = (CardBookView) ((LinearLayout) mBookContent.getChildAt(row)).getChildAt(i % 3);
            bookView.setBookUrlAndName(dataBean.coverPicture, dataBean.bookName);
            bookView.setOnClickListener(v -> {
                if (mRcommendItemClickListener != null) {
                    mRcommendItemClickListener.clicked(dataBean.bookId, dataBean.bookfrom);
                    StatisticsUtils.bookstore("点击" + mBookStoreItem.dataname);
                }
            });
        }

        if (!TextUtils.isEmpty(mBookStoreItem.systemMore)) {
            titleView.findViewById(R.id.more).setVisibility(View.VISIBLE);
            titleView.findViewById(R.id.more).setOnClickListener(v -> {
            });
        } else {
            titleView.findViewById(R.id.more).setVisibility(View.GONE);
        }
    }


}
