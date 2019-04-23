package com.hongguo.read.adapter;

import android.content.Context;
import android.view.View;

import com.hongguo.read.R;
import com.hongguo.read.mvp.model.mine.recharge.RechargeBean;
import com.hongguo.common.widget.recycler.IosRecyclerAdapter;

import java.util.List;

/**
 * Created by losg on 2018/1/5.
 */

public class RechargeAdapter extends IosRecyclerAdapter {

    private int mCurrentSelectedPosition = 0;

    private List<RechargeBean.DataBean.RulesBean> mRulesBeanList;

    public RechargeAdapter(Context context, List<RechargeBean.DataBean.RulesBean> rulesBeanList) {
        super(context);
        mRulesBeanList = rulesBeanList;
    }

    @Override
    protected int getContentResource(int areaPosition) {
        return R.layout.adapter_recharge_item;
    }

    @Override
    protected void initContentView(BaseHoder hoder, int areaPosition, int index) {
        RechargeBean.DataBean.RulesBean rulesBean = mRulesBeanList.get(index);
        View eventDescribe = hoder.getViewById(R.id.event_describe);
        eventDescribe.setVisibility(rulesBean.gvalue == 0 ? View.GONE : View.VISIBLE);
        hoder.setText(R.id.give_count, rulesBean.gvalue + "红果币");
        hoder.setText(R.id.money, rulesBean.name);
        hoder.setText(R.id.give, rulesBean.tips);
        if(index == mCurrentSelectedPosition){
            hoder.getViewById(R.id.pay_choose).setSelected(true);
        }else{
            hoder.getViewById(R.id.pay_choose).setSelected(false);
        }
        hoder.getViewById(R.id.pay_choose).setOnClickListener(v->{
            mCurrentSelectedPosition = index;
            notifyDataSetChanged();
        });
    }

    public int getCurrentSelectedPosition() {
        return mCurrentSelectedPosition;
    }

    @Override
    protected int getAreaSize() {
        return 1;
    }

    @Override
    protected int getCellCount(int areaPosition) {
        return mRulesBeanList.size();
    }

}
