package com.hongguo.read.widget.recommenstyle;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hongguo.read.R;
import com.hongguo.read.mvp.model.bookstore.RecommendItemBean;
import com.hongguo.read.mvp.ui.booktype.BookTypeWithoutSelectActivity;
import com.hongguo.read.utils.BookNumberUtils;
import com.hongguo.common.utils.StatisticsUtils;
import com.hongguo.common.utils.imageLoad.ImageLoadUtils;

/**
 * Created by losg
 *
 * @author losg
 */

public class RecommendStyleFiveManager extends BaseRecommendStyle {

    public RecommendStyleFiveManager(Context context) {
        super(context);
    }

    @Override
    public int getTitleResource() {
        if (mBookStoreItem == null) return 0;
        return R.layout.view_recommend_style_five_title;
    }

    @Override
    public int getItemResource() {
        if (mBookStoreItem == null) return 0;
        return R.layout.view_recommend_style_five_item;
    }

    @Override
    public int getItemCount() {
        if (mBookStoreItem == null) return 0;
        return mBookStoreItem.data.size() - 1;
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

        TextView bookDescribe = (TextView) titleView.findViewById(R.id.book_describe);
        bookDescribe.setText(dataBean.desc);

        TextView bookAuther = (TextView) titleView.findViewById(R.id.book_author);
        bookAuther.setText("作者: " + dataBean.author);

        TextView bookType = (TextView) titleView.findViewById(R.id.book_type);
        bookType.setText(dataBean.categoryName);
        if (TextUtils.isEmpty(dataBean.categoryName)) {
            bookType.setVisibility(View.GONE);
        } else {
            bookType.setVisibility(View.VISIBLE);
        }

        TextView bookWords = (TextView) titleView.findViewById(R.id.book_words);
        if (dataBean.words == 0 && TextUtils.isEmpty(dataBean.wordsText)) {
            bookWords.setVisibility(View.GONE);
        } else {
            bookWords.setVisibility(View.VISIBLE);
        }

        if(!TextUtils.isEmpty(dataBean.wordsText)){
            bookWords.setText(dataBean.wordsText);
        }else {
            bookWords.setText(BookNumberUtils.formartWords(dataBean.words));
        }

        titleView.findViewById(R.id.book_item).setOnClickListener(v -> {
            if (mRcommendItemClickListener != null) {
                mRcommendItemClickListener.clicked(dataBean.bookId, dataBean.bookfrom);
                StatisticsUtils.bookstore("点击" + mBookStoreItem.dataname);
            }
        });

        if (!TextUtils.isEmpty(mBookStoreItem.systemMore)) {
            titleView.findViewById(R.id.more).setVisibility(View.VISIBLE);
            titleView.findViewById(R.id.more).setOnClickListener(v -> {
                BookTypeWithoutSelectActivity.toActivity(mContext, mBookStoreItem.dataname, mBookStoreItem.systemMore, mBookStoreItem.status, mBookStoreItem.sort);
            });
        } else {
            titleView.findViewById(R.id.more).setVisibility(View.GONE);
        }
    }

    @Override
    public void initItemView(View itemView, int position) {

        RecommendItemBean.ObjBean.DataBean dataBean = mBookStoreItem.data.get(position);

        TextView bookName = (TextView) itemView.findViewById(R.id.book_name);
        bookName.setText(dataBean.bookName);

        TextView bookDescribe = (TextView) itemView.findViewById(R.id.book_describe);
        bookDescribe.setText(dataBean.desc);

        TextView bookAuther = (TextView) itemView.findViewById(R.id.book_author);
        bookAuther.setText("作者: " + dataBean.author);

        TextView bookType = (TextView) itemView.findViewById(R.id.book_type);
        bookType.setText(dataBean.categoryName);

        TextView bookWords = (TextView) itemView.findViewById(R.id.book_words);
        if (dataBean.words == 0) {
            bookWords.setVisibility(View.GONE);
        } else {
            bookWords.setVisibility(View.VISIBLE);
        }
        bookWords.setText(BookNumberUtils.formartWords(dataBean.words));

        itemView.setOnClickListener(v -> {
            if (mRcommendItemClickListener != null) {
                mRcommendItemClickListener.clicked(dataBean.bookId, dataBean.bookfrom);
                StatisticsUtils.bookstore("点击" + mBookStoreItem.dataname);
            }
        });
    }
}
