package com.hongguo.read.adapter;

import android.content.Context;

import com.hongguo.read.R;
import com.hongguo.read.mvp.model.mine.mypackage.ConsumeBean;
import com.hongguo.common.widget.recycler.IosRecyclerAdapter;

import java.util.List;

/**
 * Created by losg on 2018/1/4.
 */

public class ConsumeAdapter extends IosRecyclerAdapter {

    public List<ConsumeBean.DataBean> mDataBeans;

    public ConsumeAdapter(Context context, List<ConsumeBean.DataBean> dataBeans) {
        super(context);
        mDataBeans = dataBeans;
    }

    @Override
    protected int getCellTitleResource(int areaPosition) {
        return R.layout.adapter_consume_title;
    }

    @Override
    protected void initCellTitleView(BaseHoder hoder, int areaPosition) {
        ConsumeBean.DataBean dataBean = mDataBeans.get(areaPosition);
        hoder.setText(R.id.book_name, dataBean.title);
    }

    @Override
    protected int getFooterResource(int areaPosition) {
        if (areaPosition != getAreaSize() - 1)
            return R.layout.adapter_item_row_line;
        return 0;
    }


    @Override
    protected int getContentResource(int areaPosition) {
        return R.layout.adapter_consume_item;
    }

    @Override
    protected void initContentView(BaseHoder hoder, int areaPosition, int index) {
        ConsumeBean.SellListBean.DataBean dataBean = mDataBeans.get(areaPosition).mDataBeans.get(index);
        hoder.setText(R.id.create_time, dataBean.createDate);
        hoder.setText(R.id.chapter_name, dataBean.sellName);
        hoder.setText(R.id.consume_count, dataBean.sellAmount + " 红果币");
    }

    @Override
    protected int getAreaSize() {
        return mDataBeans.size();
    }

    @Override
    protected int getCellCount(int areaPosition) {
        return mDataBeans.get(areaPosition).mDataBeans.size();
    }

    public void setDataBeans(List<ConsumeBean.DataBean> dataBeans) {
        mDataBeans = dataBeans;
    }
}
