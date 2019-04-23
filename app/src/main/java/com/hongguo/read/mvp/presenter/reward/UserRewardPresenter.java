package com.hongguo.read.mvp.presenter.reward;

import com.hongguo.common.annotation.PresenterMethod;
import com.hongguo.read.base.BaseImpPresenter;
import com.hongguo.read.constants.RankType;
import com.hongguo.common.dagger.scope.ApiLife;
import com.hongguo.read.mvp.contractor.reward.UserRewardContractor;
import com.hongguo.read.retrofit.api.ApiService;
import com.hongguo.common.utils.rxjava.RxJavaResponseDeal;
import com.losg.library.utils.MathUtils;

import javax.inject.Inject;

public class UserRewardPresenter extends BaseImpPresenter<UserRewardContractor.IView> implements UserRewardContractor.IPresenter{

	@Inject
	public UserRewardPresenter(@ApiLife ApiService apiService){
		super(apiService);
	}

	@Override
	public void loading() {
	}

	@PresenterMethod
	public void queryCoinTop(String bookId) {
		mApiService.queryRewardRank(bookId, RankType.RANK_COIN_REWARD, 1, 3).compose(RxJavaResponseDeal.create(this).commonDeal(rewardBean -> {
			mView.setCoinReward(rewardBean);
		}));
	}

	@PresenterMethod
	public void queryMoneyTop(String bookId) {
		mApiService.queryRewardRank(bookId, RankType.RANK_MONEY_REWARD, 1, 3).compose(RxJavaResponseDeal.create(this).commonDeal(rewardBean -> {
			mView.setMoneyReward(rewardBean);
		}));
	}

	@PresenterMethod
	public void rewardCoin(String bookId,int bookType, String bookName,String coin){
		mApiService.rewardCoin(bookId, bookName, bookType,coin).compose(RxJavaResponseDeal.create(this).widthDialog("正在打赏").commonDeal(response->{
			if(response.code == 0){
				mView.toastMessage("打赏成功");
				mView.rewardCoinSuccess();
			}else{
				mView.toastMessage("打赏失败");
			}
		}));
	}

	@PresenterMethod
	public void rewardMoney(String bookId,int bookType, String bookName,String coin, String payType){
		mApiService.rewardOrder(bookId, bookName, bookType, MathUtils.stringToInteger(coin) * 100,payType).compose(RxJavaResponseDeal.create(this).widthDialog("正在获取订单信息", true).commonDeal(response->{
			mView.setPayOrder(response.data.orderNo);
			mView.startToPay(response.data.payUrl);
		}));
	}
}