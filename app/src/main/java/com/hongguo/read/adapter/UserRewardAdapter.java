package com.hongguo.read.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.hongguo.read.R;
import com.hongguo.read.repertory.data.RewardRepertory;
import com.hongguo.common.widget.recycler.IosRecyclerAdapter;
import com.hongguo.common.utils.AnimUtil;

import java.util.List;

/**
 * Created by losg on 2018/2/8.
 */

public class UserRewardAdapter extends IosRecyclerAdapter {

    private int mSelectedPosition = 0;
    private List<RewardRepertory.RewardList> mRewardLists;
    private boolean mIsMoney = true;

    public UserRewardAdapter(Context context, List<RewardRepertory.RewardList> rewardLists) {
        super(context);
        mRewardLists = rewardLists;
    }

    @Override
    protected int getContentResource(int areaPosition) {
        return R.layout.adapter_user_reward_item;
    }

    @Override
    protected void initContentView(BaseHoder hoder, int areaPosition, int index) {
        RewardRepertory.RewardList rewardList = mRewardLists.get(index);
        ImageView rewardImage = hoder.getViewById(R.id.reward_image);
        if (mSelectedPosition == index) {
            hoder.getViewById(R.id.reward_name).setSelected(true);
            rewardImage.setImageResource(rewardList.selectedResource);
            AnimUtil.scaleAnim(hoder.itemView);
        } else {
            hoder.getViewById(R.id.reward_name).setSelected(false);
            rewardImage.setImageResource(rewardList.normalResource);
        }
        hoder.setText(R.id.reward_name, rewardList.name);
        hoder.setText(R.id.reward_money, mIsMoney ? rewardList.coin + "元" : rewardList.coin);
        hoder.itemView.setOnClickListener(v -> {
            mSelectedPosition = index;
            notifyDataSetChanged();
        });
    }

    @Override
    protected int getAreaSize() {
        return 1;
    }

    @Override
    protected int getCellCount(int areaPosition) {
        return mRewardLists.size();
    }

    public void setSelectedPosition(int selectedPosition) {
        mSelectedPosition = selectedPosition;
    }

    public void setRewardLists(List<RewardRepertory.RewardList> rewardLists) {
        mRewardLists = rewardLists;
    }

    public int getSelectedPosition() {
        return mSelectedPosition;
    }

    public RewardRepertory.RewardList getCurrentRewardLists() {
        return mRewardLists.get(mSelectedPosition);
    }

    public void setMoney(boolean money) {
        mIsMoney = money;
    }
}
