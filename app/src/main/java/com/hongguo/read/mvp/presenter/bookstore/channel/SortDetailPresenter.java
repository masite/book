package com.hongguo.read.mvp.presenter.bookstore.channel;

import com.hongguo.common.annotation.PresenterMethod;
import com.hongguo.common.dagger.scope.ApiLife;
import com.hongguo.common.utils.rxjava.RxJavaResponseDeal;
import com.hongguo.read.base.BaseImpPresenter;
import com.hongguo.read.mvp.contractor.bookstore.channel.SortDetailContractor;
import com.hongguo.read.retrofit.api.ApiService;
import com.losg.library.base.BaseView;
import com.losg.library.widget.loading.BaLoadingView;

import javax.inject.Inject;

public class SortDetailPresenter extends BaseImpPresenter<SortDetailContractor.IView> implements SortDetailContractor.IPresenter {

    private String mRankType;

    @Inject
    public SortDetailPresenter(@ApiLife ApiService apiService) {
        super(apiService);
    }

    @PresenterMethod
    public void setRankType(String rankType) {
        mRankType = rankType;
    }

    @Override
    public void loading() {
        queryRank();
    }

    @PresenterMethod
    public void queryRank() {
        mApiService.queryRank(mRankType, mCurrentPage, PAGE_SIZE).compose(RxJavaResponseDeal.create(this).loadingTag(0).commonDeal(ranks -> {
            if ((mCurrentPage == 1 && ranks.data.size() == 0)) {
                mView.changeLoadingStatus(BaLoadingView.LoadingStatus.RESULT_NULL, 0);
            } else {
                if(ranks.data.size() < PAGE_SIZE){
                    mView.refreshStatus(BaseView.RefreshStatus.LOADING_ALL, null);
                }else{
                    mView.refreshStatus(BaseView.RefreshStatus.LOADING_SUCCESS, null);
                }
            }
            mView.setRank(ranks.data, mCurrentPage == 1);
        }));
    }

    @Override
    public void loadingMore() {
        super.loadingMore();
        queryRank();
    }
}