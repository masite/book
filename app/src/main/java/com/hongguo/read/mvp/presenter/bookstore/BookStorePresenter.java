package com.hongguo.read.mvp.presenter.bookstore;

import com.hongguo.read.base.BaseImpPresenter;
import com.hongguo.read.constants.CmsBanner;
import com.hongguo.common.dagger.scope.ApiLife;
import com.hongguo.read.mvp.contractor.bookstore.BookStoreContractor;
import com.hongguo.read.repertory.share.CacheRepertory;
import com.hongguo.read.retrofit.api.ApiService;
import com.hongguo.common.utils.rxjava.RxJavaResponseDeal;
import com.losg.library.widget.loading.BaLoadingView;

import javax.inject.Inject;

public class BookStorePresenter extends BaseImpPresenter<BookStoreContractor.IView> implements BookStoreContractor.IPresenter {

    @Inject
    public BookStorePresenter(@ApiLife ApiService apiService) {
        super(apiService);
    }

    @Override
    public void loading() {
        queryBanner();
    }

    private void queryBanner() {

        mFirstLoading = true;
        if (CacheRepertory.getBookStoreBanner() != null) {
            mView.setBannerUrls(CacheRepertory.getBookStoreBanner().data);
            mView.changeLoadingStatus(BaLoadingView.LoadingStatus.LADING_SUCCESS, 0);
            mFirstLoading = false;
        }
        mApiService.getBanners(CmsBanner.HOME_BANNER).compose(RxJavaResponseDeal.create(this).withLoading(mFirstLoading).commonDeal(homeBanner -> {
            mView.setBannerUrls(homeBanner.data);
            CacheRepertory.setBookStoreBanner(homeBanner);
        }));
    }
}