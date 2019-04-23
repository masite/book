package com.hongguo.read.adapter;

import android.content.Context;
import android.widget.TextView;

import com.hongguo.read.R;
import com.hongguo.read.constants.RankType;
import com.hongguo.read.mvp.model.book.detail.AwardRankBean;
import com.hongguo.common.widget.recycler.IosRecyclerAdapter;

import java.util.List;

/**
 * Created by losg on 2018/1/14.
 */
public class RankTopAdapter extends IosRecyclerAdapter {

    private int [] mResource = new int[]{
            R.mipmap.ic_reward_top_one_number,
            R.mipmap.ic_reward_top_two_number,
            R.mipmap.ic_reward_top_three_number
    };

    private List<AwardRankBean.DataBean> mDataBeans;
    private String mRankType;

    public RankTopAdapter(Context context, String rankType, List<AwardRankBean.DataBean> dataBeans) {
        super(context);
        mDataBeans = dataBeans;
        mRankType = rankType;
    }

    @Override
    protected int getContentResource(int areaPosition) {
        return R.layout.adapter_rank_top;
    }

    @Override
    protected void initContentView(BaseHoder hoder, int areaPosition, int index) {
        AwardRankBean.DataBean dataBean = mDataBeans.get(index);
        TextView textView = hoder.getViewById(R.id.top_number);
        if(index < 3){
            textView.setText("");
            textView.setBackgroundResource(mResource[index]);
        }else{
            textView.setText(index + 1 + "");
            textView.setBackgroundResource(R.color.base_transparent);
        }
        hoder.setText(R.id.reward_name, dataBean.nickName);
        if(mRankType.equals(RankType.RANK_MONEY_REWARD)) {
            hoder.setText(R.id.reward, dataBean.sumAmount / 100+ " 元");
        }else{
            hoder.setText(R.id.reward, dataBean.sumAmount+ " 红果币");
        }
    }

    @Override
    protected int getAreaSize() {
        return 1;
    }

    @Override
    protected int getCellCount(int areaPosition) {
        return mDataBeans.size();
    }

    public void setDataBeans(List<AwardRankBean.DataBean> dataBeans) {
        mDataBeans = dataBeans;
    }
}
