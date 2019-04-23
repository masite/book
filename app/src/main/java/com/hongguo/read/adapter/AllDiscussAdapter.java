package com.hongguo.read.adapter;

import android.content.Context;

import com.hongguo.common.widget.recycler.IosRecyclerAdapter;
import com.hongguo.read.R;
import com.hongguo.read.mvp.model.book.detail.BookDiscussBean;
import com.hongguo.read.mvp.ui.book.detail.BookDetailDiscussHelper;
import com.losg.library.widget.loading.BaLoadingView;

/**
 * Created by losg on 2017/12/29.
 */

public class AllDiscussAdapter extends IosRecyclerAdapter {

    private BookDetailDiscussHelper     mBookDetailDiscussHelper;
    private BaLoadingView.LoadingStatus mLoadingStatus = BaLoadingView.LoadingStatus.LOADING;

    public AllDiscussAdapter(Context context, String bookId) {
        super(context);
        mBookDetailDiscussHelper = new BookDetailDiscussHelper(mContext, bookId);
    }

    @Override
    protected int getCellTitleResource(int areaPosition) {
        return R.layout.adapter_item_row_line;
    }

    @Override
    protected int getContentResource(int areaPosition) {
        return mBookDetailDiscussHelper.getItemResouce();
    }

    @Override
    protected void initContentView(BaseHoder hoder, int areaPosition, int index) {
        mBookDetailDiscussHelper.initBookDiscuss(hoder, index);
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
        return mBookDetailDiscussHelper.getCellCount();
    }

    public void setBookDiscussBean(BookDiscussBean bookDiscussBean) {
        mBookDetailDiscussHelper.setBookDiscussBean(bookDiscussBean);
    }

    public void setLoadingStatus(BaLoadingView.LoadingStatus loadingStatus) {
        mLoadingStatus = loadingStatus;
        notifyChange();
    }
}
