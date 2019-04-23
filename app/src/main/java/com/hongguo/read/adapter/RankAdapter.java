package com.hongguo.read.adapter;

import android.content.Context;

import com.hongguo.common.utils.imageLoad.ImageLoadUtils;
import com.hongguo.common.widget.recycler.IosRecyclerAdapter;
import com.hongguo.read.R;
import com.hongguo.read.mvp.model.search.RankBean;
import com.hongguo.read.mvp.ui.book.detail.BookDetailActivity;

import java.util.List;

/**
 * Created by losg on 2018/1/12.
 */

public class RankAdapter extends IosRecyclerAdapter {

    private List<RankBean.DataBean> mDataBeans;

    public RankAdapter(Context context, List<RankBean.DataBean> dataBeans) {
        super(context);
        mDataBeans = dataBeans;
    }

    @Override
    protected int getContentResource(int areaPosition) {
        return R.layout.adapter_rank_item;
    }

    @Override
    protected void initContentView(BaseHoder hoder, int areaPosition, int index) {
        RankBean.DataBean dataBean = mDataBeans.get(index);
        ImageLoadUtils.loadUrl(hoder.getViewById(R.id.book_cover), dataBean.cover);
        hoder.setText(R.id.book_name, dataBean.name);
        hoder.setText(R.id.book_describe, dataBean.descript);
        hoder.setText(R.id.book_author, "作者 "+dataBean.authorName);
        hoder.setText(R.id.book_type, dataBean.categoryName);
        hoder.itemView.setOnClickListener(v->{
            BookDetailActivity.toActivity(mContext, dataBean.iD, dataBean.bookFrom);
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
}
