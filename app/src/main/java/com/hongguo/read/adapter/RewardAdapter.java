package com.hongguo.read.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.hongguo.read.R;
import com.hongguo.read.mvp.model.mine.mypackage.ConsumeBean;
import com.hongguo.common.utils.imageLoad.ImageLoadUtils;
import com.hongguo.common.widget.recycler.IosRecyclerAdapter;

import java.util.List;

/**
 * Created by losg on 2018/1/6.
 */

public class RewardAdapter extends IosRecyclerAdapter {

    private List<ConsumeBean.SellListBean.DataBean> mDataBeans;

    public RewardAdapter(Context context, List<ConsumeBean.SellListBean.DataBean> dataBeans) {
        super(context);
        mDataBeans = dataBeans;
    }

    @Override
    protected int getContentResource(int areaPosition) {
        return R.layout.adapter_reward_item;
    }

    @Override
    protected void initContentView(BaseHoder hoder, int areaPosition, int index) {
        ConsumeBean.SellListBean.DataBean dataBean = mDataBeans.get(index);
        ImageLoadUtils.loadUrl(hoder.getViewById(R.id.book_cover), dataBean.sellCover);
        hoder.setText(R.id.book_name, TextUtils.isEmpty(dataBean.masterName) ? dataBean.sellName : dataBean.masterName);
        hoder.setText(R.id.create_time, dataBean.createDate);
        hoder.setText(R.id.decribe, dataBean.sellName);
        hoder.setText(R.id.reward_info, "打赏 " + dataBean.sellAmount + " 红果币");
    }

    @Override
    protected int getAreaSize() {
        return 1;
    }

    @Override
    protected int getCellCount(int areaPosition) {
        return mDataBeans.size();
    }

    public void setDataBeans(List<ConsumeBean.SellListBean.DataBean> dataBeans) {
        mDataBeans = dataBeans;
    }
}
