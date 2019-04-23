package com.hongguo.read.adapter;

import android.content.Context;

import com.hongguo.read.R;
import com.hongguo.read.mvp.model.search.SearchBean;
import com.hongguo.read.mvp.ui.book.detail.BookDetailActivity;
import com.hongguo.common.utils.imageLoad.ImageLoadUtils;
import com.hongguo.common.widget.recycler.IosRecyclerAdapter;

import java.util.List;

/**
 * Created by losg on 2018/1/12.
 */

public class SearchAdapter extends IosRecyclerAdapter {

    private List<SearchBean.DataBean> mDataBeans;

    public SearchAdapter(Context context, List<SearchBean.DataBean> dataBeans) {
        super(context);
        mDataBeans = dataBeans;
    }

    @Override
    protected int getContentResource(int areaPosition) {
        return R.layout.adapter_search_item;
    }

    @Override
    protected void initContentView(BaseHoder hoder, int areaPosition, int index) {
        SearchBean.DataBean dataBean = mDataBeans.get(index);
        ImageLoadUtils.loadUrl(hoder.getViewById(R.id.book_cover), dataBean.coverPicture);
        hoder.setText(R.id.book_name, dataBean.bookName);
        hoder.setText(R.id.book_describe, dataBean.desc);
        hoder.setText(R.id.book_author, "作者 "+dataBean.author);
        hoder.setText(R.id.book_type, dataBean.categoryName);
        hoder.itemView.setOnClickListener(v->{
            BookDetailActivity.toActivity(mContext, dataBean.bookId, dataBean.bookFrom);
        });
    }

    @Override
    protected int getAreaSize() {
        return 1;
    }

    @Override
    protected int getCellCount(int areaPosition) {
        return mDataBeans.size();
    }

    public void setDataBeans(List<SearchBean.DataBean> dataBeans) {
        mDataBeans = dataBeans;
    }
}
