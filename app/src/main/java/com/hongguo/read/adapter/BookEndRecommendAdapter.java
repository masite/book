package com.hongguo.read.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.hongguo.common.utils.imageLoad.ImageLoadUtils;
import com.hongguo.common.widget.recycler.IosRecyclerAdapter;
import com.hongguo.read.R;
import com.hongguo.read.mvp.model.book.detail.SimilarBookBean;
import com.hongguo.read.mvp.ui.book.detail.BookDetailActivity;

import java.util.List;

/**
 * Created by losg on 2018/4/12.
 */

public class BookEndRecommendAdapter extends IosRecyclerAdapter {

    private List<SimilarBookBean.DataBean> mDataBeans;

    public BookEndRecommendAdapter(Context context, List<SimilarBookBean.DataBean> dataBeans) {
        super(context);
        mDataBeans = dataBeans;
    }

    @Override
    protected int getContentResource(int areaPosition) {
        return R.layout.view_end_recommend_item;
    }

    @Override
    protected void initContentView(BaseHoder hoder, int areaPosition, int index) {
        SimilarBookBean.DataBean dataBean = mDataBeans.get(index);
        ImageView imageView = hoder.getViewById(R.id.book_cover);
        ImageLoadUtils.loadUrl(imageView, dataBean.coverPicture);
        hoder.setText(R.id.book_name, dataBean.bookName);

        hoder.itemView.setOnClickListener(v -> {
            BookDetailActivity.toActivity(mContext, dataBean.bookId, dataBean.bookType);
        });
    }

    @Override
    protected int getAreaSize() {
        return 1;
    }

    @Override
    protected int getCellCount(int areaPosition) {
        return mDataBeans.size() > 6 ? 6 : mDataBeans.size();
    }
}
