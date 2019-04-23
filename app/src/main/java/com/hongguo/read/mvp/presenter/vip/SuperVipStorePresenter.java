package com.hongguo.read.mvp.presenter.vip;

import com.hongguo.read.base.BaseImpPresenter;
import com.hongguo.read.constants.CmsBanner;
import com.hongguo.read.constants.Constants;
import com.hongguo.common.dagger.scope.ApiLife;
import com.hongguo.read.eventbus.VIPStoreRefreshSuccessEvent;
import com.hongguo.read.mvp.contractor.vip.SuperVipStoreContractor;
import com.hongguo.read.mvp.model.vip.VipBean;
import com.hongguo.read.retrofit.api.ApiService;
import com.hongguo.common.utils.rxjava.RxJavaResponseDeal;
import com.losg.library.base.BaseView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class SuperVipStorePresenter extends BaseImpPresenter<SuperVipStoreContractor.IView> implements SuperVipStoreContractor.IPresenter {

    private List<VipBean.DataBean> mDataBeans;

    @Inject
    public SuperVipStorePresenter(@ApiLife ApiService apiService) {
        super(apiService);
        mDataBeans = new ArrayList<>();
    }

    @Override
    public void loading() {
        queryEventBanner();
        queryVipBooks(false);
    }

    public void queryEventBanner() {
        mApiService.getBanners(CmsBanner.EVENT_BANNER).compose(RxJavaResponseDeal.create(this).commonDeal(banners -> {
            mView.setBannerUrls(banners.data);
        }));
    }

    public void queryVipBooks(boolean refresh) {
        mApiService.queryVipBooks(Constants.BOOK_FREE_TYPE.BOOK_SVIP_LIMIT_FREE, mCurrentPage, 18).compose(RxJavaResponseDeal.create(this).commonDeal(new RxJavaResponseDeal.ResponseWithErrorListener<VipBean>() {
            @Override
            public void failure(Throwable e) {
                EventBus.getDefault().post(new VIPStoreRefreshSuccessEvent());
            }

            @Override
            public void netError() {
                super.netError();
                EventBus.getDefault().post(new VIPStoreRefreshSuccessEvent());
            }

            @Override
            public void success(VipBean books) {
                if (mCurrentPage == 1) {
                    mDataBeans.clear();
                    if (refresh)
                        EventBus.getDefault().post(new VIPStoreRefreshSuccessEvent());
                }
                mDataBeans.addAll(books.data);
                if (books.pager.total <= mDataBeans.size()) {
                    mView.refreshStatus(BaseView.RefreshStatus.LOADING_ALL, null);
                }
                mView.setVipBooks(mDataBeans);
            }
        }));
    }

    @Override
    public void refresh() {
        super.refresh();
        queryVipBooks(true);
    }

    @Override
    public void loadingMore() {
        super.loadingMore();
        queryVipBooks(false);
    }

    @Override
    public void reLoad() {
        super.reLoad();
        queryVipBooks(false);
    }
}