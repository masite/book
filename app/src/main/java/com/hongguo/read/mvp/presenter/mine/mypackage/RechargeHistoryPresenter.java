package com.hongguo.read.mvp.presenter.mine.mypackage;

import com.hongguo.common.annotation.PresenterMethod;
import com.hongguo.read.base.BaseImpPresenter;
import com.hongguo.common.dagger.scope.ApiLife;
import com.hongguo.read.mvp.contractor.mine.mypackage.RechargeHistoryContractor;
import com.hongguo.read.mvp.model.mine.mypackage.RechargeHistoryBean;
import com.hongguo.read.retrofit.api.ApiService;
import com.hongguo.common.utils.rxjava.RxJavaResponseDeal;
import com.losg.library.base.BaseView;
import com.losg.library.widget.loading.BaLoadingView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class RechargeHistoryPresenter extends BaseImpPresenter<RechargeHistoryContractor.IView> implements RechargeHistoryContractor.IPresenter {

    private List<RechargeHistoryBean.DataBean> mDataBeans;

    @Inject
    public RechargeHistoryPresenter(@ApiLife ApiService apiService) {
        super(apiService);
        mDataBeans = new ArrayList<>();
    }

    @Override
    public void loading() {
        queryUserRechargeHistory();
    }

    @PresenterMethod
    public void queryUserRechargeHistory() {
        mApiService.queryUserRechargeHistory(mCurrentPage, PAGE_SIZE).compose(RxJavaResponseDeal.create(this).loadingTag(0).commonDeal(recharges -> {
            if (mCurrentPage == 1) {
                mDataBeans.clear();
                if (recharges.data.size() == 0) {
                    mView.changeLoadingStatus(BaLoadingView.LoadingStatus.RESULT_NULL, 0);
                    return;
                }
            }
            mDataBeans.addAll(recharges.data);
            mView.setRechargeHistory(mDataBeans);
            if (mDataBeans.size() >= recharges.pager.total){
                mView.refreshStatus(BaseView.RefreshStatus.LOADING_ALL, null);
            }
        }));
    }

    @Override
    public void refresh() {
        super.refresh();
        queryUserRechargeHistory();
    }

    @Override
    public void loadingMore() {
        super.loadingMore();
        queryUserRechargeHistory();
    }

    @Override
    public void reLoad() {
        super.reLoad();
        queryUserRechargeHistory();
    }
}