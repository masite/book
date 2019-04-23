package com.hongguo.read.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hongguo.common.utils.StatisticsUtils;
import com.hongguo.common.utils.imageLoad.ImageLoadUtils;
import com.hongguo.common.widget.recycler.IosRecyclerAdapter;
import com.hongguo.read.R;
import com.hongguo.read.mvp.model.vip.VipBean;
import com.hongguo.read.mvp.ui.book.detail.BookDetailActivity;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created time 2017/11/27.
 *
 * @author losg
 */

public class SuperVipStoreAdapter extends IosRecyclerAdapter {

    private List<VipBean.DataBean> mItems;
    private DecimalFormat          mDecimalFormat;

    public SuperVipStoreAdapter(Context context, List<VipBean.DataBean> items) {
        super(context);
        mItems = items;
        mDecimalFormat = new DecimalFormat("#.#");
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

        TextView price = hoder.getViewById(R.id.price);
        price.setVisibility(View.VISIBLE);

        ImageView imageView = hoder.getViewById(R.id.book_image);
        ImageLoadUtils.loadUrl(imageView, item.cover);
        String priceText = mDecimalFormat.format(item.words / 20000f);

        price.setText("售价 " + priceText + " 元");
        price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

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
