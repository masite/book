package com.hongguo.read.adapter;

import android.content.Context;

import com.hongguo.read.R;
import com.hongguo.read.mvp.model.mine.recharge.RechargeBean;
import com.hongguo.common.widget.recycler.IosRecyclerAdapter;

import java.util.List;


/**
 * Created time 2017/11/28.
 *
 * @author losg
 */

public class VipPayDialogAdapter extends IosRecyclerAdapter {

    private List<RechargeBean.DataBean> mItems;
    private ItemClickListener           mItemClickListener;
    private int                         mSelectPosition;

    public VipPayDialogAdapter(Context context, List<RechargeBean.DataBean> items) {
        super(context);
        mItems = items;
    }

    @Override
    protected int getCellTitleResource(int areaPosition) {
        return R.layout.dialog_pay_title;
    }

    @Override
    protected void initCellTitleView(BaseHoder hoder, int areaPosition) {
        super.initCellTitleView(hoder, areaPosition);
        RechargeBean.DataBean dataBean = mItems.get(areaPosition);
        hoder.setText(R.id.vip_title, dataBean.name);
    }

    @Override
    protected int getContentResource(int areaPosition) {
        return R.layout.adapter_vip_pay_item;
    }

    @Override
    protected void initContentView(BaseHoder hoder, int areaPosition, int index) {
        RechargeBean.DataBean.RulesBean rulesBean = mItems.get(areaPosition).rules.get(index);
        hoder.setText(R.id.day_number, rulesBean.name);
        hoder.setText(R.id.vip_selection, rulesBean.value / 100 + "元");
        hoder.getViewById(R.id.vip_selection).setSelected(index == mSelectPosition);
        hoder.itemView.setOnClickListener(v -> {
            mSelectPosition = index;
            notifyDataSetChanged();
            if (mItemClickListener != null) {
                mItemClickListener.itemClick(areaPosition, index);
            }
        });
    }

    public void setSelectPosition(int selectPosition) {
        mSelectPosition = selectPosition;
    }

    @Override
    protected int getAreaSize() {
        return mItems.size();
    }

    @Override
    protected int getCellCount(int areaPosition) {
        return mItems.get(areaPosition).rules.size();
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void itemClick(int area, int index);
    }
}
