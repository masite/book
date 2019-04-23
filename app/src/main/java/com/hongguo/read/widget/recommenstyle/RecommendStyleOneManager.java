package com.hongguo.read.widget.recommenstyle;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hongguo.read.R;
import com.hongguo.read.mvp.model.bookstore.RecommendItemBean;
import com.hongguo.read.utils.BookNumberUtils;
import com.hongguo.common.utils.StatisticsUtils;
import com.hongguo.common.utils.imageLoad.ImageLoadUtils;

/**
 * Created by losg
 */

public class RecommendStyleOneManager extends BaseRecommendStyle {

    public RecommendStyleOneManager(Context context) {
        super(context);
    }


    @Override
    public int getTitleResource() {
        return R.layout.view_recommend_style_one_title;
    }

    @Override
    public int getItemResource() {
        return R.layout.view_recommend_style_one_item;
    }

    @Override
    public int getItemCount() {
        return (mBookStoreItem.data.size() - 1) / 3;
    }

    @Override
    public void initTitleView(View titleView) {
        TextView title = (TextView) titleView.findViewById(R.id.title);
        title.setText(mBookStoreItem.dataname);

        RecommendItemBean.ObjBean.DataBean dataBean = mBookStoreItem.data.get(0);

        ImageView bookImage = (ImageView) titleView.findViewById(R.id.book_image);
        ImageLoadUtils.loadUrl(bookImage, dataBean.coverPicture);

        TextView bookName = (TextView) titleView.findViewById(R.id.book_name);
        bookName.setText(dataBean.bookName);

        TextView bookAuthor = (TextView) titleView.findViewById(R.id.book_author);
        bookAuthor.setText(dataBean.author);

        TextView bookDescribe = (TextView) titleView.findViewById(R.id.book_describe);
        bookDescribe.setText(dataBean.desc);

        TextView bookType = (TextView) titleView.findViewById(R.id.book_type);
        if (TextUtils.isEmpty(dataBean.categoryName)) {
            bookType.setVisibility(View.GONE);
        } else {
            bookType.setVisibility(View.VISIBLE);
        }
        bookType.setText(dataBean.categoryName);

        TextView bookWords = (TextView) titleView.findViewById(R.id.book_words);
        if (!TextUtils.isEmpty(dataBean.wordsText)) {
            bookWords.setText(dataBean.wordsText);
        } else {
            bookWords.setText(BookNumberUtils.formartWords(dataBean.words));
        }

        titleView.setOnClickListener(v -> {
            if (mRcommendItemClickListener != null) {
                mRcommendItemClickListener.clicked(dataBean.bookId, dataBean.bookfrom);
                StatisticsUtils.bookstore("点击" + mBookStoreItem.dataname);
            }
        });

        if (!TextUtils.isEmpty(mBookStoreItem.systemMore)) {
            titleView.findViewById(R.id.more).setVisibility(View.VISIBLE);
            titleView.findViewById(R.id.more).setOnClickListener(v -> {
            });
        } else {
            titleView.findViewById(R.id.more).setVisibility(View.GONE);
        }
    }

    @Override
    public void initItemView(View itemView, int position) {
        int start = position * 3 + 1;
        for (int i = 0; i < 3; i++) {
            RecommendItemBean.ObjBean.DataBean dataBean = mBookStoreItem.data.get(start + i);
            ImageView bookImage = (ImageView) ((LinearLayout) ((LinearLayout) ((LinearLayout) itemView).getChildAt(0)).getChildAt(i)).getChildAt(0);
            TextView bookName = (TextView) ((LinearLayout) ((LinearLayout) ((LinearLayout) itemView).getChildAt(0)).getChildAt(i)).getChildAt(1);
            ImageLoadUtils.loadUrl(bookImage, dataBean.coverPicture);
            bookName.setText(dataBean.bookName);
            ((LinearLayout) ((LinearLayout) itemView).getChildAt(0)).getChildAt(i).setOnClickListener(v -> {
                if (mRcommendItemClickListener != null) {
                    mRcommendItemClickListener.clicked(dataBean.bookId, dataBean.bookfrom);
                    StatisticsUtils.bookstore("点击" + mBookStoreItem.dataname);
                }
            });
        }
    }
}
