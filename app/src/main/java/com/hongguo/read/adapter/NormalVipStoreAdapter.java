package com.hongguo.read.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.hongguo.common.utils.StatisticsUtils;
import com.hongguo.read.R;
import com.hongguo.read.mvp.model.vip.VipBean;
import com.hongguo.read.mvp.ui.book.detail.BookDetailActivity;
import com.hongguo.common.utils.imageLoad.ImageLoadUtils;
import com.hongguo.common.widget.recycler.IosRecyclerAdapter;

import java.util.List;


/**
 * Created time 2017/11/27.
 *
 * @author losg
 */

public class NormalVipStoreAdapter extends IosRecyclerAdapter {

    private List<VipBean.DataBean> mItems;

    public NormalVipStoreAdapter(Context context, List<VipBean.DataBean> items) {
        super(context);
        mItems = items;
    }

    @Override
    protected int getContentResource(int areaPosition) {
        return R.layout.adapter_normal_vip_store_item;
    }

    @Override
    protected void initContentView(BaseHoder hoder, int areaPosition, int index) {
        VipBean.DataBean item = mItems.get(index);
        hoder.setText(R.id.book_title, item.name);
        hoder.setText(R.id.book_auther, "作者：" + item.author);
        hoder.setText(R.id.book_descibe, item.summary);
        ImageView imageView = hoder.getViewById(R.id.book_image);
        ImageLoadUtils.loadUrl(imageView, item.cover);
        hoder.itemView.setOnClickListener(v -> {
            StatisticsUtils.collect("VIP专区", item.name);
            StatisticsUtils.vip(item.name);
            BookDetailActivity.toActivity(mContext, item.id, item.bookType);
        });
    }

    @Override
    protected int getAreaSize() {
        return 1;
    }

    @Override
    protected int getCellCount(int areaPosition) {
        return mItems.size();
    }

    public void setItems(List<VipBean.DataBean> items) {
        mItems = items;
    }
}
