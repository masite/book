package com.hongguo.read.repertory.data;

import com.hongguo.read.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by losg on 2018/2/8.
 */

public class RewardRepertory {

    public static List<RewardList> getMoneyRewardList() {
        List<RewardList> lists = new ArrayList<>();
        lists.add(createReward("2", "超喜欢", R.mipmap.ic_reward_money2, R.mipmap.ic_reward_money2_selected));
        lists.add(createReward("5", "桂花酒", R.mipmap.ic_reward_money5, R.mipmap.ic_reward_money5_selected));
        lists.add(createReward("10", "苹果", R.mipmap.ic_reward_money10, R.mipmap.ic_reward_money10_selected));
        lists.add(createReward("50", "口红", R.mipmap.ic_reward_money50, R.mipmap.ic_reward_money50_selected));
        lists.add(createReward("100", "婚纱", R.mipmap.ic_reward_money100, R.mipmap.ic_reward_money100_selected));
        lists.add(createReward("500", "钻戒一生", R.mipmap.ic_reward_money500, R.mipmap.ic_reward_money500_selected));
        return lists;
    }

    public static List<RewardList> getCoinRewardList() {
        List<RewardList> lists = new ArrayList<>();
        lists.add(createReward("188", "连环符", R.mipmap.ic_reward_coin188, R.mipmap.ic_reward_coin188_selected));
        lists.add(createReward("388", "催更符", R.mipmap.ic_reward_coin388, R.mipmap.ic_reward_coin388_selected));
        lists.add(createReward("588", "菊花", R.mipmap.ic_reward_coin588, R.mipmap.ic_reward_coin588_selected));
        lists.add(createReward("666", "情书", R.mipmap.ic_reward_coin666, R.mipmap.ic_reward_coin666_selected));
        lists.add(createReward("888", "告白", R.mipmap.ic_reward_coin888, R.mipmap.ic_reward_coin888_selected));
        lists.add(createReward("1888", "单身狗", R.mipmap.ic_reward_coin1888, R.mipmap.ic_reward_coin1888_selected));
        return lists;
    }

    private static RewardList createReward(String coin, String name, int normalResource, int selectedResource) {
        RewardList rewardList = new RewardList();
        rewardList.coin = coin;
        rewardList.name = name;
        rewardList.normalResource = normalResource;
        rewardList.selectedResource = selectedResource;
        return rewardList;
    }

    public static class RewardList {
        public int    normalResource;
        public int    selectedResource;
        public String name;
        public String coin;
    }
}
