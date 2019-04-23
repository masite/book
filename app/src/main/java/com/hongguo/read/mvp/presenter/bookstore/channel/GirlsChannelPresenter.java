package com.hongguo.read.mvp.presenter.bookstore.channel;

import com.hongguo.common.base.CommonBean;
import com.hongguo.common.dagger.scope.ApiLife;
import com.hongguo.common.utils.rxjava.RxJavaResponseDeal;
import com.hongguo.read.base.BaseImpPresenter;
import com.hongguo.read.eventbus.BookStoreRefreshSuccessEvent;
import com.hongguo.read.mvp.contractor.bookstore.channel.GirlsChannelContractor;
import com.hongguo.read.mvp.model.bookstore.RecommendItemBean;
import com.hongguo.read.retrofit.api.ApiService;
import com.hongguo.read.utils.convert.BeanConvert;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import io.reactivex.Observable;

public class GirlsChannelPresenter extends BaseImpPresenter<GirlsChannelContractor.IView> implements GirlsChannelContractor.IPresenter {

    private static final String SYSTEM_ROMANTIC_TYPE_IDS = "340";  //从服务器端默认获取的 浪漫青春 分类 (青春)
    private static final String SYSTEM_ANCIENT_TYPE_IDS  = "346";   //从服务器端默认获取的 古现言情 分类 (同人)


    @Inject
    public GirlsChannelPresenter(@ApiLife ApiService apiService) {
        super(apiService);
    }

    @Override
    public void loading() {
        queryRecommendBook();
        queryNewBook();
        queryFinishBook();
        queryAppendRomanticItem();
        queryAppendAncient();
    }

    private void queryRecommendBook() {
        mApiService.querySelfBookByType(SYSTEM_ROMANTIC_TYPE_IDS, "s9", "s1", 1, CommonBean.PAGE_SIZE).flatMap(books -> {
            RecommendItemBean.ObjBean objBean = new RecommendItemBean.ObjBean();
            objBean.showtype = 3;
            objBean.status = "s9";
            objBean.sort = "s1";
            objBean.dataname = "女频力荐";
            objBean.systemMore = SYSTEM_ROMANTIC_TYPE_IDS;
            BeanConvert.convertBean(books, objBean);
            return Observable.just(objBean);
        }).compose(RxJavaResponseDeal.create(this).commonDeal(new RxJavaResponseDeal.ResponseWithErrorListener<RecommendItemBean.ObjBean>() {
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
            public void success(RecommendItemBean.ObjBean books) {
                mView.setRecommendBook(books);
                EventBus.getDefault().post(new BookStoreRefreshSuccessEvent());
            }
        }));
    }

    private void queryNewBook() {
        mApiService.querySelfBookByType("347", "s0", "s0", 1, CommonBean.PAGE_SIZE).flatMap(books -> {
            RecommendItemBean.ObjBean objBean = new RecommendItemBean.ObjBean();
            objBean.showtype = 2;
            objBean.dataname = "人气作品";
            objBean.status = "s0";
            objBean.sort = "s0";
            objBean.systemMore = "347";
            BeanConvert.convertBean(books, objBean);
            return Observable.just(objBean);
        }).compose(RxJavaResponseDeal.create(this).commonDeal(books -> {
            mView.setNewBook(books);
        }));
    }

    private void queryFinishBook() {
        mApiService.querySelfBookByType(SYSTEM_ROMANTIC_TYPE_IDS, "s1", "s9", 1, CommonBean.PAGE_SIZE).flatMap(books -> {
            RecommendItemBean.ObjBean objBean = new RecommendItemBean.ObjBean();
            objBean.showtype = 3;
            objBean.status = "s1";
            objBean.sort = "s9";
            objBean.dataname = "经典完本";
            objBean.systemMore = SYSTEM_ROMANTIC_TYPE_IDS;
            BeanConvert.convertBean(books, objBean);
            return Observable.just(objBean);
        }).compose(RxJavaResponseDeal.create(this).commonDeal(books -> {
            mView.setFinishBook(books);
        }));
    }


    /**
     * 查询浪漫
     */
    private void queryAppendRomanticItem() {

        mApiService.querySelfBookByType("340", "s0", "s0", 1, CommonBean.PAGE_SIZE).flatMap(books -> {
            RecommendItemBean.ObjBean objBean = new RecommendItemBean.ObjBean();
            objBean.showtype = 3;
            objBean.dataname = "浪漫青春";
            objBean.status = "s0";
            objBean.sort = "s0";
            objBean.systemMore = "340";
            BeanConvert.convertBean(books, objBean);
            return Observable.just(objBean);
        }).compose(RxJavaResponseDeal.create(this).commonDeal(books -> {
            mView.setRomanticItem(books);
        }));
    }

    private void queryAppendAncient() {

        mApiService.querySelfBookByType(SYSTEM_ANCIENT_TYPE_IDS, "s9", "s9", 1, CommonBean.PAGE_SIZE).flatMap(books -> {
            RecommendItemBean.ObjBean objBean = new RecommendItemBean.ObjBean();
            objBean.showtype = 3;
            objBean.dataname = "古现言情";
            objBean.status = "s9";
            objBean.sort = "s9";
            objBean.systemMore = SYSTEM_ANCIENT_TYPE_IDS;
            BeanConvert.convertBean(books, objBean);
            return Observable.just(objBean);
        }).compose(RxJavaResponseDeal.create(this).commonDeal(books -> {
            mView.setAncientItem(books);
        }));
    }


}