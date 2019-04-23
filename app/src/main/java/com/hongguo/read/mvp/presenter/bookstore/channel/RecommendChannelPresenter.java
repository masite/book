package com.hongguo.read.mvp.presenter.bookstore.channel;

import com.hongguo.common.annotation.PresenterMethod;
import com.hongguo.common.dagger.scope.ApiLife;
import com.hongguo.common.utils.rxjava.RxJavaResponseDeal;
import com.hongguo.read.base.BaseImpPresenter;
import com.hongguo.read.eventbus.BookStoreRefreshSuccessEvent;
import com.hongguo.read.mvp.contractor.bookstore.channel.RecommendChannelContractor;
import com.hongguo.read.mvp.model.bookstore.HomeBookBean;
import com.hongguo.read.mvp.model.bookstore.RecommendItemBean;
import com.hongguo.read.repertory.share.CacheRepertory;
import com.hongguo.read.retrofit.api.ApiService;
import com.hongguo.read.utils.convert.BeanConvert;
import com.losg.library.widget.loading.BaLoadingView;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

public class RecommendChannelPresenter extends BaseImpPresenter<RecommendChannelContractor.IView> implements RecommendChannelContractor.IPresenter {

    @Inject
    @ApiLife(value = ApiLife.ApiFrom.STRING)
    ApiService mStringApi;

    private boolean mFirstLoading = true;

    @Inject
    public RecommendChannelPresenter(@ApiLife ApiService apiService) {
        super(apiService);
    }

    @Override
    public void loading() {
        if (CacheRepertory.getHotRecommendItem() != null) {
            mView.setHotsRecommend(CacheRepertory.getHotRecommendItem());
            mView.setHotsBook(CacheRepertory.getHotItem());
            mView.setNewBooks(CacheRepertory.getNewBook());
            mView.setRecomend(CacheRepertory.getRecommendItem());
            mView.setBookFavor(CacheRepertory.getFavor());
            mView.changeLoadingStatus(BaLoadingView.LoadingStatus.LADING_SUCCESS, 0);
            mFirstLoading = false;
        }
        queryHotRecommend();
        queryHots();
        queryNewBook();
        queryRecommend();
        queryFavor();
    }

    private void queryHotRecommend() {
        mApiService.queryHomeBooks("31300210", "9", "2", "300", "1", "6").compose(RxJavaResponseDeal.create(this).withLoading(mFirstLoading).loadingTag(0).commonDeal(new RxJavaResponseDeal.ResponseWithErrorListener<HomeBookBean>() {
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
            public void success(HomeBookBean books) {
                try {
                    RecommendItemBean.ObjBean objBean = BeanConvert.beanConvert(books, RecommendItemBean.ObjBean.class);
                    objBean.showtype = 1;
                    objBean.dataname = "热门推荐";
                    CacheRepertory.setHotRecommendItem(objBean);
                    mView.setHotsRecommend(objBean);
                    EventBus.getDefault().post(new BookStoreRefreshSuccessEvent());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));

    }

    private void queryNewBook() {
        mApiService.queryHomeBooks("31200210", "9", "2", "300", "1", "6").compose(RxJavaResponseDeal.create(this).withLoading(mFirstLoading).loadingTag(0).commonDeal(books -> {
            try {
                RecommendItemBean.ObjBean objBean = BeanConvert.beanConvert(books, RecommendItemBean.ObjBean.class);
                objBean.showtype = 2;
                objBean.dataname = "新书速递";
                CacheRepertory.setNewBook(objBean);
                mView.setNewBooks(objBean);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
    }

    private void queryHots() {
        mApiService.queryHomeBooks("31600210", "9", "2", "300", "1", "6").compose(RxJavaResponseDeal.create(this).withLoading(mFirstLoading).loadingTag(0).commonDeal(books -> {
            try {
                RecommendItemBean.ObjBean objBean = BeanConvert.beanConvert(books, RecommendItemBean.ObjBean.class);
                objBean.showtype = 3;
                objBean.dataname = "畅销火书";
                CacheRepertory.setHotItem(objBean);
                mView.setHotsBook(objBean);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
    }

    @PresenterMethod
    public void queryRecommend() {
        mApiService.queryHomeBooks("31100210", "9", "2", "300", "1", "6").compose(RxJavaResponseDeal.create(this).withLoading(mFirstLoading).loadingTag(0).commonDeal(books -> {
            try {
                RecommendItemBean.ObjBean objBean = BeanConvert.beanConvert(books, RecommendItemBean.ObjBean.class);
                objBean.showtype = 4;
                objBean.dataname = "主编力荐";
                CacheRepertory.setRecommendItem(objBean);
                mView.setRecomend(objBean);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
    }

    @PresenterMethod
    public void queryFavor() {

        mApiService.queryFavor().compose(RxJavaResponseDeal.create(this).withLoading(mFirstLoading).loadingTag(0).commonDeal(books->{
            try {
                RecommendItemBean.ObjBean objBean = BeanConvert.beanConvert(books, RecommendItemBean.ObjBean.class);
                objBean.showtype = 3;
                objBean.dataname = "猜你喜欢";
                CacheRepertory.setFavorItem(objBean);
                mView.setBookFavor(objBean);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));

    }
}