package com.hongguo.read.mvp.presenter.bookstore.channel;

import com.hongguo.common.base.CommonBean;
import com.hongguo.common.dagger.scope.ApiLife;
import com.hongguo.common.utils.rxjava.RxJavaResponseDeal;
import com.hongguo.read.base.BaseImpPresenter;
import com.hongguo.read.eventbus.BookStoreRefreshSuccessEvent;
import com.hongguo.read.mvp.contractor.bookstore.channel.BoysChannelContractor;
import com.hongguo.read.mvp.model.bookstore.RecommendItemBean;
import com.hongguo.read.retrofit.api.ApiService;
import com.hongguo.read.utils.convert.BeanConvert;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import io.reactivex.Observable;

public class BoysChannelPresenter extends BaseImpPresenter<BoysChannelContractor.IView> implements BoysChannelContractor.IPresenter{

	private static final String SYSTEM_FANTASY_TYPE_IDS  = "344";  //从服务器端默认获取的 玄幻仙侠 分类 (玄幻)

	@Inject
	public BoysChannelPresenter(@ApiLife ApiService apiService) {
		super(apiService);
	}

	@Override
	public void loading() {
		queryRecommendBook();
		queryNewBook();
		queryFinishBook();
		queryAppendFantasyItem();
		queryAppenAncient();
	}

	private void queryRecommendBook() {
		mApiService.querySelfBookByType("344", "s9", "s1", 1, CommonBean.PAGE_SIZE).flatMap(books -> {
			RecommendItemBean.ObjBean objBean = new RecommendItemBean.ObjBean();
			objBean.showtype = 3;
			objBean.status = "s9";
			objBean.sort = "s1";
			objBean.dataname = "男频力荐";
			objBean.systemMore = "344";
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
		mApiService.querySelfBookByType("341", "s0", "s0", 1, CommonBean.PAGE_SIZE).flatMap(books -> {
			RecommendItemBean.ObjBean objBean = new RecommendItemBean.ObjBean();
			objBean.showtype = 2;
			objBean.dataname = "人气作品";
			objBean.status = "s0";
			objBean.sort = "s0";
			objBean.systemMore = "341";
			BeanConvert.convertBean(books, objBean);
			return Observable.just(objBean);
		}).compose(RxJavaResponseDeal.create(this).commonDeal(books -> {
			mView.setNewBook(books);
		}));
	}

	private void queryFinishBook() {
		mApiService.querySelfBookByType("343", "s1", "s9", 1, CommonBean.PAGE_SIZE).flatMap(books -> {
			RecommendItemBean.ObjBean objBean = new RecommendItemBean.ObjBean();
			objBean.showtype = 3;
			objBean.status = "s1";
			objBean.sort = "s9";
			objBean.dataname = "经典完本";
			objBean.systemMore = "343";
			BeanConvert.convertBean(books, objBean);
			return Observable.just(objBean);
		}).compose(RxJavaResponseDeal.create(this).commonDeal(books -> {
			mView.setFinishBook(books);
		}));
	}

	/**
	 * 玄幻仙侠
	 */
	private void queryAppendFantasyItem() {

		mApiService.querySelfBookByType(SYSTEM_FANTASY_TYPE_IDS, "s9", "s9", 1, CommonBean.PAGE_SIZE).flatMap(books -> {
			RecommendItemBean.ObjBean objBean = new RecommendItemBean.ObjBean();
			objBean.showtype = 3;
			objBean.dataname = "玄幻仙侠";
			objBean.status = "s9";
			objBean.systemMore = SYSTEM_FANTASY_TYPE_IDS;
			objBean.sort = "s9";
			BeanConvert.convertBean(books, objBean);
			return Observable.just(objBean);
		}).compose(RxJavaResponseDeal.create(this).commonDeal(books -> {
			mView.setFantasyItem(books);
		}));
	}

	private void queryAppenAncient() {


		mApiService.querySelfBookByType("341", "s0", "s0", 1, CommonBean.PAGE_SIZE).flatMap(books -> {
			RecommendItemBean.ObjBean objBean = new RecommendItemBean.ObjBean();
			objBean.showtype = 3;
			objBean.dataname = "恐怖悬疑";
			objBean.status = "s0";
			objBean.systemMore = "341";
			objBean.sort = "s0";
			BeanConvert.convertBean(books, objBean);
			return Observable.just(objBean);
		}).compose(RxJavaResponseDeal.create(this).commonDeal(books -> {
			mView.setUrbanItem(books);
		}));
	}
}