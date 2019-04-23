package com.hongguo.read.mvp.contractor.reward;

import com.hongguo.common.base.BasePresenter;
import com.hongguo.common.base.BaseViewEx;
import com.hongguo.read.mvp.model.book.detail.AwardRankBean;

public class UserRewardContractor {

    public interface IView extends BaseViewEx {
        void setCoinReward(AwardRankBean awardRankBean);

        void setMoneyReward(AwardRankBean awardRankBean);

        void startToPay(String directUrl);

        void setPayOrder(String order);

        void rewardCoinSuccess();

    }

    public interface IPresenter extends BasePresenter {
    }
}