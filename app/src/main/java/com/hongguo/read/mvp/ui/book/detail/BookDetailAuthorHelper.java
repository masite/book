package com.hongguo.read.mvp.ui.book.detail;

import android.content.Context;

import com.hongguo.common.widget.recycler.IosRecyclerAdapter;
import com.hongguo.read.R;
import com.hongguo.read.mvp.model.book.detail.BookAuthorBean;


/**
 * Created by losg on 2017/12/26.
 */

public class BookDetailAuthorHelper {

    private Context        mContext;
    private BookAuthorBean mBookAuthorBean;
    private int            mBookType;

    public BookDetailAuthorHelper(Context context, int bookType) {
        mContext = context;
        mBookType = bookType;
    }

    public int getTileResouce() {
        return 0;
    }

    public void initTitle(IosRecyclerAdapter.BaseHoder hoder) {
    }

    public int getCellCount() {
        return 1;
    }

    public int getItemResouce() {
        return R.layout.view_book_detail_author;
    }

    public void initBookAuthor(IosRecyclerAdapter.BaseHoder hoder, int position) {
        if (mBookAuthorBean == null) return;
        hoder.setText(R.id.book_author, "版本来源:" + mBookAuthorBean.data.fromName);
//        if(mBookType == Constants.BOOK_FROM.FROM_BAIDU){
//            hoder.setText(R.id.author_describe, "本书由百度文学授权本软件使用，若包括不良信息，请及时告知客服，请支持正版阅读。");
//        }
    }

    public void setBookAuthorBean(BookAuthorBean bookAuthorBean) {
        mBookAuthorBean = bookAuthorBean;
    }
}
