package com.hongguo.read.widget.recommenstyle;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.hongguo.read.R;
import com.hongguo.read.mvp.model.bookstore.RecommendItemBean;
import com.hongguo.read.utils.BookNumberUtils;
import com.hongguo.common.utils.StatisticsUtils;
import com.hongguo.common.utils.imageLoad.ImageLoadUtils;
import com.hongguo.read.widget.recommenstyle.scrollbook.ScrollBookView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by losg
 */

public class RecommendStyleFourManager extends BaseRecommendStyle {

    private ScrollBookView mScrollBookView;
    private TextView       mTitle;
    private TextView       mBookDescribe;
    private TextView       mBookType;
    private TextView       mBookAuther;
    private TextView       mBookWords;

    public RecommendStyleFourManager(Context context) {
        super(context);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public int getTitleResource() {
        return R.layout.view_recommend_style_four;
    }

    @Override
    public void initTitleView(View titleView) {
        mScrollBookView = (ScrollBookView) titleView.findViewById(R.id.book_scroll);
        mTitle = (TextView) titleView.findViewById(R.id.title);
        mBookDescribe = (TextView) titleView.findViewById(R.id.book_describe);
        mBookType = (TextView) titleView.findViewById(R.id.book_type);
        mBookAuther = (TextView) titleView.findViewById(R.id.book_author);
        mBookWords = (TextView) titleView.findViewById(R.id.book_words);

        mTitle.setText(mBookStoreItem.dataname);
        List<String> urls = new ArrayList<>();
        for (int i = 0; i < mBookStoreItem.data.size(); i++) {
            RecommendItemBean.ObjBean.DataBean dataBean = mBookStoreItem.data.get(i);
            urls.add(dataBean.coverPicture);
        }

        RecommendItemBean.ObjBean.DataBean firstBean = mBookStoreItem.data.get(0);
        mBookDescribe.setText(firstBean.desc);
        mBookType.setText(firstBean.categoryName);
        if (TextUtils.isEmpty(firstBean.categoryName)) {
            mBookType.setVisibility(View.GONE);
        } else {
            mBookType.setVisibility(View.VISIBLE);
        }
        mBookAuther.setText(firstBean.author);

        if(!TextUtils.isEmpty(firstBean.wordsText)){
            mBookWords.setText(firstBean.wordsText);
        }else {
            mBookWords.setText(BookNumberUtils.formartWords(firstBean.words));
        }

        mScrollBookView.setAutoPlay(true);
        mScrollBookView.setImageLoader((imageView, path) -> ImageLoadUtils.loadUrl(imageView, path));
        //点击
        mScrollBookView.setOnScrollBookViewClickListener(position -> {
            RecommendItemBean.ObjBean.DataBean dataBean = mBookStoreItem.data.get(position);
            if (mRcommendItemClickListener != null) {
                mRcommendItemClickListener.clicked(dataBean.bookId, dataBean.bookfrom);
                StatisticsUtils.bookstore("点击" + mBookStoreItem.dataname);
            }
        });

        //位置变化
        mScrollBookView.setScrollBookChangeListener(position -> {
            RecommendItemBean.ObjBean.DataBean child = mBookStoreItem.data.get(position);
            mBookDescribe.setText(child.desc);
            mBookType.setText(child.categoryName);
            if (TextUtils.isEmpty(child.categoryName)) {
                mBookType.setVisibility(View.GONE);
            } else {
                mBookType.setVisibility(View.VISIBLE);
            }
            mBookAuther.setText(child.author);
            if(!TextUtils.isEmpty(child.wordsText)){
                mBookWords.setText(child.wordsText);
            }else {
                mBookWords.setText(BookNumberUtils.formartWords(child.words));
            }
        });
        mScrollBookView.setLoadUrls(urls);


        if (!TextUtils.isEmpty(mBookStoreItem.systemMore)) {
            titleView.findViewById(R.id.more).setVisibility(View.VISIBLE);
            titleView.findViewById(R.id.more).setOnClickListener(v -> {
            });
        } else {
            titleView.findViewById(R.id.more).setVisibility(View.GONE);
        }
    }

}
