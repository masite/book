package com.hongguo.read.mvp.presenter.reward;

import com.hongguo.common.annotation.PresenterMethod;
import com.hongguo.read.base.BaseImpPresenter;
import com.hongguo.common.base.CommonBean;
import com.hongguo.common.dagger.scope.ApiLife;
import com.hongguo.read.mvp.contractor.reward.RewardTopContractor;
import com.hongguo.read.mvp.model.book.detail.AwardRankBean;
import com.hongguo.read.retrofit.api.ApiService;
import com.hongguo.common.utils.rxjava.RxJavaResponseDeal;
import com.losg.library.base.BaseView;
import com.losg.library.widget.loading.BaLoadingView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class RewardTopPresenter extends BaseImpPresenter<RewardTopContractor.IView> implements RewardTopContractor.IPresenter {

    private List<AwardRankBean.DataBean> mDataBeans;
    private String mBookId;
    private String mRankType;


    @Inject
    public RewardTopPresenter(@ApiLife ApiService apiService) {
        super(apiService);
        mDataBeans = new ArrayList<>();
    }

    @Override
    public void loading() {
    }

    @PresenterMethod
    public void queryRewardTop(String bookid, String rankType) {
        mBookId = bookid;
        mRankType = rankType;
        mApiService.queryRewardRank(bookid, rankType, mCurrentPage, CommonBean.PAGE_SIZE).compose(RxJavaResponseDeal.create(this).commonDeal(rewardBean -> {
            if (mCurrentPage == 1) {
                mDataBeans.clear();
            }
            if (mCurrentPage == 1 && rewardBean.data.size() == 0) {
                mView.changeLoadingStatus(BaLoadingView.LoadingStatus.RESULT_NULL, 0);
            }
            if (!rewardBean.hasMore()) {
                mView.refreshStatus(BaseView.RefreshStatus.LOADING_ALL, null);
            }
            mView.setRankTops(rewardBean.data);
        }));
    }

    @Override
    public void refresh() {
        super.refresh();
        queryRewardTop(mBookId, mRankType);
    }

    @Override
    public void loadingMore() {
        super.loadingMore();
        queryRewardTop(mBookId, mRankType);
    }

    @Override
    public void reLoad() {
        super.reLoad();
        queryRewardTop(mBookId, mRankType);
    }
}
