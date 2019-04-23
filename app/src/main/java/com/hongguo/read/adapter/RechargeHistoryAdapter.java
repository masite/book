package com.hongguo.read.adapter;


import android.content.Context;

import com.hongguo.read.R;
import com.hongguo.read.mvp.model.mine.mypackage.RechargeHistoryBean;
import com.hongguo.common.widget.recycler.IosRecyclerAdapter;

import java.util.List;


public class RechargeHistoryAdapter extends IosRecyclerAdapter {

    private List<RechargeHistoryBean.DataBean> mDataBeanList;

    public RechargeHistoryAdapter(Context context, List<RechargeHistoryBean.DataBean> dataBeanList) {
        super(context);
        mDataBeanList = dataBeanList;
    }

    @Override
    protected int getContentResource(int areaPosition) {
        return R.layout.adapter_recharge_history_item;
    }

    @Override
    protected void initContentView(BaseHoder hoder, int areaPosition, int index) {
        RechargeHistoryBean.DataBean dataBean = mDataBeanList.get(index);
        hoder.setText(R.id.payAmount, "充值 "+(dataBean.payAmount/100)+" 元");
        hoder.setText(R.id.payMethod, "Alipay".equals(dataBean.payMethod) ? "支付宝" : "微信");
        hoder.setText(R.id.create_time, dataBean.createDate);
    }

    @Override
    protected int getAreaSize() {
        return 1;
    }

    @Override
    protected int getCellCount(int areaPosition) {
        return mDataBeanList.size();
    }

    public void setDataBeanList(List<RechargeHistoryBean.DataBean> dataBeanList) {
        mDataBeanList = dataBeanList;
    }
}
