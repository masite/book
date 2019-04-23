package com.hongguo.read.mvp.presenter.bookstore.channel;

import com.hongguo.common.annotation.PresenterMethod;
import com.hongguo.common.dagger.scope.ApiLife;
import com.hongguo.common.utils.rxjava.RxJavaResponseDeal;
import com.hongguo.read.R;
import com.hongguo.read.base.BaseImpPresenter;
import com.hongguo.read.constants.RankType;
import com.hongguo.read.eventbus.BookStoreRefreshSuccessEvent;
import com.hongguo.read.mvp.contractor.bookstore.channel.SortChannelContractor;
import com.hongguo.read.mvp.model.search.RankBean;
import com.hongguo.read.retrofit.api.ApiService;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

public class SortChannelPresenter extends BaseImpPresenter<SortChannelContractor.IView> implements SortChannelContractor.IPresenter {

    @Inject
    public SortChannelPresenter(@ApiLife ApiService apiService) {
        super(apiService);
    }

    @Override
    public void loading() {
        queryClickRank();
        queryRecommendRank();
        queryUpdateRank();
        queryFavorRank();
        queryRewardRank();
    }

    @PresenterMethod
    public void queryClickRank() {
        mApiService.queryRank(RankType.RANK_CLICK).compose(RxJavaResponseDeal.create(this).loadingTag(0).commonDeal(new RxJavaResponseDeal.ResponseWithErrorListener<RankBean>() {
            @Override
            public void failure(Throwable e) {
                EventBus.getDefault().post(new BookStoreRefreshSuccessEvent());
            }

            @Override
            public void netError() {
                super.netError();
                EventBus.getDefault().post(new BookStoreRefreshSuccessEvent());
            }

            @Override
            public void success(RankBean rankBean) {
                rankBean.resource = R.mipmap.ic_sort_click;
                rankBean.title = "点击榜";
                rankBean.type = RankType.RANK_CLICK;
                mView.setClickRank(rankBean);
                EventBus.getDefault().post(new BookStoreRefreshSuccessEvent());
            }
        }));
    }

    @PresenterMethod
    public void queryRecommendRank() {
        mApiService.queryRank(RankType.RANK_SUGGEST).compose(RxJavaResponseDeal.create(this).commonDeal(ranks -> {
            ranks.resource = R.mipmap.ic_sort_recommend;
            ranks.title = "推荐榜";
            ranks.type = RankType.RANK_SUGGEST;
            mView.setRecommendRank(ranks);
        }));
    }

    @PresenterMethod
    public void queryUpdateRank() {
        mApiService.queryRank(RankType.RANK_UPDATE).compose(RxJavaResponseDeal.create(this).commonDeal(ranks -> {
            ranks.resource = R.mipmap.ic_sort_update;
            ranks.title = "更新榜";
            ranks.type = RankType.RANK_UPDATE;
            mView.setUpdateRank(ranks);
        }));
    }

    @PresenterMethod
    public void queryFavorRank() {
        mApiService.queryRank(RankType.RANK_FAVOR).compose(RxJavaResponseDeal.create(this).commonDeal(ranks -> {
            ranks.resource = R.mipmap.ic_sort_favor;
            ranks.title = "收藏榜";
            ranks.type = RankType.RANK_FAVOR;
            mView.setFavorRank(ranks);
        }));
    }

    @PresenterMethod
    public void queryRewardRank() {
        mApiService.queryRank(RankType.RANK_REWARD).compose(RxJavaResponseDeal.create(this).commonDeal(ranks -> {
            ranks.resource = R.mipmap.ic_sort_reward;
            ranks.title = "打赏榜";
            ranks.type = RankType.RANK_REWARD;
            mView.setRewardRank(ranks);
        }));
    }
}