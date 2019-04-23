package com.hongguo.read.mvp.presenter.vip;

import com.hongguo.read.base.BaseImpPresenter;
import com.hongguo.read.constants.Constants;
import com.hongguo.common.dagger.scope.ApiLife;
import com.hongguo.read.eventbus.VIPStoreRefreshSuccessEvent;
import com.hongguo.read.mvp.contractor.vip.NormalVipStoreContractor;
import com.hongguo.read.mvp.model.vip.VipBean;
import com.hongguo.read.retrofit.api.ApiService;
import com.hongguo.common.utils.rxjava.RxJavaResponseDeal;
import com.losg.library.base.BaseView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class NormalVipStorePresenter extends BaseImpPresenter<NormalVipStoreContractor.IView> implements NormalVipStoreContractor.IPresenter{

	private List<VipBean.DataBean> mDataBeans;

	@Inject
	public NormalVipStorePresenter(@ApiLife ApiService apiService) {
		super(apiService);
		mDataBeans = new ArrayList<>();
	}

	@Override
	public void loading() {
		queryVipBooks(false);
	}

	public void queryVipBooks(boolean refresh) {
		mApiService.queryVipBooks(Constants.BOOK_FREE_TYPE.BOOK_VIP_LIMIT_FREE, mCurrentPage, 23).compose(RxJavaResponseDeal.create(this).commonDeal(new RxJavaResponseDeal.ResponseWithErrorListener<VipBean>() {
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
				if (books.pager.total <= mDataBeans.size() + 3) {
					mView.refreshStatus(BaseView.RefreshStatus.LOADING_ALL, null);
				}
				if(mCurrentPage == 1){
					mView.setTopBooks(mDataBeans.subList(0, 3));
				}
				mView.setVipBooks(mDataBeans.subList(3, mDataBeans.size()));
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