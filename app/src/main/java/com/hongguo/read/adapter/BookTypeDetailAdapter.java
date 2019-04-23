package com.hongguo.read.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hongguo.common.utils.imageLoad.ImageLoadUtils;
import com.hongguo.read.R;
import com.hongguo.read.mvp.model.bookstore.BookStoreBookBean;
import com.hongguo.read.mvp.ui.book.detail.BookDetailActivity;
import com.hongguo.common.widget.recycler.IosRecyclerAdapter;
import com.losg.library.widget.loading.BaLoadingView;

import java.util.List;


/**
 * Created by losg on 17-12-12.
 */

public class BookTypeDetailAdapter extends IosRecyclerAdapter {

    private List<BookStoreBookBean.DataBean> mDataBeans;
    private BaLoadingView.LoadingStatus mLoadingStatus = BaLoadingView.LoadingStatus.LOADING;


    public BookTypeDetailAdapter(Context context, List<BookStoreBookBean.DataBean> dataBeans) {
        super(context);
        mDataBeans = dataBeans;
    }

    @Override
    protected int getContentResource(int areaPosition) {
        return R.layout.adapter_search_item;
    }

    @Override
    protected void initContentView(BaseHoder hoder, int areaPosition, int index) {
        BookStoreBookBean.DataBean dataBean = mDataBeans.get(index);

        ImageView bookImage = hoder.getViewById(R.id.book_cover);
        ImageLoadUtils.loadUrl(bookImage, dataBean.coverPicture);

        TextView bookName = hoder.getViewById(R.id.book_name);
        bookName.setText(dataBean.bookName);

        TextView bookDescribe = hoder.getViewById(R.id.book_describe);
        bookDescribe.setText(dataBean.desc);

        TextView bookAuther = hoder.getViewById(R.id.book_author);
        bookAuther.setText("作者: " + dataBean.author);

        TextView bookType = hoder.getViewById(R.id.book_type);
        if (!TextUtils.isEmpty(dataBean.categoryName)) {
            bookType.setText(dataBean.categoryName);
            bookType.setVisibility(View.VISIBLE);
        } else {
            bookType.setVisibility(View.GONE);
        }

        hoder.itemView.setOnClickListener(v -> {
            BookDetailActivity.toActivity(mContext, dataBean.bookId, dataBean.bookType);
        });

    }

    public void setDataBeans(List<BookStoreBookBean.DataBean> dataBeans) {
        mDataBeans = dataBeans;
    }

    @Override
    protected int getAreaSize() {
        return 1;
    }

    @Override
    protected int getCellCount(int areaPosition) {
        if (mLoadingStatus != BaLoadingView.LoadingStatus.LADING_SUCCESS) {
            return 0;
        }
        return mDataBeans.size();
    }


    public void setLoadingStatus(BaLoadingView.LoadingStatus loadingStatus) {
        mLoadingStatus = loadingStatus;
        notifyChange();
    }
}
