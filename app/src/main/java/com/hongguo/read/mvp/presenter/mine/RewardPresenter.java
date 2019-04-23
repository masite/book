package com.hongguo.read.mvp.presenter.mine;

import com.hongguo.common.annotation.PresenterMethod;
import com.hongguo.read.base.BaseImpPresenter;
import com.hongguo.read.constants.SellType;
import com.hongguo.common.dagger.scope.ApiLife;
import com.hongguo.read.mvp.contractor.mine.RewardContractor;
import com.hongguo.read.mvp.model.mine.mypackage.ConsumeBean;
import com.hongguo.read.retrofit.api.ApiService;
import com.hongguo.common.utils.rxjava.RxJavaResponseDeal;
import com.losg.library.base.BaseView;
import com.losg.library.widget.loading.BaLoadingView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class RewardPresenter extends BaseImpPresenter<RewardContractor.IView> implements RewardContractor.IPresenter{

	private List<ConsumeBean.SellListBean.DataBean> mDataBeans;

	@Inject
	public RewardPresenter(@ApiLife ApiService apiService){
		super(apiService);
	}

	@Override
	public void loading() {
		mDataBeans = new ArrayList<>();
		queryReward();
	}

	@PresenterMethod
	public void queryReward(){
		mApiService.queryUserConsumeHistory(SellType.REWARD_BOOK,mCurrentPage, PAGE_SIZE).compose(RxJavaResponseDeal.create(this).loadingTag(0).commonDeal(rewards->{
			if(mCurrentPage == 1){
				mDataBeans.clear();
			}
			mDataBeans.addAll(rewards.data);
			if(mDataBeans.size() >= rewards.pager.total){
				mView.refreshStatus(BaseView.RefreshStatus.LOADING_ALL, null);
			}
			if(mDataBeans.size() == 0){
				mView.changeLoadingStatus(BaLoadingView.LoadingStatus.RESULT_NULL, 0);
			}

			mView.setRewrads(mDataBeans);
		}));
	}

	@Override
	public void reLoad() {
		super.reLoad();
		queryReward();
	}

	@Override
	public void loadingMore() {
		super.loadingMore();
		queryReward();
	}

	@Override
	public void refresh() {
		super.refresh();
		queryReward();
	}
}