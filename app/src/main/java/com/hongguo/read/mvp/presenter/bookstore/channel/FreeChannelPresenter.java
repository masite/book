package com.hongguo.read.mvp.presenter.bookstore.channel;

import com.hongguo.read.base.BaseImpPresenter;
import com.hongguo.common.dagger.scope.ApiLife;
import com.hongguo.read.eventbus.BookStoreRefreshSuccessEvent;
import com.hongguo.read.mvp.contractor.bookstore.channel.FreeChannelContractor;
import com.hongguo.read.mvp.model.bookstore.channel.FreeChannelBean;
import com.hongguo.read.mvp.model.loading.BookPattern;
import com.hongguo.read.pattern.BookPatternUtils;
import com.hongguo.read.retrofit.api.ApiService;
import com.hongguo.common.utils.rxjava.RxJavaResponseDeal;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import io.reactivex.Observable;

public class FreeChannelPresenter extends BaseImpPresenter<FreeChannelContractor.IView> implements FreeChannelContractor.IPresenter {


    @Inject
    public FreeChannelPresenter(@ApiLife(value = ApiLife.ApiFrom.STRING) ApiService apiService) {
        super(apiService);
    }

    @Override
    public void loading() {
        queryBaiduFree();
    }

    private void queryBaiduFree() {

        mApiService.queryBaiduFree(BookPattern.getInstance().getFreeUrl()).flatMap(s -> {
            FreeChannelBean freeChannelBean = BookPatternUtils.dealFreeBaidu(s);
            return Observable.just(freeChannelBean);
        }).compose(RxJavaResponseDeal.create(this).commonDeal(new RxJavaResponseDeal.ResponseWithErrorListener<FreeChannelBean>() {
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
            public void success(FreeChannelBean freeChannelBean) {
                mView.baiduFree(freeChannelBean);
                EventBus.getDefault().post(new BookStoreRefreshSuccessEvent());
            }
        }));
    }
}